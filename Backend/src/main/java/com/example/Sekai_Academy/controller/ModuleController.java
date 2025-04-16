package com.example.Sekai_Academy.controller;

import com.example.Sekai_Academy.dto.Response;
import com.example.Sekai_Academy.service.interfac.IModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/modules")
public class ModuleController {

    @Autowired
    private IModuleService moduleService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> addNewModule(
            @RequestParam(value = "courseId", required = false) Long courseId,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "content", required = false) String content
    ) {
        Response response = moduleService.addModule(courseId, title, content);

        if (response.getStatusCode() == 200) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllModules() {
        Response response = moduleService.getAllModules();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/module-by-id/{moduleId}")
    public ResponseEntity<Response> getModuleById(@PathVariable Long moduleId) {
        Response response = moduleService.getModuleById(moduleId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update/{moduleId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateModule(@PathVariable Long moduleId,
                                                 @RequestParam(value = "title", required = false) String title,
                                                 @RequestParam(value = "content", required = false) String content
    ) {
        Response response = moduleService.updateModule(moduleId, title, content);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{moduleId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteModule(@PathVariable Long moduleId) {
        Response response = moduleService.deleteModule(moduleId);
        return ResponseEntity.status(response.getStatusCode()).body(response);

    }

}
