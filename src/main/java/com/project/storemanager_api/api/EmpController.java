package com.project.storemanager_api.api;

import com.project.storemanager_api.domain.user.dto.request.SignUpEmpRequest;
import com.project.storemanager_api.domain.user.dto.response.EmpResponseDto;
import com.project.storemanager_api.service.EmpService;
import com.project.storemanager_api.util.DateFormatUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
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
    public ResponseEntity<List<EmpResponseDto>> getAllEmp(@PathVariable Long storeId) {
        List<EmpResponseDto> empList = empService.getEmpList(storeId);
        return ResponseEntity.ok().body(empList);
    }

    // 출근 요청
    @PostMapping("/start/{empId}")
    public ResponseEntity<Map<String, Object>> startWorker(@PathVariable Long empId) {
        log.info("request for start work: {}", empId);
        empService.startWork(empId);
        return ResponseEntity.ok().body(Map.of(
                MESSAGE, "출근을 시작합니다." + DateFormatUtil.formatLocalDateTimeDefault(LocalDateTime.now())
        ));
    }

    // 퇴근 요청
    @PostMapping("/leave/{empId}")
    public ResponseEntity<Map<String, Object>> leaveWorker(@PathVariable Long empId) {
        log.info("request for end work: {}", empId);
        empService.endWork(empId);
        return ResponseEntity.ok().body(Map.of(
                MESSAGE, "퇴근. " + DateFormatUtil.formatLocalDateTimeDefault(LocalDateTime.now())
        ));
    }

}
