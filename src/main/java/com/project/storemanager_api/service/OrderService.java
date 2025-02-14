package com.project.storemanager_api.service;

import com.project.storemanager_api.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    // 오더보더 더 작은 단위의 로직처리, 오더 상세
    private final OrderMenuService orderMenuService;


}
