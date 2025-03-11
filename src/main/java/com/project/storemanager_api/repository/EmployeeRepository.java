package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.user.dto.request.SignUpEmpRequest;
import com.project.storemanager_api.domain.user.dto.response.EmpResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface EmployeeRepository {

    void saveEmp(SignUpEmpRequest request);

    List<EmpResponseDto> findEmpListByStoreId(Long storeId);
}
