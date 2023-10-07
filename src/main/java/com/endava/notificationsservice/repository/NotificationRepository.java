package com.endava.notificationsservice.repository;

import com.endava.notificationsservice.enums.Status;
import com.endava.notificationsservice.enums.UserType;
import com.endava.notificationsservice.model.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, UUID> {

    List<NotificationEntity> findByStatusAndNotificationDateBefore(Status status, LocalDateTime localDateTime);

    List<NotificationEntity> findAllByEmployeeId(UUID employeeId);

    List<NotificationEntity> findAllByEmployeeIdAndUserTypeOrderByNotificationDateDesc(UUID userID, UserType userType);

    boolean existsNotificationEntitiesByEmployeeId(UUID employeeId);

    boolean existsNotificationEntityByNotificationId(UUID notificationId);
}