package com.example.Sekai_Academy.controller;

import com.example.Sekai_Academy.dto.Response;
import com.example.Sekai_Academy.service.interfac.IEnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/enrollments")
public class EnrollmentController {

    @Autowired
    private IEnrollmentService enrollmentService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllEnrollments() {
        Response response = enrollmentService.getAllEnrollments();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/getAllEnrollmentsByUserId/{userId}")
    public ResponseEntity<Response> getAllEnrollmentsByUserId(@PathVariable Long userId) {
        Response response = enrollmentService.getAllEnrollmentsByUserId(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/existsByStudentIdAndCourseId")
    public boolean existsByStudentIdAndCourseId(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "courseId") Long courseId
    ) {
        return enrollmentService.existsByStudentIdAndCourseId(userId, courseId);
    }
}
