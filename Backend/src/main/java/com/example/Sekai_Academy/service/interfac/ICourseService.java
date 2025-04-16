package com.example.Sekai_Academy.service.interfac;

import com.example.Sekai_Academy.dto.Response;
import com.example.Sekai_Academy.entity.Course;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Optional;

public interface ICourseService {

    Response addCourse(MultipartFile file, String title, String description, BigDecimal price, String duration);
    Response getAllCourses();
    Response getCourseById(Long id);
    Response updateCourse(Long id,MultipartFile file, String name, String description, BigDecimal price, String duration);
    Response deleteCourse(Long id);
    long getTotalCourseCount();
}
