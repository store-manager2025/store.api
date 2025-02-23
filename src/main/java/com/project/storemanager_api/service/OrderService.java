package com.project.storemanager_api.service;

import com.project.storemanager_api.domain.menu.dto.response.MenuResponseDto;
import com.project.storemanager_api.domain.order.dto.request.OrderItemRequestDto;
import com.project.storemanager_api.domain.order.dto.request.OrderRequestDto;
import com.project.storemanager_api.domain.order.dto.request.RefundOrderDto;
import com.project.storemanager_api.domain.order.dto.response.OrderAllResponseDto;
import com.project.storemanager_api.domain.order.dto.response.OrderDetailResponseDto;
import com.project.storemanager_api.domain.order.entity.Order;
import com.project.storemanager_api.exception.ErrorCode;
import com.project.storemanager_api.exception.MenuException;
import com.project.storemanager_api.exception.OrderException;
import com.project.storemanager_api.exception.StoreException;
import com.project.storemanager_api.repository.MenuRepository;
import com.project.storemanager_api.repository.OrderRepository;
import com.project.storemanager_api.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.project.storemanager_api.domain.order.entity.Order.OrderStatus.SUCCESS;
import static com.project.storemanager_api.domain.order.entity.Order.OrderStatus.UNPAID;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMenuService orderMenuService;
    private final MenuRepository menuRepository; // 메뉴 가격 조회를 위한 Repository
    private final StoreRepository storeRepository;

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

    @Transactional // 한 매장에 대한 모든 주문 목록 조회
    public List<OrderAllResponseDto> getAllOrders(Long storeId) {
        validateStoreId(storeId);
        return orderRepository.findAllListByStoreId(storeId);
    }


    @Transactional // 특정 기간에 대한 주문 목록 조회
    public List<OrderAllResponseDto> getPeriodOrderList(Long storeId, LocalDate startDate, LocalDate endDate) {
        validateStoreId(storeId);
        return orderRepository.findPeriodOrderListByStoreId(storeId, startDate, endDate);
    }

    @Transactional // 특정 하루에 대한 주문 목록 조회
    public List<OrderDetailResponseDto> getDailyOrderList(Long storeId, LocalDate date) {
        validateStoreId(storeId);
        List<OrderDetailResponseDto> result = orderRepository.findDailyListByStoreId(storeId, date);
        for (OrderDetailResponseDto dto : result) {
            dto.setMenuDetail(menuRepository.findMenuInOrderDtoById(dto.getOrderId()));
        }
        return result;
    }

    @Transactional
    public void validateStoreId(Long storeId) {
        storeRepository.findPasswordById(storeId).orElseThrow(
                () -> new StoreException(ErrorCode.STORE_NOT_FOUND, ErrorCode.STORE_NOT_FOUND.getMessage())
        );
    }

    // 메뉴들에 대한 환불요청 (부분 환불도 가능하도록 설계해야 함)
    public void refundOrder(Long orderId, List<RefundOrderDto> refundInfo) {

        Order foundOrder = validateOrder(orderId); // 주문 정보
        List<RefundOrderDto> originMenuInfos = orderMenuService.findOriginOrderMenus(orderId);
        log.info("originMenuInfos.toString() : {}", originMenuInfos.toString());
        log.info("requestRefundInfo.toString() : {}", refundInfo.toString());

        // 시나리오
        if (foundOrder.getOrderStatus().equals(SUCCESS)) {
            // 1. 선불결제 시나리오

            // 1-1. 전체 주문 취소일시 orders테이블에서 주문 상태 변경
            // 1-2. 부분 취소 일시
        } else {
            // 2. 후불결제 시나리오
            // 2-1. orders테이블에서 상태 변경

        }
    }

    // orderId 유효성 검증
    @Transactional
    public Order validateOrder(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new OrderException(ErrorCode.ORDER_NOT_FOUND, ErrorCode.ORDER_NOT_FOUND.getMessage())
        );
    }

}