package com.example.Sekai_Academy.service.impl;

import com.example.Sekai_Academy.dto.ModuleDTO;
import com.example.Sekai_Academy.dto.Response;
import com.example.Sekai_Academy.entity.Course;
import com.example.Sekai_Academy.exception.OurException;
import com.example.Sekai_Academy.repo.CourseRepo;
import com.example.Sekai_Academy.repo.ModuleRepo;
import com.example.Sekai_Academy.entity.Module;
import com.example.Sekai_Academy.service.interfac.IModuleService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleService implements IModuleService {

    @Autowired
    private ModuleRepo moduleRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CourseRepo courseRepo;

    @Override
    public Response addModule(Long id, String title, String content) {
        Response response = new Response();

        try {
            Course course = courseRepo.findById(id).orElseThrow(() -> new Exception("Course not found"));

            Module module = new Module();
            module.setTitle(title);
            module.setContent(content);
            module.setCourse(course);

            moduleRepo.save(module);

            ModuleDTO moduleDTO = modelMapper.map(module, ModuleDTO.class);

            response.setStatusCode(200);
            response.setMessage("Module added successfully");
            response.setModule(moduleDTO);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error adding module: " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAllModules() {
        Response response = new Response();
        try {
            List<Module> moduleList = moduleRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<ModuleDTO> moduleDTOList = modelMapper.map(moduleList, new TypeToken<List<ModuleDTO>>() {}.getType());
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setModuleList(moduleDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getModuleById(Long id) {
        Response response = new Response();

        try {
            Module module = moduleRepo.findById(id).orElseThrow(() -> new OurException("Module Not Found"));
            ModuleDTO moduleDTO = modelMapper.map(module, ModuleDTO.class);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setModule(moduleDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    @Override
    public Response updateModule(Long id, String title, String content) {
        Response response = new Response();

        try {
            Module module = moduleRepo.findById(id)
                    .orElseThrow(() -> new OurException("Module Not Found"));

            if (title != null && !title.isBlank()) {
                module.setTitle(title);
            }
            if (content != null && !content.isBlank()) {
                module.setContent(content);
            }

            moduleRepo.save(module);

            ModuleDTO moduleDTO = modelMapper.map(module, ModuleDTO.class);

            response.setStatusCode(200);
            response.setMessage("Module updated successfully");
            response.setModule(moduleDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating module: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteModule(Long id) {
        Response response = new Response();

        try {
            moduleRepo.findById(id).orElseThrow(() -> new OurException("Module Not Found"));
            moduleRepo.deleteById(id);
            response.setStatusCode(200);
            response.setMessage("successful");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error delecting a Module " + e.getMessage());
        }
        return response;
    }
}
