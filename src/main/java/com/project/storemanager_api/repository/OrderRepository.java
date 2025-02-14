package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.order.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OrderRepository {

    void saveOrder(Order order);

}


