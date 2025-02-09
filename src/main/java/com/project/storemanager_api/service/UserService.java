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
        String username = dto.getUsername();

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

        // 로그인이 성공했을 때 액세스 토큰을 전송
        return Map.of(
                "message", "로그인에 성공했습니다.",
                "name", foundUser.getName(),
                "accessToken", jwtTokenProvider.createAccessToken(foundUser.getUserId(), foundUser.getEmail())
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

    public boolean findById(Long userId) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (!foundUser.isPresent()) {
            throw new UserException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        }
        return true;
    }
}
