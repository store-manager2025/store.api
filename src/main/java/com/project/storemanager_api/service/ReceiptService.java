package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.pay.dto.request.CreatePayRequestDto;
import com.project.storemanager_api.domain.pay.dto.request.CreatePaymentDetailDto;
import com.project.storemanager_api.domain.pay.dto.response.ReceiptResponseDto;
import com.project.storemanager_api.domain.pay.entity.Receipt;
import com.project.storemanager_api.exception.ErrorCode;
import com.project.storemanager_api.exception.PaymentException;
import com.project.storemanager_api.repository.MenuRepository;
import com.project.storemanager_api.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ReceiptService {

    // Random 객체를 클래스 수준에서 재사용하기 위해 static으로 선언
    private static final Random random = new Random();
    private final ReceiptRepository receiptRepository;
    private final MenuRepository menuRepository;

    // 영수증을 DB에 저장
    public void saveAndResponseReceipt(CreatePayRequestDto dto) {
        Receipt receipt = makeReceipt(dto);
        receiptRepository.saveReceipt(receipt); // 1끝
    }

    // 영수증 발급하는 로직
    public ReceiptResponseDto printReceipt(Long paymentId) {
        ReceiptResponseDto receiptResponseDto = receiptRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new PaymentException(ErrorCode.INVALID_ID, "결제 정보를 찾지 못하였습니다."));


        receiptResponseDto.setMenuList(menuRepository.findMenuInOrderDtoById(receiptResponseDto.getOrderId()));

        log.info("receiptResponseDto : {}", receiptResponseDto);

        List<CreatePaymentDetailDto> payList = receiptRepository.getCardInfosByPaymentId(paymentId);

        log.info("payList size: {}", payList == null ? "null" : payList.size());

        receiptResponseDto.fillRestValue(payList);

        return receiptResponseDto;
    }

    // 영수증 조회 로직 생성하자


    private Receipt makeReceipt(CreatePayRequestDto dto) {
        Long paymentId = dto.getPaymentId();
        return Receipt.builder()
                .paymentId(paymentId)
                .storeId(dto.getStoreId())
                .orderId(dto.getOrderId())
                .placeId(dto.getPlaceId())
                .receiptDate(formatReceiptDate(paymentId))
                .approveNumber(makeRandomValue(7))
                .joinNumber(makeRandomValue(8))
                .build();
    }

    public static String makeRandomValue(int range) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < range; i++) {
            int group = random.nextInt(10);
            sb.append(group);
        }
        return sb.toString();
    }


    // 20190729-10008의 형태로 포맷하는 함수 (영수증번호)
    private String formatReceiptDate(Long id) {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateStr = now.format(formatter);
        long secondPart = 10000 + id;  // 예: id가 8이면 10008
        return dateStr + "-" + secondPart;
    }


}
