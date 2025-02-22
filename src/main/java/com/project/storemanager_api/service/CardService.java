package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.pay.dto.request.CreatePaymentDetailDto;
import com.project.storemanager_api.domain.pay.entity.Card;
import com.project.storemanager_api.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

import static com.project.storemanager_api.domain.pay.entity.PaymentDetail.PaymentType.CARD;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class CardService {

    private final CardRepository cardRepository;


    /**
     * 결제요청이 들어왔을때, 카드정보를 저장하는 로직
     * @param payId 결제id
     * @param payList 요청에 있는 카드정보 리스트
     */
    public void saveCard(Long payId, List<CreatePaymentDetailDto> payList) {

        for (CreatePaymentDetailDto dto : payList) {
            // card 결제가 아니면 패스
            if (!dto.getPaymentType().equals(CARD)) {
                continue;
            }

            Card newCard = Card.builder()
                    .paymentId(payId)
                    .cardCompany(dto.getCardCompany())
                    .cardNumber(dto.getCardNumber())
                    .paidMoney(dto.getPaidMoney())
                    .build();
            cardRepository.saveCard(newCard);
        }

    }

    public static String makeRandomCardNum() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            // 1000 ~ 9999 범위의 4자리 숫자 생성
            int group = random.nextInt(9000) + 1000;
            sb.append(group);
            if (i < 3) {
                sb.append("-");
            }
        }
        return sb.toString();
    }
}
