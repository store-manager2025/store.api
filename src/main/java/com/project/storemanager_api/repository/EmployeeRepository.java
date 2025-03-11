package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.user.dto.request.SignUpEmpRequest;
import com.project.storemanager_api.domain.user.dto.response.EmpResponseDto;
import com.project.storemanager_api.domain.user.entity.Employees;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Mapper
public interface EmployeeRepository {

    void saveEmp(SignUpEmpRequest request);

    List<EmpResponseDto> findEmpListByStoreId(Long storeId);

    Optional<Employees> checkExist(Long empId);

    void startWork(@Param("empId") Long empId);

    void endWork(@Param("empId") Long empId);

}
