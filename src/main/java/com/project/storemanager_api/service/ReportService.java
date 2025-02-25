package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.order.dto.response.OrderAllResponseDto;
import com.project.storemanager_api.domain.order.dto.response.OrderDetailResponseDto;
import com.project.storemanager_api.domain.report.dto.response.AverageValueDto;
import com.project.storemanager_api.exception.ErrorCode;
import com.project.storemanager_api.exception.StoreException;
import com.project.storemanager_api.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
// 매출 보고와 관련된 비즈니스 로직 전용 클래스
public class ReportService {

    private final OrderRepository orderRepository;
    private final ReportRepository reportRepository;
    private final PaymentRepository paymentRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;


    @Transactional // 한 매장에 대한 모든 주문 목록 조회
    public List<OrderAllResponseDto> getAllOrders(Long storeId, String status) {
        validateStoreId(storeId);
        return reportRepository.findAllListByStoreId(storeId, status);
    }


    @Transactional // 특정 기간에 대한 주문 목록 조회
    public List<OrderAllResponseDto> getPeriodOrderList(Long storeId, LocalDate startDate, LocalDate endDate, String status) {
        validateStoreId(storeId);
        return reportRepository.findPeriodOrderListByStoreId(storeId, startDate, endDate, status);
    }

    @Transactional // 특정 하루에 대한 주문 목록 조회
    public List<OrderDetailResponseDto> getDailyOrderList(Long storeId, LocalDate date, String status) {
        validateStoreId(storeId);
        List<OrderDetailResponseDto> result = reportRepository.findDailyListByStoreId(storeId, date, status);
        for (OrderDetailResponseDto dto : result) {
            dto.setMenuDetail(menuRepository.findMenuInOrderDtoById(dto.getOrderId()));
        }
        return result;
    }

    @Transactional
    public void validateStoreId(Long storeId) {
        storeRepository.findPasswordById(storeId).orElseThrow(
                () -> new StoreException(ErrorCode.STORE_NOT_FOUND, ErrorCode.STORE_NOT_FOUND.getMessage())
        );
    }

    @Transactional
    public AverageValueDto findAverageValueById(Long storeId) {
        return reportRepository.findAverageValueById(storeId).orElseThrow(
                () -> new StoreException(ErrorCode.STORE_NOT_FOUND, ErrorCode.STORE_NOT_FOUND.getMessage())
        );
    }
}
