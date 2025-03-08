package com.project.storemanager_api.service;

import com.project.storemanager_api.repository.StoreOperateTimeRepository;
import com.project.storemanager_api.util.DateFormatUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.project.storemanager_api.util.Constants.MESSAGE;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class StoreOperateTimeService {

    private final StoreOperateTimeRepository storeTimeRepository;

    private final OrderService orderService;

    public Map<String, Object> openStore(Long storeId) {
        storeTimeRepository.insertOpenStore(storeId); // 새로운 row 추가 (중복 방지)
        return Map.of(
                MESSAGE, "가게 오픈 시작. " + formatTimeNow()
        );
    }

    public Map<String, Object> closeStore(Long storeId) {
        // 가게에 unpaid 상태의 order가 있는지 확인
        List<Long> flag = isAllSuccess(storeId);
        String message;

        if (flag.isEmpty()) {
            storeTimeRepository.closeStore(storeId); // 오늘 날짜의 row에 closed_at 업데이트
            message = "영업을 종료합니다. " + formatTimeNow();
        } else {
            message = "아직 완료되지 않은 주문이 존재합니다. 주문번호: " + flag.stream()
                    .map(String::valueOf) // Long 값을 문자열로 변환
                    .collect(Collectors.joining(", "));
        }

        return Map.of(
                MESSAGE, message
        );
    }

    private List<Long> isAllSuccess(Long storeId) {
        return orderService.checkExistUnpaidOrder(storeId);
    }

    private String formatTimeNow() {
        return DateFormatUtil.formatLocalDateTimeDefault(LocalDateTime.now());
    }

}
