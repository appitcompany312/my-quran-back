package com.alisoft.hatim.jobs;

import com.alisoft.hatim.service.WsService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RollbackExpiredProgressedPages {

    private final WsService wsService;

    public RollbackExpiredProgressedPages(WsService wsService) {
        this.wsService = wsService;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void runTask() {
        wsService.rollbackExpiredProgressedPages();
    }
}
