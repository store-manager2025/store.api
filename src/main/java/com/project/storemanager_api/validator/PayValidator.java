package com.project.storemanager_api.validator;

import com.project.storemanager_api.domain.order.entity.Order;
import com.project.storemanager_api.domain.pay.dto.request.CreatePayRequestDto;
import com.project.storemanager_api.domain.pay.dto.request.CreatePaymentDetailDto;
import com.project.storemanager_api.exception.*;
import com.project.storemanager_api.repository.OrderRepository;
import com.project.storemanager_api.repository.PlaceRepository;
import com.project.storemanager_api.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import static com.project.storemanager_api.domain.pay.entity.PaymentDetail.PaymentType;
import static com.project.storemanager_api.domain.pay.entity.PaymentDetail.PaymentType.CASH;

@Component
@Slf4j
@RequiredArgsConstructor
public class PayValidator {

    private final StoreRepository storeRepository;
    private final OrderRepository orderRepository;
    private final PlaceRepository placeRepository;

    public void validateValues(CreatePayRequestDto dto) {
        // 주문 id가 유효한지 검증
        Order foundOrder = validateOrderId(dto.getOrderId());
        // 유효하다면 storeId 저장
        dto.setStoreId(foundOrder.getStoreId());
        // place id가 유효한지 검증
        validatePlaceId(dto.getPlaceId());

        Integer sumAmount = 0;
        for (CreatePaymentDetailDto info : dto.getPayList()) {
            sumAmount += info.getPaidMoney();
        }
        // 긁어야 하는 돈보다, 요청 들어온 돈이 큰 경우
        if (foundOrder.getPrice() < sumAmount) {
            throw new PaymentException(ErrorCode.TOO_MUCH_PRICE, "필요한 금액을 초과합니다.");
        }
        for (CreatePaymentDetailDto payDetail : dto.getPayList()) {
            // payDetail.getExpiryDate() 날짜 확인
            checkExpiryDate(payDetail.getExpiryDate(), payDetail.getPaymentType());
        }
    }

    public void validateStoreId(Long storeId) {
        storeRepository.findPasswordById(storeId).orElseThrow(
                () -> new StoreException(ErrorCode.STORE_NOT_FOUND, ErrorCode.STORE_NOT_FOUND.getMessage())
        );
    }


    public Order validateOrderId(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new OrderException(ErrorCode.INVALID_ID, "주문 내역을 찾을 수 없습니다.")
        );
    }

    public void validatePlaceId(Long placeId) {
        placeRepository.findById(placeId).orElseThrow(
                () -> new PlaceException(ErrorCode.INVALID_ID, "장소 정보를 찾을 수 없습니다.")
        );
    }

    private void checkExpiryDate(String expiryDate, PaymentType paymentType) {
        // 현금 결제는 패스
        if (paymentType.equals(CASH)) {
            return;
        }
        // 문자 형식 검증
        validateExpiryDate(expiryDate);

        // 예시 -> "2025/03"은 2025년 3월 1일로 간주
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM");
        YearMonth yearMonth = YearMonth.parse(expiryDate, formatter);

        // ✅ 해당 월의 1일을 LocalDate로 변환
        LocalDate expirationDate = yearMonth.atDay(1);

        // 현재 날짜 가져오기
        LocalDate today = LocalDate.now();

        // 만료 여부 확인
        if (today.isAfter(expirationDate)) {
            throw new PaymentException(ErrorCode.DATE_EXPIRATION, ErrorCode.DATE_EXPIRATION.getMessage() + " " + expiryDate);
        }
    }

    // 문자 형식 검증
    private void validateExpiryDate(String expiryDate) {
        if (expiryDate.length() != 7) {
            throw new PaymentException(ErrorCode.NOT_CORRECT_EXPIRATION, ErrorCode.NOT_CORRECT_EXPIRATION.getMessage());
        }
        String[] parts = expiryDate.split("/"); // ["2026", "03"]
        if (parts.length != 2) {
            throw new PaymentException(ErrorCode.NOT_CORRECT_EXPIRATION, ErrorCode.NOT_CORRECT_EXPIRATION.getMessage());
        }

        int month = Integer.parseInt(parts[1]); // "03" → 3
        if (month < 1 || month > 12) {
            throw new PaymentException(ErrorCode.NOT_CORRECT_MONTH, ErrorCode.NOT_CORRECT_MONTH.getMessage());
        }
    }

}

