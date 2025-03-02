package com.project.storemanager_api.api;

import com.project.storemanager_api.annotation.StoreAuthCheck;
import com.project.storemanager_api.domain.order.dto.response.OrderAllResponseDto;
import com.project.storemanager_api.domain.order.dto.response.OrderDetailResponseDto;
import com.project.storemanager_api.domain.report.dto.response.*;
import com.project.storemanager_api.domain.user.dto.response.CustomUserPrincipal;
import com.project.storemanager_api.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    // 사실 reports 테이블은 필요가 없는것 같다.
    // 컨트롤러만 ReportController 사용, orderService를 주입받아도 되지만,
    // 보고 조회전용 reportService를 따로 구축하는게 좋을듯?

    // 모든 오더 기록을 조회하는 API
    @GetMapping("/all/{storeId}") // 한 매장에 등록된 매출 전체 조회
    @StoreAuthCheck
    @PreAuthorize("hasRole('OWNER')")
    @Cacheable(value = "orderList", key = "#storeId")
    public ResponseEntity<List<OrderAllResponseDto>> getAllOrders(
            @AuthenticationPrincipal CustomUserPrincipal info,
            @PathVariable Long storeId,
            @RequestParam(value = "status", required = false, defaultValue = "SUCCESS") String status // 쿼리 파라미터로 상태값 받기
    ) {
        log.info("매출 조회 요청 - storeId: {}, status: {}", storeId, status);
        List<OrderAllResponseDto> result = reportService.getAllOrders(storeId, status);
        return ResponseEntity.ok().body(result);
    }

    // 특정 기간에 대한 주문 목록 조회
    @GetMapping
    @StoreAuthCheck
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<List<OrderAllResponseDto>> getPeriodOrderList(
            @AuthenticationPrincipal CustomUserPrincipal info,
            @RequestParam Long storeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "status", required = false, defaultValue = "SUCCESS") String status) {
        log.info("시작일 : {} , 종료일 : {}", startDate, endDate);
        List<OrderAllResponseDto> result = reportService.getPeriodOrderList(storeId, startDate, endDate, status);
        return ResponseEntity.ok().body(result);
    }

    // 하루에 대한 주문 리스트
    @GetMapping("/daily")
    @StoreAuthCheck
    @PreAuthorize("hasRole('OWNER')")
    // paymentId 까지 넣어야함
    public ResponseEntity<List<OrderDetailResponseDto>> getDailyOrderList(
            @AuthenticationPrincipal CustomUserPrincipal info,
            @RequestParam Long storeId,
            @RequestParam(value = "status", required = false, defaultValue = "SUCCESS") String status,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<OrderDetailResponseDto> result = reportService.getDailyOrderList(storeId, date, status);
        return ResponseEntity.ok().body(result);
    }

    // 매장의 총 평균 객단가 분석
    @GetMapping("/average")
    @StoreAuthCheck
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<AverageValueDto> getAverageValue(
            @AuthenticationPrincipal CustomUserPrincipal info,
            @RequestParam Long storeId) {
        AverageValueDto averageValue = reportService.findAverageValueById(storeId);
        return ResponseEntity.ok().body(averageValue);
    }

    @GetMapping("/categories")
    @StoreAuthCheck
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<List<SalesByCategoryDto>> getSalesCategory(
            @AuthenticationPrincipal CustomUserPrincipal info,
            @RequestParam Long storeId) {
        List<SalesByCategoryDto> result = reportService.findSalesCategoryByStoreId(storeId);
        return ResponseEntity.ok().body(result);
    }

    /**
     * 피크타임 계산 API
     * @param info 소유주 검증을 위한 데이터
     * @param storeId 조회를 원하는 매장의 PK
     * @param startDate require=false
     * @param endDate require=false
     * @return 1시간 별로 매출이 담긴 Dto List
     */
    @GetMapping("/peak-time")
    @StoreAuthCheck
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<List<PeakTimeGroupedResponseDto>> getPeakTime(
            @AuthenticationPrincipal CustomUserPrincipal info,
            @RequestParam Long storeId,
            @RequestParam (required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam (required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        LocalDate today = LocalDate.now();
        startDate = (startDate == null) ? today : startDate;
        endDate = (endDate == null) ? today : endDate;

        List<PeakTimeGroupedResponseDto> peakTimes = reportService.getPeakTime(storeId, startDate, endDate);
        return ResponseEntity.ok(peakTimes);
    }

    @GetMapping("/payment-type")
    @StoreAuthCheck
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<SalesByPaymentTypeResponseDto> getSalesByPaymentType(
            @AuthenticationPrincipal CustomUserPrincipal info,
            @RequestParam Long storeId) {
        SalesByPaymentTypeResponseDto result = reportService.findSalesByPaymentType(storeId);
        return ResponseEntity.ok().body(result);
    }




}
