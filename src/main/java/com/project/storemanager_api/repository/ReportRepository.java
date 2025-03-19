package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.order.dto.response.OrderAllResponseDto;
import com.project.storemanager_api.domain.order.dto.response.OrderDetailResponseDto;
import com.project.storemanager_api.domain.report.dto.response.AverageValueDto;
import com.project.storemanager_api.domain.report.dto.response.PeakTimeRawDto;
import com.project.storemanager_api.domain.report.dto.response.SalesByCategoryDto;
import com.project.storemanager_api.domain.report.dto.response.SalesByPaymentType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Mapper
public interface ReportRepository {

    // 한 매장에 대한 전체 기록 조회
    List<OrderAllResponseDto> findAllListByStoreId(@Param("storeId") Long storeId,
                                                   @Param("status") String status);

    // 특정 기간에 대한 기록 조회
    List<OrderAllResponseDto> findPeriodOrderListByStoreId(
            @Param("storeId") Long storeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") String status);

    // 하루별 기록 상세 조회
    List<OrderDetailResponseDto> findDailyListByStoreId(
            @Param("storeId") Long storeId,
            @Param("date") LocalDate date,
            @Param("status") String status,
            @Param("size") Integer size,
            @Param("offset") Integer offset
    );


    // 매장 전체 결제내역의 평균 객단가 분석
    Optional<AverageValueDto> findAverageValueById(Long storeId);

    // 매장 카테고리별 매출 분석
    List<SalesByCategoryDto> findSalesCategoryByStoreId(Long storeId);

    // 피크타임 분석
    List<PeakTimeRawDto> findPeakTime(@Param("storeId") Long storeId,
                                      @Param("startDate") String startDate,
                                      @Param("endDate") String endDate);

    // 결제방식(카드,현금)에 따른 수입 분석
    List<SalesByPaymentType> findCardAndCash(Long storeId);

    // MIX결제 시 카드결제에 쓰인 금액 조회
    Integer findCardPayAmount(Long orderId);

}
