package com.endava.notificationsservice.service;

import com.endava.notificationsservice.dto.NotificationRequestDTO;
import com.endava.notificationsservice.dto.NotificationResponseDTO;
import com.endava.notificationsservice.dto.UserNotificationID;
import com.endava.notificationsservice.enums.UserType;
import com.endava.notificationsservice.exceptions.ServerSentEventException;
import com.endava.notificationsservice.mapper.NotificationMapper;
import com.endava.notificationsservice.model.NotificationEntity;
import com.endava.notificationsservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublisherSubscriberNotificationService {
    private final Map<UserNotificationID, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final NotificationMapper notificationMapper;
    private final NotificationRepository notificationRepository;
    @Value("${sse.timeout.value}")
    private final Long timeOutValue;

    public SseEmitter subscribe(UUID employeeID, UserType userType) {
        SseEmitter sseEmitter = new SseEmitter(timeOutValue);

        UserNotificationID userNotificationID = new UserNotificationID(employeeID, userType);
        log.info(userType + " user successfully subscribe!");

        emitters.put(userNotificationID, sseEmitter);
        sseEmitter.onCompletion(() -> emitters.remove(sseEmitter));
        sseEmitter.onTimeout(() -> emitters.remove(sseEmitter));
        sseEmitter.onError((throwable) -> handleError(sseEmitter));

        return sseEmitter;
    }

    @Transactional
    public void postNotificationToASpecificEmployee(NotificationRequestDTO notificationRequestDTO) {
        NotificationEntity notificationEntity = notificationMapper.notificationRequestDTOToNotificationEntity(notificationRequestDTO);
        UserNotificationID userNotificationID = new UserNotificationID(notificationRequestDTO.getUserID(), notificationRequestDTO.getUserType());

        NotificationEntity notification = notificationRepository.save(notificationEntity);
        if (emitters.containsKey(userNotificationID)) {
            NotificationResponseDTO notificationResponseDTO = notificationMapper.notificationEntityToNotificationResponseDTO(notificationEntity);
            SseEmitter sseEmitter = emitters.get(userNotificationID);
            try {
                sseEmitter.send(SseEmitter.event()
                        .data(notificationResponseDTO, MediaType.APPLICATION_JSON));
            } catch (IOException e) {
                emitters.remove(sseEmitter);
                log.error("An error occurred when the notification was sent.");
                throw new ServerSentEventException("Unable to send notification to employee with id " + notificationRequestDTO.getUserID());
            }
        }
        log.debug(notification.toString());
    }

    private void handleError(SseEmitter sseEmitter) {
        emitters.remove(sseEmitter);
        log.error("An unexpected error occurred");
        throw new ServerSentEventException("An error occurred");
    }
}
