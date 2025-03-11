package com.project.storemanager_api.api;

import com.project.storemanager_api.domain.user.dto.request.SignUpEmpRequest;
import com.project.storemanager_api.domain.user.dto.response.EmpResponseDto;
import com.project.storemanager_api.service.EmpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static com.project.storemanager_api.util.Constants.MESSAGE;
import static com.project.storemanager_api.util.Constants.USERNAME;

@RestController
@RequestMapping("/api/emp")
@Slf4j
@RequiredArgsConstructor
public class EmpController {

    private final EmpService empService;

    // 사장이 직원의 아이디를 만들어준다.
    @PostMapping("/{storeId}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Map<String, Object>> joinEmployee(@RequestBody @Valid SignUpEmpRequest signUpRequest
            , @PathVariable("storeId") Long storeId) {
        log.info("request for signup: {}", signUpRequest);
        empService.signUpEmp(signUpRequest, storeId);

        return ResponseEntity.ok().body(Map.of(
                MESSAGE, "가입이 완료되었습니다.",
                USERNAME, signUpRequest.getName()
        ));
    }

    // 직원 목록 가져오는 API
    @GetMapping("/all/{storeId}")
    public ResponseEntity<?> getAllEmp(@PathVariable Long storeId) {
        List<EmpResponseDto> empList = empService.getEmpList(storeId);
        return ResponseEntity.ok().body(empList);
    }

}
