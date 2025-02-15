package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.order.dto.response.OrderDetailResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback
class OrderServiceTest {

    @Autowired
    private OrderService orderService;


    @Test
    @DisplayName("오더 디테일 뽑아오기")
    void getOrderDetail() {
        //given
        Long orderId = 5L;
        //when
        OrderDetailResponseDto detail = orderService.getDetail(orderId);
        //then
        System.out.println("detail = " + detail);
    }


}