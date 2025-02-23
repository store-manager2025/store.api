package com.project.storemanager_api.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CacheEvictScheduler {

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정(00:00:00)에 실행
    @CacheEvict(value = "orderList", allEntries = true)
    public void clearOrderCache() {
      log.info("일별 매출 캐시 초기화 완료");
    }

}
