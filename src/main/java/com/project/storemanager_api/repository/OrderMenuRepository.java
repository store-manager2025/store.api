package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.order.dto.request.OrderItemRequestDto;
import com.project.storemanager_api.domain.order.entity.OrderMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Mapper
@Repository
public interface OrderMenuRepository {

    void saveOrderMenu(@Param("orderId") Long orderId, @Param("item") OrderItemRequestDto item);

    Optional<OrderMenu> findByOrderIdAndMenuId(@Param("orderId") Long orderId, @Param("menuId") Long menuId);

    void updateQuantityAndPrice(@Param("orderId") Long orderId,
                                @Param("menuId") Long menuId,
                                @Param("quantity") Integer quantity,
                                @Param("price") Integer price);
}