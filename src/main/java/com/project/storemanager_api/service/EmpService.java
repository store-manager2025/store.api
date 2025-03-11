package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.user.dto.request.SignUpEmpRequest;
import com.project.storemanager_api.domain.user.dto.response.EmpResponseDto;
import com.project.storemanager_api.exception.EmpException;
import com.project.storemanager_api.exception.ErrorCode;
import com.project.storemanager_api.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class EmpService {

    private final EmployeeRepository employeeRepository;

    private final PasswordEncoder passwordEncoder;

    private final StoreService storeService;

    private final String DEFAULT_IMAGE = "https://search.pstatic.net/sunny/?src=https%3A%2F%2Fus.123rf.com%2F450wm%2Fyupiramos%2Fyupiramos1611%2Fyupiramos161101987%2F65283464-user-avatar-silhouette-icon-vector-illustration-design.jpg%3Fver%3D6&type=a340";



    public void signUpEmp(SignUpEmpRequest signUpRequest, Long storeId) {

        if (signUpRequest.getProfileImg().isEmpty()) signUpRequest.setProfileImg(DEFAULT_IMAGE);

        // 순수 비밀번호
        String rawPassword = signUpRequest.getPassword();
        // 암호화 작업
        String encodedPassword = passwordEncoder.encode(rawPassword);

        signUpRequest.setPassword(encodedPassword);

        signUpRequest.setStoreId(storeId);

        employeeRepository.saveEmp(signUpRequest);
    }


    public List<EmpResponseDto> getEmpList(Long storeId) {
        storeService.checkExistStore(storeId);

        List<EmpResponseDto> empList = employeeRepository.findEmpListByStoreId(storeId);
        if (empList.isEmpty()) {
            throw new EmpException(ErrorCode.EMPTY_EMP, ErrorCode.EMPTY_EMP.getMessage());
        }
        return empList;
    }
}
