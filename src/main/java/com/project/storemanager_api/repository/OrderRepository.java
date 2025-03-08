package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.order.dto.response.OrderDetailResponseDto;
import com.project.storemanager_api.domain.order.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface OrderRepository {

    void saveOrder(Order order);

    Optional<Order> findById(Long orderId);

    void updatePrice(@Param("orderId") Long orderId,
                     @Param("updatedPrice") int updatedPrice);

    // 단일 조회
    Optional<OrderDetailResponseDto> findDetailById(Long orderId);


    // 결제 상태 변경
    void updateOrderStatus(@Param("orderId") Long orderId,
                           @Param("orderStatus") String status);


    // 주문 장소로 단일 조회
    Optional<OrderDetailResponseDto> findDetailByPlaceId(Long placeId);

    Integer findTotalAmount(Long orderId);

    List<Long> getUnpaidOrders(@Param("storeId") Long storeId,
                                 @Param("currentTime") String currentTime);
}


