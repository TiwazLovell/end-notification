package com.endava.notificationsservice.model;

import com.endava.notificationsservice.enums.NotificationType;
import com.endava.notificationsservice.enums.Status;
import com.endava.notificationsservice.enums.UserType;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Builder
@Table(name = "notifications")
public class NotificationEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "notification_id", nullable = false)
    private UUID notificationId;

    @Column(name = "message")
    private String message;

    @Column(name = "employee_id")
    private UUID employeeId;

    @Column(columnDefinition = "status_info")
    @Enumerated(EnumType.STRING)
    @Type(PostgreSQLEnumType.class)
    private Status status;

    @Column(columnDefinition = "user_type")
    @Enumerated(EnumType.STRING)
    @Type(PostgreSQLEnumType.class)
    private UserType userType;

    @Column(columnDefinition = "notification_type")
    @Enumerated(EnumType.STRING)
    @Type(PostgreSQLEnumType.class)
    private NotificationType notificationType;

    @Column(name = "notification_date")
    private LocalDateTime notificationDate;

}
