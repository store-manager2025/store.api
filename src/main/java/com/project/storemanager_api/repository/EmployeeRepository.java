package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.user.dto.request.SignUpEmpRequest;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface EmployeeRepository {

    void saveEmp(SignUpEmpRequest request);

}
