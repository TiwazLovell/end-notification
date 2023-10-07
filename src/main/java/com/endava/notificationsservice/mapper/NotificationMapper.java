package com.endava.notificationsservice.mapper;

import com.endava.notificationsservice.dto.NotificationDTO;
import com.endava.notificationsservice.dto.NotificationRequestDTO;
import com.endava.notificationsservice.dto.NotificationResponseDTO;
import com.endava.notificationsservice.model.NotificationEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(imports = LocalDateTime.class)
public interface NotificationMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(NotificationDTO dto, @MappingTarget NotificationEntity notification);

    NotificationDTO notificationToNotificationDTO(NotificationEntity notification);

    List<NotificationDTO> notificationToNotificationDTOList(List<NotificationEntity> notification);

    @Mapping(target = "employeeId", source = "userID")
    @Mapping(target = "status", defaultValue = "UNREAD")
    @Mapping(target = "userType", source = "userType")
    @Mapping(target = "notificationDate", expression = "java(LocalDateTime.now())")
    NotificationEntity notificationRequestDTOToNotificationEntity(NotificationRequestDTO notificationDTO);

    @Mapping(target = "userID", source = "employeeId")
    NotificationResponseDTO notificationEntityToNotificationResponseDTO(NotificationEntity notificationEntity);

    List<NotificationResponseDTO> notificationlistToNotificationResponseDTOList(List<NotificationEntity> notification);

}
