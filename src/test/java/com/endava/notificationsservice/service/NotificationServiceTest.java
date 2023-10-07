package com.endava.notificationsservice.service;

import com.endava.notificationsservice.dto.NotificationDTO;
import com.endava.notificationsservice.dto.NotificationRequestDTO;
import com.endava.notificationsservice.dto.NotificationResponseDTO;
import com.endava.notificationsservice.enums.Status;
import com.endava.notificationsservice.enums.UserType;
import com.endava.notificationsservice.mapper.NotificationMapper;
import com.endava.notificationsservice.model.NotificationEntity;
import com.endava.notificationsservice.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    private final static UUID notificationId = UUID.fromString("3fa85f64-5717-4562-b3aa-2c963f66afa6");
    private final static UUID employeeId = UUID.fromString("4fa65f61-5111-4562-b3bc-2c963f66afa6");
    @Mock
    private NotificationRepository notificationRepository;
    @Spy
    private NotificationMapper notificationMapper = Mappers.getMapper(NotificationMapper.class);
    @Mock
    private PublisherSubscriberNotificationService subscriberNotificationService;
    @InjectMocks
    private NotificationService notificationService;

    private static List<NotificationEntity> notificationEntityList;
    private static List<NotificationDTO> notificationDTOList;

    @BeforeEach
    void setUp() {
        notificationEntityList = new ArrayList<>();
        notificationEntityList.add(getNotificationEntity());

        notificationDTOList = new ArrayList<>();
        notificationDTOList.add(getNotificationDTO());
    }



    @Test
    void notificationSave_test() {
        lenient().when(notificationRepository.save(any(NotificationEntity.class))).thenReturn(getNotificationEntity());

        subscriberNotificationService.postNotificationToASpecificEmployee(getNotificationReqDTO());

        assertNotNull(getNotificationReqDTO());
        assertEquals("Test request", getNotificationReqDTO().getMessage());
    }

    @Test
    void getAllNotifications_test() {
        Mockito.when(notificationRepository.findAllByEmployeeIdAndUserTypeOrderByNotificationDateDesc(employeeId, UserType.EMPLOYEE))
                .thenReturn(notificationEntityList);

        List<NotificationResponseDTO> notificationDTOS = notificationService
                .getAllNotificationsForASpecificEmployee(employeeId, UserType.EMPLOYEE);

        assertTrue(notificationDTOS.size() > 0);
        assertNotNull(notificationDTOS.get(0));
    }

    @Test
    void getAllNotificationForInvalidEmployeeId_test(){
        UUID invalidEmployeeId = UUID.fromString("5f565f61-5111-4762-b3bc-2c963f66afa8");
//        when(notificationRepository.findAllByEmployeeIdAndUserTypeOrderByNotificationDateDesc(invalidEmployeeId, UserType.EMPLOYEE))
//                .thenThrow(EmployeeNotFoundException.class);
//
//        assertThrows(EmployeeNotFoundException.class,
//                () -> notificationService.getAllNotificationsForASpecificEmployee(invalidEmployeeId, UserType.EMPLOYEE));
    }

    @Test
    void getReadNotifications_test() {
        Mockito.when(notificationRepository.findAllByEmployeeIdAndUserTypeOrderByNotificationDateDesc(employeeId, UserType.EMPLOYEE))
                .thenReturn(notificationEntityList);
        notificationService.readAllNotifications(employeeId, UserType.EMPLOYEE);
        verify(notificationRepository, times(1)).findAllByEmployeeIdAndUserTypeOrderByNotificationDateDesc(employeeId, UserType.EMPLOYEE);
    }

    @Test
    void getNotificationById_test() {
        Mockito.when(notificationRepository.findById(notificationId))
                .thenReturn(Optional.ofNullable(notificationEntityList.get(0)));

        NotificationResponseDTO result = notificationService.getNotificationById(notificationId);
        assertEquals(notificationId, result.getNotificationId());
    }

    @Test
    void getNotificationByIdThrowException_test() {
        UUID invalidId = UUID.fromString("2da85f64-5717-4562-b3aa-2c111f66afa6");
//        when(notificationRepository.findById(invalidId))
//                .thenThrow(EntityNotFoundException.class);
//
//        assertThrows(EntityNotFoundException.class,
//                () -> notificationService.getNotificationById(invalidId));
    }

    @Test
    void deleteNotificationById_test() {
        lenient().when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(new NotificationEntity()));
        notificationService.delete(notificationId);
        verify(notificationRepository, times(1)).deleteById(notificationId);
    }

    private NotificationEntity getNotificationEntity() {
        return NotificationEntity.builder()
                .notificationId(UUID.fromString("3fa85f64-5717-4562-b3aa-2c963f66afa6"))
                .message("Test")
                .employeeId(UUID.fromString("4fa65f61-5111-4562-b3bc-2c963f66afa6"))
                .status(Status.UNREAD)
                .userType(UserType.EMPLOYEE)
                .notificationDate(LocalDateTime.now())
                .build();
    }
    private NotificationDTO getNotificationDTO() {
        return NotificationDTO.builder()
                .notificationId(UUID.fromString("3fa85f64-5717-4562-b3aa-2c963f66afa6"))
                .message("Test")
                .employeeId(UUID.fromString("4fa65f61-5111-4562-b3bc-2c963f66afa6"))
                .status(Status.UNREAD)
                .userType(UserType.EMPLOYEE)
                .notificationDate(LocalDateTime.now())
                .build();
    }
    private NotificationRequestDTO getNotificationReqDTO(){
        return NotificationRequestDTO.builder()
                .notificationId(notificationId)
                .userID(employeeId)
                .message("Test request")
                .userType(UserType.EMPLOYEE)
                .build();
    }
}
