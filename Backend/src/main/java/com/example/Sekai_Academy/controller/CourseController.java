package com.example.Sekai_Academy.controller;


import com.example.Sekai_Academy.dto.Response;
import com.example.Sekai_Academy.service.interfac.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;


@RestController
@RequestMapping("api/v1/courses")
public class CourseController {

    @Autowired
    ICourseService courseService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> addNewCourse(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "price", required = false) BigDecimal price,
            @RequestParam(value = "duration", required = false) String duration
    ) {

        if (file == null || file.isEmpty() || title == null || title.isBlank() || price == null || title.isBlank()) {
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please provide values for all fields");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        Response response = courseService.addCourse(file, title, description, price, duration);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllCourses() {
        Response response = courseService.getAllCourses();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/course-by-id/{courseId}")
    public ResponseEntity<Response> getCourseById(@PathVariable Long courseId) {
        Response response = courseService.getCourseById(courseId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update/{courseId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateCourse(@PathVariable Long courseId,
                                               @RequestParam(value = "file", required = false) MultipartFile file,
                                               @RequestParam(value = "title", required = false) String title,
                                               @RequestParam(value = "description", required = false) String description,
                                               @RequestParam(value = "price", required = false) BigDecimal price,
                                               @RequestParam(value = "duration", required = false) String duration

    ) {
        Response response = courseService.updateCourse(courseId, file, title, description, price, duration);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{courseId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteCourse(@PathVariable Long courseId) {
        Response response = courseService.deleteCourse(courseId);
        return ResponseEntity.status(response.getStatusCode()).body(response);

    }


    @GetMapping("/courseCount")
    public long getTotalCourseCount(){
        return courseService.getTotalCourseCount();
    }

}
