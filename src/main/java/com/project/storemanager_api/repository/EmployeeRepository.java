package com.project.storemanager_api.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface EmployeeRepository {

    void saveEmp(@Param("userId") Long userId, @Param("storeId") Long storeId);

    List<Long> findStoreIdByUserId(Long userId);
}
