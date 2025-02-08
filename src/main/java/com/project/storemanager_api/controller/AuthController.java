package com.project.storemanager_api.controller;

import com.project.storemanager_api.domain.dto.request.SignUpRequestDto;
import com.project.storemanager_api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> signUp(@RequestBody @Valid SignUpRequestDto signUpRequest) {
        log.info("request for signup: {}", signUpRequest.getName());
        userService.signUp(signUpRequest);

        return ResponseEntity.ok().body(Map.of(
                "message", "회원가입이 완료되었습니다.",
                "username", signUpRequest.getName()
        ));
    }

}
