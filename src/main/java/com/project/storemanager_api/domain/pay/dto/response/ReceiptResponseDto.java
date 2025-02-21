package com.project.storemanager_api.domain.pay.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.storemanager_api.domain.menu.dto.response.MenuDetailResponseDto;
import com.project.storemanager_api.domain.pay.dto.request.CreatePaymentDetailDto;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

import static com.project.storemanager_api.service.CardService.makeRandomCardNum;
import static com.project.storemanager_api.service.ReceiptService.makeRandomValue;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 읽기 전용
@Slf4j
public class ReceiptResponseDto {

    private String storeName; // 매장명 stores
    private String businessNum; // 사업자번호 (9자리 임의숫자)
    private String owner; // 대표자, users
    private String phoneNumber; // stores
    private String storePlace; // 주소 stores

    private List<MenuDetailResponseDto> menuList;

    private Long orderId; // 주문번호
    private String receiptDate; // 영수증번호, 20190729-10008, createdAt + -10000 + id
    private String placeName; // 장소 이름 places
    private String cardCompany; // cards
    private String cardNumber; // cards
    private String inputMethod; // 입력방법. 일단 Swiped 기본값
    private String approveDate; // 승인일자. 12자리의 임의 숫자,
    private String installmentPeriod; // 할부기간. 일단 일시불 기본값
    private String approveNumber; // 승인번호. 8자리 임의 숫자
    private String joinNumber; // 가맹번호. 9자리 임의숫자
    private Long totalAmount; // 승인 금액

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd a hh:mm:ss")
    private LocalDateTime createdAt; // 발행 일시


    public ReceiptResponseDto fillRestValue(ReceiptResponseDto dto,
                                            List<CreatePaymentDetailDto> payList) {
        dto.setBusinessNum(makeRandomValue(9));
        dto.setCardCompany(payList.get(0).getCardCompany());
        dto.setCardNumber(makeRandomCardNum());
        dto.setInputMethod("SWIPE");
        dto.setApproveDate(makeRandomValue(9));
        dto.setInstallmentPeriod("일시불");

        return dto;
    }
}
