package com.example.Sekai_Academy.controller;

import com.example.Sekai_Academy.dto.Response;
import com.example.Sekai_Academy.service.interfac.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
    @RequestMapping("api/v1/events")
public class EventController {

    @Autowired
    private IEventService eventService;

    @PostMapping(value = "/add")
    public ResponseEntity<Response> addEvent(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("content") String content) {
        Response response = eventService.addEvent(file, title, content);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllEvents() {
        Response response = eventService.getAllEvents();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getEventById/{id}")
    public ResponseEntity<Response> getEventById(@PathVariable Long id) {
        Response response = eventService.getEventById(id);
        if (response.getStatusCode() == 200) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response> updateEvent(
            @PathVariable Long id,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "content", required = false) String content) {
        Response response = eventService.updateEvent(id, file, title, content);
        if (response.getStatusCode() == 200) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (response.getStatusCode() == 404) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteEvent(@PathVariable Long id) {
        Response response = eventService.deleteEvent(id);
        if (response.getStatusCode() == 200) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (response.getStatusCode() == 404) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/eventCount")
    public long getTotalEventCount(){
        return eventService.getTotalEventCount();
    }
}