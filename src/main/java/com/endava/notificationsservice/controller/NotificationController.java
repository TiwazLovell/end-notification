package com.endava.notificationsservice.controller;

import com.endava.notificationsservice.dto.NotificationRequestDTO;
import com.endava.notificationsservice.dto.NotificationResponseDTO;
import com.endava.notificationsservice.enums.UserType;
import com.endava.notificationsservice.enums.ValidationType;
import com.endava.notificationsservice.service.NotificationService;
import com.endava.notificationsservice.service.PublisherSubscriberNotificationService;
import com.endava.notificationsservice.validations.annotations.ValidParameter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@Validated
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final PublisherSubscriberNotificationService subscriberNotificationService;

    @PostMapping
    public void postNotification(@RequestBody NotificationRequestDTO notificationDTO) {
        subscriberNotificationService.postNotificationToASpecificEmployee(notificationDTO);
    }

    @CrossOrigin
    @GetMapping("subscribe/employee/{userId}")
    public SseEmitter subscribeEmployee(@ValidParameter(type = ValidationType.EMPLOYEE)
                                        @PathVariable UUID userId) {
        return subscriberNotificationService.subscribe(userId, UserType.EMPLOYEE);
    }

    @CrossOrigin
    @GetMapping("subscribe/admin/{userId}")
    public SseEmitter subscribeAdmin(@ValidParameter(type = ValidationType.EMPLOYEE)
                                     @PathVariable UUID userId) {
        return subscriberNotificationService.subscribe(userId, UserType.ADMIN);
    }

    @CrossOrigin
    @GetMapping("/employee/{userId}")
    public List<NotificationResponseDTO> getAllNotificationsForAnEmployee(@ValidParameter(type = ValidationType.EMPLOYEE)
                                                                          @PathVariable UUID userId) {
        return notificationService.getAllNotificationsForASpecificEmployee(userId, UserType.EMPLOYEE);
    }

    @CrossOrigin
    @GetMapping("/admin/{userId}")
    public List<NotificationResponseDTO> getAllNotificationsForAdmin(@ValidParameter(type = ValidationType.EMPLOYEE)
                                                                     @PathVariable UUID userId) {
        return notificationService.getAllNotificationsForASpecificEmployee(userId, UserType.ADMIN);
    }

    @DeleteMapping("/{notificationId}")
    public void delete(@ValidParameter(type = ValidationType.NOTIFICATION)
                       @PathVariable UUID notificationId) {
        notificationService.delete(notificationId);
    }

    @GetMapping("/{notificationId}")
    public NotificationResponseDTO getNotificationById(@ValidParameter(type = ValidationType.NOTIFICATION)
                                                       @PathVariable UUID notificationId) {
        return notificationService.getNotificationById(notificationId);
    }

    @PatchMapping("/{notificationId}/status")
    @ResponseStatus(OK)
    public void update(@ValidParameter(type = ValidationType.NOTIFICATION)
                       @PathVariable UUID notificationId) {
        notificationService.markNotificationRead(notificationId);
    }

    @PatchMapping("/employee-notifications/{userId}")
    public void readAllNotificationsForEmployee(@ValidParameter(type = ValidationType.EMPLOYEE)
                                                @PathVariable UUID userId) {
        notificationService.readAllNotifications(userId, UserType.EMPLOYEE);
    }

    @PatchMapping("/admin-notifications/{userId}")
    public void readAllNotificationsForAdmin(@ValidParameter(type = ValidationType.EMPLOYEE)
                                             @PathVariable UUID userId) {
        notificationService.readAllNotifications(userId, UserType.ADMIN);
    }
}
