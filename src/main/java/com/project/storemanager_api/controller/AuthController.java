package com.project.storemanager_api.controller;

import com.project.storemanager_api.domain.user.dto.request.LoginRequestDto;
import com.project.storemanager_api.domain.user.dto.request.ModifyUserDto;
import com.project.storemanager_api.domain.user.dto.request.SignUpRequestDto;
import com.project.storemanager_api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping
public class AuthController {

    private final UserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity<Map<String, Object>> signUp(@RequestBody @Valid SignUpRequestDto signUpRequest) {
        log.info("request for signup: {}", signUpRequest.getName());
        userService.signUp(signUpRequest);

        return ResponseEntity.ok().body(Map.of(
                "message", "회원가입이 완료되었습니다.",
                "username", signUpRequest.getName()
        ));
    }


    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody @Valid LoginRequestDto loginRequest, HttpServletResponse response) {

        log.info("request for login: {}", loginRequest.getUsername());
        Map<String, Object> responseMap = userService.authenticate(loginRequest);

        /*
         로그인이 성공하면 클라이언트에게 2가지 인증정보를 전달해야 한다.
         1. API 요청을 위한 토큰정보를 JSON에 담아 전달하고
         2. 페이지 라우팅 요청을 위한 쿠키로 전달해야 함.
        */
        Cookie cookie = new Cookie("accessToken", responseMap.get("accessToken").toString() );
        // 쿠키의 수명, 사용 경로, 보안 등을 설정
        cookie.setMaxAge(60 * 60); // 단위: 초
        cookie.setPath("/"); // 어디서 들고다닐거냐
        cookie.setHttpOnly(true); // 보안설정 - 자바스크립트는 쿠키에 접근 불가

        // 쿠키를 클라이언트에 전송
        response.addCookie(cookie);

        return ResponseEntity.ok().body(responseMap);
    }

    @PatchMapping("/api/user")
    public ResponseEntity<Map<String, Object>> patchUserInfo(@RequestBody ModifyUserDto modifyUserDto, @AuthenticationPrincipal String email) {

        log.info("인증된 사용자의 email : {} ", email);
        userService.modifyUserInfo(modifyUserDto, email);

        return ResponseEntity.ok().body(Map.of(
                "message", "회원정보 수정이 완료되었습니다."
        ));
    }

    @DeleteMapping("/api/user")
    public ResponseEntity<Map<String, Object>> deleteUserInfo(@AuthenticationPrincipal String email) {
        userService.deleteUser(email);

        return ResponseEntity.ok().body(Map.of(
                "message", "성공적으로 탈퇴 되었습니다."
        ));
    }



    // 로그아웃 처리 API
    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {

        log.info("요청 들어옴 - {} ", response);
        // 쿠키 무효화
        Cookie cookie = new Cookie("accessToken", null);
        // 쿠키 생성과 반대로
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        // 쿠키를 클라이언트에 전송
        response.addCookie(cookie);

        return ResponseEntity.ok().body(Map.of(
                "message", "로그아웃이 처리되었습니다."
        ));
    }



}
