package com.endava.notificationsservice.service;

import com.endava.notificationsservice.dto.NotificationDTO;
import com.endava.notificationsservice.dto.NotificationResponseDTO;
import com.endava.notificationsservice.enums.Status;
import com.endava.notificationsservice.enums.UserType;
import com.endava.notificationsservice.exceptions.NotificationNotFoundException;
import com.endava.notificationsservice.mapper.NotificationMapper;
import com.endava.notificationsservice.model.NotificationEntity;
import com.endava.notificationsservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    @Value("${delete.readed.after.days}")
    private final Long deleteReadedAfterDays;

    public List<NotificationResponseDTO> getAllNotificationsForASpecificEmployee(UUID employeeID, UserType userType) {
        List<NotificationEntity> notificationList = notificationRepository.findAllByEmployeeIdAndUserTypeOrderByNotificationDateDesc(employeeID, userType);
        return notificationMapper.notificationlistToNotificationResponseDTOList(notificationList);
    }

    public void readAllNotifications(UUID employeeId, UserType userType) {
        List<NotificationEntity> notificationEntityList = notificationRepository.findAllByEmployeeIdAndUserTypeOrderByNotificationDateDesc(employeeId, userType);

        notificationEntityList.forEach(notificationEntity -> {
            notificationEntity.setStatus(Status.READ);
            notificationRepository.save(notificationEntity);
        });
    }

    public void delete(UUID notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    public NotificationResponseDTO getNotificationById(UUID notificationId) {
        NotificationEntity notificationEntity = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException(notificationId));
        return notificationMapper.notificationEntityToNotificationResponseDTO(notificationEntity);
    }

    public void update(UUID notificationId, NotificationDTO dto) {
        NotificationEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException(notificationId));
        notificationMapper.updateFromDto(dto, notification);
        notificationRepository.save(notification);

    }

    @Transactional
    public void markNotificationRead(UUID notificationId) {
        NotificationEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException(notificationId));
        notification.setStatus(Status.READ);
        notificationRepository.save(notification);

    }

    @Transactional
    public void deleteReadedNotifications() {
        log.info("called delete notifications at {}", new Date().getTime());
        LocalDateTime dateBefore = LocalDateTime.now().minusDays(deleteReadedAfterDays);
        List<NotificationEntity> list = notificationRepository.findByStatusAndNotificationDateBefore(Status.READ, dateBefore);
        notificationRepository.deleteAll(list);
    }
}
