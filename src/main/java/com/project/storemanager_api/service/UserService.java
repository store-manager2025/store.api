package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.dto.request.SignUpRequestDto;
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
}
