package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.pay.dto.request.CreatePayRequestDto;
import com.project.storemanager_api.domain.pay.dto.request.CreatePaymentDetailDto;
import com.project.storemanager_api.domain.pay.dto.response.PaymentDetailResponseDto;
import com.project.storemanager_api.domain.pay.dto.response.PaymentResponseDto;
import com.project.storemanager_api.domain.pay.dto.response.ReceiptResponseDto;
import com.project.storemanager_api.domain.pay.dto.response.RefundInfoDto;
import com.project.storemanager_api.exception.ErrorCode;
import com.project.storemanager_api.exception.PaymentException;
import com.project.storemanager_api.repository.OrderRepository;
import com.project.storemanager_api.repository.PaymentRepository;
import com.project.storemanager_api.validator.PayValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.project.storemanager_api.domain.order.entity.Order.OrderStatus.CANCELLED;
import static com.project.storemanager_api.domain.order.entity.Order.OrderStatus.SUCCESS;
import static com.project.storemanager_api.domain.pay.entity.Payment.Status;
import static com.project.storemanager_api.util.Constants.MESSAGE;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository; // 페이와 1:1로 연결된 객체

    private final PaymentDetailService paymentDetailService; // 페이와 1:n로 디테일 처리를 담당하는 객체

    private final OrderRepository orderRepository; // 주문과 연관된 데이터를 처리

    private final CardService cardService; // 카드 정보 저장

    private final PayValidator payValidator; // 입력값에 대한 검증 전용 클래스

    private final ReceiptService receiptService; // 영수증 발행용 클래스

    private final PayTransactionService payTransactionService; // 결제 흐름과 관련한 transaction 처리

    private final OrderMenuService orderMenuService;

    /**
     * 카드 결제 데이터 흐름
     * 1. 결제 진행 -> payments 생성 (상태: pending)
     * 2. 카드 결제 요청 -> PG사 API 호출 (생략)
     * 3. 결제 승인 -> payment_transactions 저장 & payments 상태 success로 변경
     * 4. 영수증 발행 -> receipts 생성
     */
    public ReceiptResponseDto requestPayment(CreatePayRequestDto dto) {

        // 모든 입력값 검증
        payValidator.validateValues(dto);

        // 1. 결제 진행 -> payments 생성 (상태: pending)
        paymentRepository.savePayment(dto);

        // 저장 후 생성된 id 받아와서 결제디테일 테이블에 저장
        Long generatedPaymentId = dto.getPaymentId();

        // 주문 상세정보 저장
        for (CreatePaymentDetailDto payDetail : dto.getPayList()) {
            // payDetail.getExpiryDate() 날짜 확인
            paymentDetailService.savePayInfo(payDetail, generatedPaymentId);

            orderMenuService.updateOrderStatusWithoutMenu(dto.getOrderId(), String.valueOf(SUCCESS));
        }
        // 결제에 사용된 카드정보 저장
        cardService.saveCard(generatedPaymentId, dto.getPayList());

        // order쪽에서의 orderStatus도 SUCCESS로 변경
        orderRepository.updateOrderStatus(dto.getOrderId(), String.valueOf(SUCCESS));

        // 3. 결제 승인 -> payment_transactions 저장 & payments 상태 success로 변경
        payTransactionService.saveTransaction(dto.getPaymentId(), dto.getTotalAmount());
        paymentRepository.changeStatus(Status.SUCCESS, dto.getPaymentId());

        // 4. 영수증 발행 -> receipts 생성
        try {
            return receiptService.saveAndResponseReceipt(dto);
        } catch (Exception e) {
            throw new PaymentException(ErrorCode.ALREADY_PAYMENT, ErrorCode.ALREADY_PAYMENT.getMessage());
        }
    }


    // 한 매장에 등록된 결제 정보 반환 메서드
    public List<PaymentResponseDto> getAllPayments(Long storeId) {
        // 1. storeId가 유효한지 검증
        payValidator.validateStoreId(storeId);

        // 2. db에 전달 후 데이터 받아옴
        return paymentRepository.findAllByStoreId(storeId);

    }

    // 결제 단일 정보 조회
    @Transactional(readOnly = true)
    public PaymentDetailResponseDto getPaymentDetail(Long paymentId) {
        PaymentDetailResponseDto result = paymentRepository.findPaymentDetail(paymentId).orElseThrow(
                () -> new PaymentException(ErrorCode.INVALID_ID, "결제 정보를 찾을 수 없습니다.")
        );
        log.info("result: {}", result.toString());

        // menuList 파싱
        result.setMenuInfo(parseMenuList(result.getMenuList()));

        return result;
    }


    public List<Map<String, Object>> parseMenuList(String data) {
        if (data == null || data.isBlank()) {
            return Collections.emptyList(); // 빈 리스트 반환
        }

        String[] segments = data.split(",");
        List<Map<String, Object>> menuList = new ArrayList<>();

        for (String seg : segments) {
            seg = seg.trim();
            Map<String, Object> menu = parseSegment(seg);

            if (menu != null) {
                menuList.add(menu); // 올바르게 파싱된 메뉴만 추가
            }
        }

        return menuList;
    }

    private Map<String, Object> parseSegment(String segment) {
        String[] tokens = segment.split("\\s+");

        String menuName = null;  // null로 초기화
        Integer quantity = null; // null로 초기화
        Integer price = null;

        for (int i = 0; i < tokens.length; i++) {
            switch (tokens[i]) {
                case "메뉴:":
                    if (i + 1 < tokens.length) {
                        menuName = tokens[i + 1];
                    }
                    break;
                case "수량:":
                    if (i + 1 < tokens.length) {
                        quantity = Integer.valueOf(tokens[i + 1]);
                    }
                    break;
                case "가격:":
                    if (i + 1 < tokens.length) {
                        price = Integer.valueOf(tokens[i + 1]);
                    }
                    break;
                default:
                    break;
            }
        }

        // 메뉴 이름, 수량, 가격이 모두 존재할 경우에만 Map에 담아 반환
        if (menuName != null && quantity != null && price != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("menuName", menuName);
            map.put("quantity", quantity);
            map.put("price", price);
            return map;
        }

        return Map.of(MESSAGE, "올바르지 못한 데이터입니다."); // 제대로 파싱되지 않았으면 null 반환
    }

    // 카드 정보도 담아볼까 싶음
    public Integer cancelAndUpdateStatus(Long paymentId) {
        RefundInfoDto originPayment = paymentRepository.findRefundOriginDataByPaymentId(paymentId).orElseThrow(
                () -> new PaymentException(ErrorCode.INVALID_ID, "결제 정보를 찾을 수 없습니다.")
        );
        log.info("originPayment: {}", originPayment.toString());

        if (originPayment.getStatus().equals(Status.PENDING)) {
            throw new PaymentException(ErrorCode.CANT_REFUND, ErrorCode.CANT_REFUND.getMessage());
        }

        if (originPayment.getStatus().equals(Status.CANCELLED)) {
            throw new PaymentException(ErrorCode.ALREADY_PAYMENT, ErrorCode.ALREADY_PAYMENT.getMessage());
        }

        paymentRepository.updateStatusByPaymentId(paymentId, String.valueOf(CANCELLED));
        return originPayment.getPaymentAmount();
    }
}
