package com.alisoft.hatim.jobs;

import com.alisoft.hatim.service.WsService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RollbackExpiredBookedPages {

    private final WsService wsService;

    public RollbackExpiredBookedPages(WsService wsService) {
        this.wsService = wsService;
    }

    @Scheduled(cron = "0 */2 * * * *")
    public void runTask() {
        wsService.rollbackExpiredBookedPages();
    }
}
