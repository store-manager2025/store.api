package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.user.dto.request.LoginRequestDto;
import com.project.storemanager_api.domain.user.dto.request.ModifyUserRequestDto;
import com.project.storemanager_api.domain.user.dto.request.SignUpRequestDto;
import com.project.storemanager_api.domain.user.entity.User;
import com.project.storemanager_api.exception.ErrorCode;
import com.project.storemanager_api.exception.UserException;
import com.project.storemanager_api.jwt.JwtTokenProvider;
import com.project.storemanager_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public void signUp(SignUpRequestDto signUpRequest) {


        userRepository.findByEmail(signUpRequest.getEmail())
                .ifPresent(m -> {throw new UserException(ErrorCode.DUPLICATE_EMAIL, ErrorCode.DUPLICATE_EMAIL.getMessage());});

        // 순수 비밀번호
        String rawPassword = signUpRequest.getPassword();
        // 암호화 작업
        String encodedPassword = passwordEncoder.encode(rawPassword);

        User newUser = signUpRequest.toEntity();
        newUser.setPassword(encodedPassword);
        userRepository.saveUser(newUser);

    }

    // 로그인 처리 (인증 처리)
    /*
        1. 클라이언트가 전달한 이메일과 패스워드를 받아야 함
        2. 이메일을 데이터베이스에 조회해서 존재하는지 유무를 확인 (가입 여부 조회)
        3. 존재한다면 회원정보를 데이터베이스에서 받아와서 비밀번호를 꺼내옴 (비밀번호 일치 여부 조회)
        4. 패스워드 일치를 검사
     */
    @Transactional(readOnly = true) // select만 하고있기에 transactional을 걸어줌
    public Map<String, Object> authenticate(LoginRequestDto dto) {
        String username = dto.getEmail();

        // 1
        User foundUser = userRepository.findByEmail(username)
                .orElseThrow(
                        () -> new UserException(ErrorCode.USER_NOT_FOUND, "존재하지 않는 회원입니다.")
                );// 조회가 실패했다면 예외 발생



        // 사용자가 입력한 패스워드와 DB에 저장된 패스워드를 추출
        String inputPassword = dto.getPassword();
        String storedPassword = foundUser.getPassword();

        // 비번이 일치하지 않으면 예외 발생
        if (!passwordEncoder.matches(inputPassword, storedPassword)) {
            throw new UserException(ErrorCode.INVALID_PASSWORD);
        }

        // 로그인이 성공했을 때 액세스/리프레시 토큰을 전송
        String refreshToken = jwtTokenProvider.createRefreshToken(foundUser.getUserId(), foundUser.getEmail());
        userRepository.updateRefreshToken(refreshToken, foundUser.getUserId());

        return Map.of(
                "message", "로그인에 성공했습니다.",
                "name", foundUser.getName(),
                "accessToken", jwtTokenProvider.createAccessToken(foundUser.getUserId(), foundUser.getEmail()),
                "refreshToken", refreshToken
        );
    }


    /**
     * 회원 정보 수정
     * @param dto - 바뀔 정보(name, password, userId)를 담은 객체
     */
    public void modifyUserInfo(ModifyUserRequestDto dto, Long userId) {

        log.info("Modify User: {}", dto);
        if (dto.getPassword().isEmpty() && dto.getName().isEmpty()) {
            throw new UserException(ErrorCode.EMPTY_DATA, ErrorCode.EMPTY_DATA.getMessage());
        }

        if (dto.getName().isEmpty()) { // password만 보내온 경우
            // 비밀번호 인코딩
            String encodedPassword = passwordEncoder.encode(dto.getPassword());
            userRepository.updatePassword(encodedPassword, userId);
        } else {
            userRepository.updateName(dto.getName(), userId);
        }
    }

    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(
                        () -> new UserException(ErrorCode.USER_NOT_FOUND, "존재하지 않는 회원입니다.")
                );// 조회가 실패했다면 예외 발생
        userRepository.deleteUser(userId);

    }


    /**
     * 리프레시 토큰을 받아 새로운 토큰들을 발급한다.
     *
     * @param refreshToken 클라이언트가 보유한 refresh token
     * @return 새로운 access token과 refresh token을 포함한 Map
     */
    public Map<String, Object> refreshToken(String refreshToken) {
        // 1. 클라이언트가 보낸 refresh token의 유효성 검사
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new UserException(ErrorCode.INVALID_TOKEN, "리프레시 토큰이 유효하지 않습니다.");
        }
        // 2. 토큰에서 사용자 id 추출
        Long userId = jwtTokenProvider.getCurrentLoginUserId(refreshToken);

        // 3. DB에서 해당 사용자를 조회 (없으면 예외 발생)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, "존재하지 않는 회원입니다."));

        // 4. 사용자에 저장된 refresh token과 클라이언트가 보낸 token 일치 여부 검사
        if (user.getRefreshToken() == null || !user.getRefreshToken().equals(refreshToken)) {
            throw new UserException(ErrorCode.INVALID_TOKEN, "리프레시 토큰이 일치하지 않습니다.");
        }

        // 5. 새로운 access token 생성
        String newAccessToken = jwtTokenProvider.createAccessToken(user.getUserId(), user.getEmail());
        // 6. 새로운 refresh token 생성 (리프레시 토큰 회전)
        String newRefreshToken = jwtTokenProvider.createRefreshToken(user.getUserId(), user.getEmail());

        // 7. DB에 새로운 refresh token 저장
        user.setRefreshToken(newRefreshToken);
        userRepository.updateRefreshToken(newRefreshToken, user.getUserId());

        log.info("리프레시 토큰 재발급 완료: userId={}, newAccessToken={}, newRefreshToken={}",
                user.getUserId(), newAccessToken, newRefreshToken);

        return Map.of(
                "message", "토큰 재발급에 성공했습니다.",
                "accessToken", newAccessToken,
                "refreshToken", newRefreshToken
        );
    }

    public boolean findById(Long userId) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (!foundUser.isPresent()) {
            throw new UserException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        }
        return true;
    }
}
