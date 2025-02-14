package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.order.dto.request.OrderItemRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OrderMenuRepository {
    void saveOrderMenu(@Param("orderId") Long orderId, @Param("item") OrderItemRequestDto item);
}