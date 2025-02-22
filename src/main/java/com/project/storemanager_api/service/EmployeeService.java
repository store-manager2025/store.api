package com.project.storemanager_api.service;

import com.project.storemanager_api.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public void saveEmp(Long userId, Long storeId) {
        employeeRepository.saveEmp(userId, storeId);
    }
}
