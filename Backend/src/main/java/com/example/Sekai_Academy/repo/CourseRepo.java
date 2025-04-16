package com.example.Sekai_Academy.repo;

import com.example.Sekai_Academy.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface CourseRepo extends JpaRepository<Course, Long> {

    @Query("SELECT COUNT(c) FROM Course c")
    long countAllCourses();
}
