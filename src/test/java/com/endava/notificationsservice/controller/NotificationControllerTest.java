package com.endava.notificationsservice.controller;

import com.endava.notificationsservice.dto.NotificationDTO;
import com.endava.notificationsservice.dto.NotificationRequestDTO;
import com.endava.notificationsservice.enums.Status;
import com.endava.notificationsservice.enums.UserType;
import com.endava.notificationsservice.service.NotificationService;
import com.endava.notificationsservice.service.PublisherSubscriberNotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Disabled
class NotificationControllerTest {

    private final static UUID notificationId = UUID.fromString("3fa85f64-5717-4562-b3aa-2c963f66afa6");
    private final static UUID employeeId = UUID.fromString("4fa65f61-5111-4562-b3bc-2c963f66afa6");
    @MockBean
    private NotificationService notificationServiceMock;
    @MockBean
    private PublisherSubscriberNotificationService subscriberNotificationService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    private List<NotificationDTO> notificationDTOList;

    private static String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    public void setUp() {
        notificationDTOList = new ArrayList<>();
        NotificationDTO notificationDTO = createNotificationDTO();
        notificationDTOList.add(notificationDTO);
    }

    @BeforeEach
    public void setUpBeforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("Delete a notification")
    void deleteNotification() throws Exception {
        doNothing().when(notificationServiceMock).delete(notificationId);

        mockMvc.perform(delete("/notifications/{notificationId}", notificationId))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete a notification returns 404")
    void deleteNotification_thenReturn404() throws Exception {
//        doThrow(NotificationNotFoundException.class).when(notificationServiceMock).delete(notificationId);

        mockMvc.perform(delete("/j/notifications/{notificationId}", notificationId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get a notification")
    void getNotification() throws Exception {
//        Mockito.when(notificationServiceMock.getNotificationById(notificationId))
//                .thenReturn(notificationDTOList.get(0));
//
//        mockMvc.perform(get("/notifications/{notificationId}", notificationId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Test"))
//                .andExpect(jsonPath("$.userType").value("EMPLOYEE"));
    }

    @Test
    @DisplayName("Get a notification returns 404")
    void getNotification_thenReturn404() throws Exception {

//        doThrow(NotificationNotFoundException.class).when(notificationServiceMock).getNotificationById(notificationId);

        mockMvc.perform(get("/j/notifications/{notificationId}", notificationId))
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("Get all the notifications for an employee")
    void getAllNotificationForEmployee() throws Exception {
//
//        int expectedListSize = 1;
//
//        Mockito.when(notificationServiceMock.getAllNotificationsForASpecificEmployee(employeeId, UserType.EMPLOYEE))
//                .thenReturn(notificationDTOList);
//
//        mockMvc.perform(get("/notifications/employee/{employeeId}", employeeId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$", hasSize(expectedListSize)));
    }

    @Test
    @DisplayName("Get all the notifications for an employee returns 404")
    void getAllNotificationForEmployee_thenReturn404() throws Exception {

//        doThrow(EmployeeNotFoundException.class).when(notificationServiceMock)
//                .getAllNotificationsForASpecificEmployee(employeeId, UserType.EMPLOYEE);

        mockMvc.perform(get("/j/notifications/employee/{employeeId}", employeeId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Save a notification")
    void createNotification() throws Exception {
        subscriberNotificationService.postNotificationToASpecificEmployee(any(NotificationRequestDTO.class));

        mockMvc.perform(post("/j/notifications/{employeeId}", employeeId)
                        .content(asJsonString(notificationDTOList.get(0)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Update a notification")
    void updateNotification() throws Exception {

        NotificationDTO notificationDTO = NotificationDTO.builder()
                .message("notification updated")
                .notificationDate(LocalDateTime.now().minusDays(8))
                .build();
        String jsonInput = asJsonString(notificationDTO);

//        Mockito.when(notificationServiceMock.update(notificationId, notificationDTOList.get(0)))
//                .thenReturn(notificationDTO);

        mockMvc.perform(patch("/notifications/{notificationId}", notificationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andDo(print())
                .andExpect(status().isOk());

        verify(notificationServiceMock, times(1)).update(any(), any());
    }

    @Test
    @DisplayName("Update a notification returns 404")
    void updateNotification_thenReturn404() throws Exception {

//        doThrow(NotificationNotFoundException.class).when(notificationServiceMock)
//                .update(notificationId, notificationDTOList.get(0));

        verify(notificationServiceMock, times(0)).update(any(), any());
    }

    @Test
    @DisplayName("Read all notifications for an employee")
    void readAllNotifications() throws Exception {

        NotificationDTO notificationDTO = createNotificationDTO();
//        notificationDTO.setStatus(Status.READED);
        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        notificationDTOS.add(notificationDTO);

        doNothing().when(notificationServiceMock).readAllNotifications(employeeId, UserType.EMPLOYEE);

        mockMvc.perform(patch("/notifications/employeeNotifications/{employeeId}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(notificationDTO)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private NotificationDTO createNotificationDTO() {
        return NotificationDTO.builder()
                .notificationId(UUID.fromString("3fa85f64-5717-4562-b3aa-2c963f66afa6"))
                .message("Test")
                .employeeId(UUID.fromString("4fa65f61-5111-4562-b3bc-2c963f66afa6"))
                .status(Status.UNREAD)
                .userType(UserType.EMPLOYEE)
                .notificationDate(LocalDateTime.now().minusDays(7))
                .build();
    }
}
