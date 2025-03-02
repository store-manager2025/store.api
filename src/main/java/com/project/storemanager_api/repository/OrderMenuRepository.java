package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.order.dto.request.OrderItemRequestDto;
import com.project.storemanager_api.domain.order.dto.request.RefundOrderDto;
import com.project.storemanager_api.domain.order.entity.OrderMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    // 메뉴 환불 요청이 들어왔을때, 원본과 대조하기 위한 데이터를 반환
    List<RefundOrderDto> findOriginMenus(Long orderId);

    void updateStatus(@Param("orderId") Long orderId,
                      @Param("menuId") Long menuId,
                      @Param("orderStatus") String orderStatus);

    void updateOrderMenuQuantity(@Param("orderId") Long orderId,
                                 @Param("menuId") Long menuId,
                                 @Param("quantity") int quantity,
                                 @Param("orderPrice") Integer orderPrice);

    void updateOrderStatusWithoutMenuId(@Param("orderId") Long orderId,
                                        @Param("orderStatus") String orderStatus);

    Optional<Integer> findQuantityById(Long orderMenuId);

    void updateQuantityAndPriceByOrderMenuId(@Param("orderMenuId") Long orderMenuId,
                                @Param("quantity") Integer quantity,
                                @Param("price") Integer price);

    void deleteOrderMenu(Long orderMenuId);
}