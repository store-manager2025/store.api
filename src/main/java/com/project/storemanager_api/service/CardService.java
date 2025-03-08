package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.pay.dto.request.CreatePayRequestDto;
import com.project.storemanager_api.domain.pay.entity.Card;
import com.project.storemanager_api.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class CardService {

    private final CardRepository cardRepository;


    /**
     * 결제요청이 들어왔을때, 카드정보를 저장하는 로직
     * @param payId 결제id
     * @param dto 요청에 있는 카드정보 리스트
     */
    public void saveCard(Long payId, CreatePayRequestDto dto) {

            Card newCard = Card.builder()
                    .paymentId(payId)
                    .cardCompany(dto.getCardCompany())
                    .cardNumber(dto.getCardNumber())
                    .paidMoney(dto.getPaidMoney())
                    .expiryDate(dto.getExpiryDate())
                    .build();
            cardRepository.saveCard(newCard);
        }

    }

