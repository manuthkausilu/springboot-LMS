package com.example.Sekai_Academy.service.impl;

import com.example.Sekai_Academy.dto.EnrollmentDTO;
import com.example.Sekai_Academy.dto.Response;
import com.example.Sekai_Academy.entity.Course;
import com.example.Sekai_Academy.entity.Enrollment;
import com.example.Sekai_Academy.entity.User;
import com.example.Sekai_Academy.exception.OurException;
import com.example.Sekai_Academy.repo.CourseRepo;
import com.example.Sekai_Academy.repo.EnrollmentRepo;
import com.example.Sekai_Academy.repo.UserRepo;
import com.example.Sekai_Academy.service.interfac.IEnrollmentService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService implements IEnrollmentService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EnrollmentRepo enrollmentRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CourseRepo courseRepo;


    @Override
    public Response getAllEnrollments() {
        Response response = new Response();

        try {
            List<Enrollment> enrollments = enrollmentRepo.findAll(Sort.by(Sort.Direction.DESC, "enrollmentId"));
            List<EnrollmentDTO> enrollmentDTOList = modelMapper.map(enrollments, new TypeToken<List<EnrollmentDTO>>() {}.getType());

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setEnrollmentList(enrollmentDTOList);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllEnrollmentsByUserId(Long userId) {
        Response response = new Response();
        try {
            User user = userRepo.findById(userId).orElseThrow(() -> new OurException("User Not Found"));

            List<Enrollment> enrollments = user.getEnrollments();

            List<EnrollmentDTO> enrollmentDTOList = modelMapper.map(enrollments, new TypeToken<List<EnrollmentDTO>>() {}.getType());
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setEnrollmentList(enrollmentDTOList);

        }catch (OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error"+e.getMessage());
        }
        return response;
    }

    @Override
    public boolean existsByStudentIdAndCourseId(Long studentId, Long courseId) {

        boolean existsByStudentIdAndCourseId;

            if (enrollmentRepo.existsByStudentIdAndCourseId(studentId, courseId)) {
                existsByStudentIdAndCourseId = true;
            }else {
                existsByStudentIdAndCourseId = false;
            }
        return existsByStudentIdAndCourseId;

    }


}
