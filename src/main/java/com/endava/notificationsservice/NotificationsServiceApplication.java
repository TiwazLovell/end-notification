package com.endava.notificationsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableDiscoveryClient
public class NotificationsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationsServiceApplication.class, args);
    }

}
