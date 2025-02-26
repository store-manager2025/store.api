package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.menu.dto.response.MenuResponseDto;
import com.project.storemanager_api.domain.order.dto.request.OrderItemRequestDto;
import com.project.storemanager_api.domain.order.dto.request.OrderRequestDto;
import com.project.storemanager_api.domain.order.dto.request.RefundOrderDto;
import com.project.storemanager_api.domain.order.dto.response.OrderDetailResponseDto;
import com.project.storemanager_api.domain.order.entity.Order;
import com.project.storemanager_api.exception.ErrorCode;
import com.project.storemanager_api.exception.MenuException;
import com.project.storemanager_api.exception.OrderException;
import com.project.storemanager_api.repository.MenuRepository;
import com.project.storemanager_api.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.project.storemanager_api.domain.order.entity.Order.OrderStatus.*;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMenuService orderMenuService;
    private final MenuRepository menuRepository; // 메뉴 가격 조회를 위한 Repository
    private final PaymentService paymentService;

    /**
     * 주문 요청 처리 비즈니스로직
     *
     * @param dto 가게id, 주문 장소id, 총 금액, 주문항목 리스트가 담긴 dto
     */
    public void createOrder(OrderRequestDto dto) {
        // 1. 각 주문 항목의 가격 계산 및 총 주문 금액 합산
        int totalPrice = getTotalPrice(dto);

        // 2. Order 엔티티 생성 (총 주문 금액 포함)
        Order order = Order.builder()
                .storeId(dto.getStoreId())
                .price(totalPrice)
                .placeId(dto.getPlaceId())
                .orderStatus(UNPAID) // 기본값
                .build();

        // 3. orders 테이블에 주문 저장 (INSERT 후 auto-generated key 반영)
        orderRepository.saveOrder(order);
        log.info("주문 저장 완료, 생성된 orderId: {}", order.getOrderId());

        // 4. 각 주문 항목 저장: OrderMenuService를 통해 처리 (order_menu 테이블에 저장)
        for (OrderItemRequestDto item : dto.getItems()) {
            orderMenuService.createOrderMenu(order.getOrderId(), item);
        }
    }

    // 추가 주문 로직
    public void addOrder(OrderRequestDto dto, Long orderId) {

        // 1. 우선 기존 주문 내역이 있는지 조회
        Order exist = validateOrder(orderId);

        // 2. 각 주문 항목의 가격 계산 및 총 주문 금액 합산
        int updatedPrice = getTotalPrice(dto) + exist.getPrice();

        // 3. orders 테이블에 totalPrice 새롭게 update
        orderRepository.updatePrice(orderId, updatedPrice);

        // 4. 각 주문 항목 저장: OrderMenuService를 통해 처리 (order_menu 테이블에 저장)
        for (OrderItemRequestDto item : dto.getItems()) {
            orderMenuService.createOrderMenu(orderId, item);

        }
    }

    //
    private int getTotalPrice(OrderRequestDto dto) {
        int totalPrice = 0;
        for (OrderItemRequestDto item : dto.getItems()) {
            // 메뉴 정보를 조회하여 가격 가져오기
            MenuResponseDto menu = menuRepository.findById(item.getMenuId())
                    .orElseThrow(() -> new MenuException(ErrorCode.INVALID_ID, "메뉴를 찾을 수 없습니다. 메뉴 ID: " + item.getMenuId()));
            int menuPrice = menu.getPrice(); // 예: menuId 10 → 3500, menuId 12 → 3000
            log.info("메뉴 ID: {}, 가격: {}", menu.getMenuId(), menuPrice);

            // 주문 항목의 가격 = 메뉴 가격 * 주문 수량
            int itemTotalPrice = menuPrice * item.getQuantity();
            // 각 주문 항목에 계산된 가격을 세팅 (OrderItemRequestDto에 orderPrice 필드가 있음)
            item.setOrderPrice(itemTotalPrice);
            log.info("메뉴 ID: {}, 수량: {}, 계산된 주문 금액: {}", item.getMenuId(), item.getQuantity(), itemTotalPrice);
            totalPrice += itemTotalPrice;
        }
        log.info("계산된 총 주문 금액: {}", totalPrice);
        return totalPrice;
    }


    // 주문 단일 상세조회
    public OrderDetailResponseDto getDetail(Long orderId) {

        OrderDetailResponseDto result = orderRepository.findDetailById(orderId).orElseThrow(
                () -> new OrderException(ErrorCode.ORDER_NOT_FOUND, ErrorCode.ORDER_NOT_FOUND.getMessage())
        );
        result.setMenuDetail(menuRepository.findMenuInOrderDtoById(orderId));

        return result;

    }

    // 메뉴들에 대한 환불요청 (부분 환불도 가능하도록 설계해야 함)
    public boolean refundOrder(Long orderId, List<RefundOrderDto> refundInfo) {

        Order foundOrder = validateOrder(orderId); // 주문 정보
        List<RefundOrderDto> originMenuInfos = orderMenuService.findOriginOrderMenus(orderId); // 기존 주문 정보
        boolean flag = checkRefundAll(originMenuInfos, refundInfo); // 전체 / 부분 취소 체크

        if (flag) {
            // 전체 취소 일시 시나리오
            orderRepository.updateOrderStatus(orderId, String.valueOf(CANCELLED)); // orders테이블 주문상태 변경
            if (foundOrder.getOrderStatus().equals(SUCCESS)) { // 만약 선불결제했다면
                paymentService.updateStatus(orderId, String.valueOf(CANCELLED));// payments 테이블 주문 상태 변경
            }
            // 주문에 대한 메뉴 디테일 정보도 업데이트
            for (RefundOrderDto info : originMenuInfos) {
                orderMenuService.updateOrderStatus(orderId, info.getMenuId(), String.valueOf(CANCELLED));
            }
        } else {
            // 1-2. 부분 취소 일시, 주문은 유효하기 때문에 위와 다르게 결제상태 변경하지 않음. 그대로 UNPAID
            updatePartialRefund(orderId, originMenuInfos, refundInfo);
        }
        return flag;
    }

    /**
     * 환불 요청이 들어왔을 때, 전체 주문 취소인지 확인하는 메서드
     *
     * @param originMenuInfos   기존 주문 메뉴 원본 데이터
     * @param requestRefundInfo 환불 요청이 들어온 데이터
     * @return 전체 취소면 true, 아니면 false
     */
    private boolean checkRefundAll(List<RefundOrderDto> originMenuInfos, List<RefundOrderDto> requestRefundInfo) {
        // 기존 주문 정보를 Map<menuId, quantity>로 변환
        Map<Long, Integer> originOrderMap = originMenuInfos.stream()
                .collect(Collectors.toMap(RefundOrderDto::getMenuId, RefundOrderDto::getQuantity));
        log.info("originOrderMap: {}", originOrderMap);

        // 요청된 환불 정보를 Map<menuId, quantity>로 변환
        Map<Long, Integer> refundRequestMap = requestRefundInfo.stream()
                .collect(Collectors.toMap(RefundOrderDto::getMenuId, RefundOrderDto::getQuantity));
        log.info("refundRequestMap: {}", refundRequestMap);

        // 두 개의 Map을 비교하여 모든 menuId와 quantity가 일치하는지 확인
        return originOrderMap.equals(refundRequestMap);
    }

    /**
     * 부분 환불 처리 - 기존 수량과 요청된 환불 수량 비교 후 처리
     */
    private void updatePartialRefund(Long orderId, List<RefundOrderDto> originMenuInfos, List<RefundOrderDto> refundInfo) {
        // 기존 주문 정보를 Map<menuId, quantity>로 변환
        Map<Long, Integer> originOrderMap = originMenuInfos.stream()
                .collect(Collectors.toMap(RefundOrderDto::getMenuId, RefundOrderDto::getQuantity));

        // 요청된 환불 정보를 Map<menuId, quantity>로 변환
        Map<Long, Integer> refundRequestMap = refundInfo.stream()
                .collect(Collectors.toMap(RefundOrderDto::getMenuId, RefundOrderDto::getQuantity));

        Integer totalRefundMoney = 0; // 전체 차감 금액
        Integer price = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderException(ErrorCode.ORDER_NOT_FOUND, ErrorCode.ORDER_NOT_FOUND.getMessage())
        ).getPrice(); // 원금

        for (Map.Entry<Long, Integer> entry : refundRequestMap.entrySet()) {
            Long menuId = entry.getKey();
            Integer refundQuantity = entry.getValue();
            Integer originQuantity = originOrderMap.get(menuId);

            if (originQuantity == null) {
                throw new MenuException(ErrorCode.INVALID_ID, "해당 메뉴가 존재하지 않습니다: " + menuId);
            }

            if (refundQuantity.equals(originQuantity)) {
                // 기존 주문 수량과 환불 수량이 동일하면, 주문 상태를 CANCELLED로 변경
                orderMenuService.updateOrderStatus(orderId, menuId, String.valueOf(CANCELLED));
            } else if (refundQuantity < originQuantity) {
                // 기존 주문 수량보다 환불 수량이 적다면, 수량만 감소
                // 여기서 order_menu.order_price 업데이트
                int updatedQuantity = originQuantity - refundQuantity; // 업데이트된 수량
                // 현재 메뉴 1개 가격 * 업데이트된 수량 = 지불해야할 금액
                Integer updatePrice = menuRepository.findPriceById(menuId) * updatedQuantity;
                log.info("updatedPrice - {}", updatePrice);
                totalRefundMoney += updatePrice;
                orderMenuService.updateMenuQuantity(orderId, menuId, updatedQuantity, updatePrice);
            } else {
                throw new OrderException(ErrorCode.DONT_OVER_QUANTITY, "환불 요청 수량이 주문 수량을 초과할 수 없습니다.");
            }
        }
        log.info("totalRefundMoney - {}", totalRefundMoney);
            // 여기서 order.price업데이트
        orderRepository.updatePrice(orderId, price - totalRefundMoney);

    }


    // orderId 유효성 검증
    public Order validateOrder(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new OrderException(ErrorCode.ORDER_NOT_FOUND, ErrorCode.ORDER_NOT_FOUND.getMessage())
        );
    }

}