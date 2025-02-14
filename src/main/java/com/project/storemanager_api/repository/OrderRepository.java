package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.order.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Mapper
@Repository
public interface OrderRepository {

    void saveOrder(Order order);

    Optional<Order> findById(Long orderId);

    void updatePrice(@Param("orderId") Long orderId, @Param("updatedPrice") int updatedPrice);
}


