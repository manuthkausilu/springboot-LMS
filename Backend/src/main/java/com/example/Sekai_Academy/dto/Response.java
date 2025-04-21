package com.example.Sekai_Academy.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private int statusCode;
    private String message;

    private String token;
    private String role;
    private String expirationTime;

    private UserDTO user;
    private List<UserDTO> userList;

    private CourseDTO course;
    private List<CourseDTO> courseList;

    private ModuleDTO module;
    private List<ModuleDTO> moduleList;

    private VideoDTO video;
    private List<VideoDTO> videoList;

    private PaymentDTO payment;
    private List<PaymentDTO> paymentList;

    private EnrollmentDTO enrollment;
    private List<EnrollmentDTO> enrollmentList;

    private EventDTO eventDTO;
    private List<EventDTO> eventDTOList;

}
