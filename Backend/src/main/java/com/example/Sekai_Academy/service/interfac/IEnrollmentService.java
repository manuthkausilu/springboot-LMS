package com.example.Sekai_Academy.service.interfac;


import com.example.Sekai_Academy.dto.Response;

public interface IEnrollmentService {

    Response getAllEnrollments();
    Response getAllEnrollmentsByUserId(Long userId);
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

}
