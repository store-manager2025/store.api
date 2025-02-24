package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.order.dto.response.OrderAllResponseDto;
import com.project.storemanager_api.domain.order.dto.response.OrderDetailResponseDto;
import com.project.storemanager_api.domain.order.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

    // 한 매장에 대한 전체 기록 조회
    List<OrderAllResponseDto> findAllListByStoreId(Long storeId);

    // 특정 기간에 대한 기록 조회
    List<OrderAllResponseDto> findPeriodOrderListByStoreId(
            @Param("storeId") Long storeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // 하루별 기록 상세 조회
    List<OrderDetailResponseDto> findDailyListByStoreId(@Param("storeId") Long storeId,
                                                        @Param("date") LocalDate date);

    // 결제 상태 변경
    void updateOrderStatus(@Param("orderId") Long orderId,
                           @Param("orderStatus") String status);

}


