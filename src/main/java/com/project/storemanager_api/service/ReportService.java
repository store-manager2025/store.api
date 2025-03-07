package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.order.dto.response.OrderAllResponseDto;
import com.project.storemanager_api.domain.order.dto.response.OrderDetailResponseDto;
import com.project.storemanager_api.domain.report.dto.response.*;
import com.project.storemanager_api.exception.ErrorCode;
import com.project.storemanager_api.exception.StoreException;
import com.project.storemanager_api.repository.MenuRepository;
import com.project.storemanager_api.repository.ReportRepository;
import com.project.storemanager_api.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
// 매출 보고와 관련된 비즈니스 로직 전용 클래스
public class ReportService {

    private final ReportRepository reportRepository;
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
        log.info("result : {}", result);
        for (OrderDetailResponseDto dto : result) {
            dto.setMenuDetail(menuRepository.findMenuInOrderDtoById(dto.getOrderId()));
        }
        return result;
    }

    // 매장 전체 결제내역의 평균 객단가 분석
    @Transactional
    public AverageValueDto findAverageValueById(Long storeId) {
        return reportRepository.findAverageValueById(storeId).orElseThrow(
                () -> new StoreException(ErrorCode.STORE_NOT_FOUND, ErrorCode.STORE_NOT_FOUND.getMessage())
        );
    }

    // 매장 카테고리별 매출 분석
    @Transactional
    public List<SalesByCategoryDto> findSalesCategoryByStoreId(Long storeId) {
        List<SalesByCategoryDto> result = reportRepository.findSalesCategoryByStoreId(storeId);
        // 퍼센테이지 연산 후 set
        return calcRatio(result);
    }


    // 피크타임 조회
    @Transactional
    public List<PeakTimeGroupedResponseDto> getPeakTime(Long storeId, LocalDate startDate, LocalDate endDate) {
        String start = (startDate != null) ? startDate.toString() : null;
        String end = (endDate != null) ? endDate.toString() : null;

        List<PeakTimeRawDto> peakTimeList = reportRepository.findPeakTime(storeId, start, end);

        // 날짜별로 그룹핑
        Map<LocalDate, List<PeakTimeDetailDto>> groupedData = peakTimeList.stream()
                .collect(Collectors.groupingBy(
                        PeakTimeRawDto::getDate,  // ✅ 올바른 date 필드 사용
                        Collectors.mapping(dto -> new PeakTimeDetailDto(dto.getTimeRange(), dto.getAmount()), Collectors.toList())
                ));

        // 변환된 리스트 반환
        List<PeakTimeGroupedResponseDto> result = groupedData.entrySet().stream()
                .map(entry -> new PeakTimeGroupedResponseDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        log.info("Grouped Peak Time List: {} ", result);
        return result;
    }

    @Transactional
    public SalesByPaymentTypeResponseDto findSalesByPaymentType(Long storeId) {
        List<SalesByPaymentType> paymentList = reportRepository.findCardAndCash(storeId);

        // 전체 매출 합계
        Integer totalSales = paymentList.stream()
                .mapToInt(SalesByPaymentType::getAmount)
                .sum();

        // 비율 계산 후 DTO 변환
        List<SalesByPaymentType> detailList = paymentList.stream()
                .map(payment -> SalesByPaymentType.builder()
                        .type(payment.getType())
                        .amount(payment.getAmount())
                        .ratio(String.format("%.1f%%", (payment.getAmount() / (double) totalSales) * 100))
                        .build())
                .collect(Collectors.toList());

        return SalesByPaymentTypeResponseDto.builder()
                .totalAmount(totalSales)
                .typeAndDetail(detailList)
                .build();
    }



    public void validateStoreId(Long storeId) {
        storeRepository.findPasswordById(storeId).orElseThrow(
                () -> new StoreException(ErrorCode.STORE_NOT_FOUND, ErrorCode.STORE_NOT_FOUND.getMessage())
        );
    }

    private List<SalesByCategoryDto> calcRatio(List<SalesByCategoryDto> result) {
        double sumOfAllCategory = result.stream()
                .mapToDouble(SalesByCategoryDto::getTotalSales)
                .sum();
        log.info("sumOfAllCategory 값: {}", sumOfAllCategory);
        for (SalesByCategoryDto dto : result) {
            // 소숫점 한자리까지 백분률 계산 후 %를 붙혀 문자열로 변환 후 Set
            dto.setRatio((Math.round((dto.getTotalSales() / sumOfAllCategory) * 1000) / 10.0) + "%");
        }
        return result;
    }
}
