package com.project.storemanager_api.service;

import com.project.storemanager_api.repository.StoreOperateTimeRepository;
import com.project.storemanager_api.util.DateFormatUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

import static com.project.storemanager_api.util.Constants.MESSAGE;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class StoreOperateTimeService {

    private final StoreOperateTimeRepository storeTimeRepo;

    public Map<String, Object> openStore(Long storeId) {
        storeTimeRepo.insertOpenStore(storeId); // 새로운 row 추가 (중복 방지)
        return Map.of(
                MESSAGE, "가게 오픈 시작. " + formatTimeNow()
        );
    }

    public Map<String, Object> closeStore(Long storeId) {
        storeTimeRepo.closeStore(storeId); // 오늘 날짜의 row에 closed_at 업데이트
        return Map.of(
                MESSAGE, "영업을 종료합니다. " + formatTimeNow()
        );
    }

    private String formatTimeNow() {
        return DateFormatUtil.formatLocalDateTimeDefault(LocalDateTime.now());
    }

}
