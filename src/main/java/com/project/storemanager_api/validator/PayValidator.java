package com.project.storemanager_api.validator;

import com.project.storemanager_api.domain.order.entity.Order;
import com.project.storemanager_api.domain.pay.dto.request.CreatePayRequestDto;
import com.project.storemanager_api.exception.*;
import com.project.storemanager_api.repository.OrderRepository;
import com.project.storemanager_api.repository.PlaceRepository;
import com.project.storemanager_api.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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

        // 긁어야 하는 돈보다, 요청 들어온 돈이 큰 경우
        if (foundOrder.getPrice() < dto.getTotalAmount()) {
            throw new PaymentException(ErrorCode.TOO_MUCH_PRICE, "필요한 금액을 초과합니다.");
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
}

