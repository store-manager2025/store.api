package com.project.storemanager_api.api;

import com.project.storemanager_api.domain.order.dto.request.OrderMenuCancelDto;
import com.project.storemanager_api.service.OrderMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.project.storemanager_api.util.Constants.MESSAGE;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/orders")
public class OrderMenuController {

    private final OrderMenuService orderMenuService;

    /**
     * 주문 메뉴 삭제 API
     *
     * @param orderMenuId 취소를 원하는 메뉴정보의 id
     * @param requestDto 취소를 원하는 메뉴 수량과 menuId를 담은 dto
     * @return message
     */
    @DeleteMapping("/{orderMenuId}")
    public ResponseEntity<Map<String, Object>> deleteOrderMenuId(
            @PathVariable("orderMenuId") Long orderMenuId,
            @RequestBody OrderMenuCancelDto requestDto) {
        log.info("orderMenuId = {}", orderMenuId);
        orderMenuService.cancelMenu(orderMenuId, requestDto);
        return ResponseEntity.ok().body(Map.of(
                MESSAGE, "메뉴가 삭제되었습니다."
        ));
    }
}
