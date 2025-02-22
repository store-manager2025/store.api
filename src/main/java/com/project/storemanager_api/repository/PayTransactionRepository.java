package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.pay.entity.PayTransaction;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface PayTransactionRepository {

    void saveTransaction(PayTransaction payTransaction);

}
