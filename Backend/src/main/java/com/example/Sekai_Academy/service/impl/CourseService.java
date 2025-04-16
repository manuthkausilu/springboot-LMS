package com.example.Sekai_Academy.service.impl;

import com.example.Sekai_Academy.dto.CourseDTO;
import com.example.Sekai_Academy.dto.Response;
import com.example.Sekai_Academy.entity.Course;
import com.example.Sekai_Academy.exception.OurException;
import com.example.Sekai_Academy.repo.CourseRepo;
import com.example.Sekai_Academy.service.interfac.ICourseService;
import com.example.Sekai_Academy.utils.FileUploadUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class CourseService implements ICourseService {

    @Autowired
    CourseRepo courseRepo;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public Response addCourse(MultipartFile file, String title, String description, BigDecimal price, String duration) {
        Response response = new Response();
        String imgUrl = "";
        String uploadDir = "ImagesFolder";

        try {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            System.out.println(fileName);
            imgUrl = FileUploadUtil.saveFile(uploadDir, fileName, file);

            CourseDTO courseDTO = new CourseDTO();
            courseDTO.setTitle(title);
            courseDTO.setDescription(description);
            courseDTO.setImgUrl(imgUrl);
            courseDTO.setPrice(price);
            courseDTO.setDuration(duration);
            courseRepo.save(modelMapper.map(courseDTO, Course.class));
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setCourse(courseDTO);
        } catch (IOException e) {
            response.setStatusCode(500);
            response.setMessage("Error uploading file: " + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error adding a course: " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAllCourses() {
        Response response = new Response();
        try {
            List<Course> courseList = courseRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<CourseDTO> courseDTOList = modelMapper.map(courseList, new TypeToken<List<CourseDTO>>() {}.getType());
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setCourseList(courseDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getCourseById(Long id) {
        Response response = new Response();

        try {
            Course course = courseRepo.findById(id).orElseThrow(() -> new OurException("Course Not Found"));
            CourseDTO courseDTO = modelMapper.map(course, CourseDTO.class);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setCourse(courseDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error get courseDetails " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateCourse(Long id, MultipartFile file, String title, String description, BigDecimal price, String duration) {
        Response response = new Response();
        String uploadDir = "ImagesFolder";

        try {
            String imageUrl = null;
            if (file != null && !file.isEmpty()) {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                imageUrl = FileUploadUtil.saveFile(uploadDir, fileName, file);
            }

            Course course = courseRepo.findById(id).orElseThrow(() -> new OurException("Course Not Found"));
            if (title != null) course.setTitle(title);
            if (price != null) course.setPrice(price);
            if (description != null) course.setDescription(description);
            if (duration != null) course.setDuration(duration);
            if (imageUrl != null) course.setImgUrl(imageUrl);

            Course updatedCourse = courseRepo.save(course);
            CourseDTO courseDTO = modelMapper.map(updatedCourse, CourseDTO.class);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setCourse(courseDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating a course " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteCourse(Long id) {
        Response response = new Response();

        try {
            courseRepo.findById(id).orElseThrow(() -> new OurException("Course Not Found"));
            courseRepo.deleteById(id);
            response.setStatusCode(200);
            response.setMessage("successful");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error delecting a course " + e.getMessage());
        }
        return response;
    }

    @Override
    public long getTotalCourseCount() {
        return courseRepo.countAllCourses();
    }


}