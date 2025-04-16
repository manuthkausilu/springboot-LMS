package com.example.Sekai_Academy.controller;

import com.example.Sekai_Academy.dto.Response;
import com.example.Sekai_Academy.service.interfac.IVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/videos")
public class VideoController {

    @Autowired
    private IVideoService videoService;


    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> addNewVideo(
            @RequestParam(value = "moduleId", required = false) Long moduleId,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "url", required = false) String url
    ) {
        Response response = videoService.addVideo(moduleId,title,url);

        if (response.getStatusCode() == 200) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllVideos() {
        Response response = videoService.getAllVideos();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/video-by-id/{videoId}")
    public ResponseEntity<Response> getVideoById(@PathVariable Long videoId) {
        Response response = videoService.getVideoById(videoId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update/{videoId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateVideo(@PathVariable Long videoId,
                                                 @RequestParam(value = "title", required = false) String title,
                                                 @RequestParam(value = "url", required = false) String url
    ) {
        Response response = videoService.updateVideo(videoId,title,url);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{videoId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteVideo(@PathVariable Long videoId) {
        Response response = videoService.deleteVideo(videoId);
        return ResponseEntity.status(response.getStatusCode()).body(response);

    }



}
