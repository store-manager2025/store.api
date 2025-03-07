package com.project.storemanager_api.domain.pay.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.storemanager_api.domain.menu.dto.response.MenuDetailResponseDto;
import com.project.storemanager_api.domain.pay.dto.request.CreatePaymentDetailDto;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.project.storemanager_api.domain.pay.entity.PaymentType.CARD;
import static com.project.storemanager_api.domain.pay.entity.PaymentType.CASH;
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

    private Long orderId; // 주문번호
    private String receiptDate; // 영수증번호, 20190729-10008, createdAt + -10000 + id
    private String placeName; // 장소 이름 places
    private String joinNumber; // 가맹번호. 9자리 임의숫자
    private Long totalAmount; // 승인 금액

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd a hh:mm:ss")
    private LocalDateTime createdAt; // 발행 일시

    private List<MenuDetailResponseDto> menuList;
    private List<CardInfoAboutPayDto> cardInfoList;



    public void fillRestValue(List<CreatePaymentDetailDto> payList) {
        this.setBusinessNum(makeRandomValue(9));

        List<CardInfoAboutPayDto> infoList = new ArrayList<>();

        if (payList != null) {
            for (CreatePaymentDetailDto info : payList) {
                log.info("Processing payment info: {}", info);
                if (info.getPaymentType().equals(CASH)) {
                    infoList.add(CardInfoAboutPayDto.builder()
                            .paymentType(CASH)
                            .cardCompany("")
                            .cardNumber("")
                            .inputMethod("")
                            .approveDate("")
                            .approveNumber("")
                            .paidMoney(info.getPaidMoney())
                            .installmentPeriod("")
                            .build());
                } else {
                    infoList.add(CardInfoAboutPayDto.builder()
                            .paymentType(CARD)
                            .cardCompany(info.getCardCompany())
                            .cardNumber(info.getCardNumber())
                            .inputMethod("SWIPE")
                            .approveDate(makeRandomValue(12))
                            .approveNumber(makeRandomValue(8))
                            .paidMoney(info.getPaidMoney())
                            .installmentPeriod("일시불")
                            .build());
                }
            }
        }

        log.info("Generated Card Info List: {}", infoList);
        this.setCardInfoList(infoList);
    }


}
