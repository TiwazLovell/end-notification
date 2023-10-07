package com.endava.notificationsservice.jobs;

import com.endava.notificationsservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationsCleaner {

    private static final Logger log = LoggerFactory.getLogger(NotificationsCleaner.class);
    private final NotificationService notificationService;

    @Scheduled(cron = "${cron.express.delete.readed}")
    public void deletingNotifications() {
        notificationService.deleteReadedNotifications();
        log.info(" Clean database process {} has been finished");
    }
}
