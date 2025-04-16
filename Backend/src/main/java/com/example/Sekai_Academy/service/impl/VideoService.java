package com.example.Sekai_Academy.service.impl;


import com.example.Sekai_Academy.dto.Response;
import com.example.Sekai_Academy.dto.VideoDTO;
import com.example.Sekai_Academy.entity.Module;
import com.example.Sekai_Academy.entity.Video;
import com.example.Sekai_Academy.exception.OurException;
import com.example.Sekai_Academy.repo.ModuleRepo;
import com.example.Sekai_Academy.repo.VideoRepo;
import com.example.Sekai_Academy.service.interfac.IVideoService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoService implements IVideoService {

    @Autowired
    VideoRepo videoRepo;

    @Autowired
    ModuleRepo moduleRepo;

    @Autowired
    ModelMapper modelMapper;


    @Override
    public Response addVideo(Long moduleId, String title, String url) {
        Response response = new Response();

        try {
            Module module = moduleRepo.findById(moduleId).orElseThrow(() -> new Exception("Module not found"));

            Video video = new Video();
            video.setTitle(title);
            video.setUrl(url);
            video.setModule(module);

            videoRepo.save(video);

            VideoDTO videoDTO = modelMapper.map(video, VideoDTO.class);

            response.setStatusCode(200);
            response.setMessage("Video added successfully");
            response.setVideo(videoDTO);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error adding video: " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAllVideos() {
        Response response = new Response();
        try {
            List<Video> videoList = videoRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<VideoDTO> videoDTOList = modelMapper.map(videoList, new TypeToken<List<VideoDTO>>() {}.getType());
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setVideoList(videoDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getVideoById(Long id) {
        Response response = new Response();

        try {
            Video video = videoRepo.findById(id).orElseThrow(() -> new Exception("Video not found"));
            VideoDTO videoDTO = modelMapper.map(video, VideoDTO.class);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setVideo(videoDTO);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateVideo(Long id, String title, String url) {
        Response response = new Response();

        try {
            Video video = videoRepo.findById(id)
                    .orElseThrow(() -> new Exception("Video not found"));

            if (title != null && !title.isBlank()) {
                video.setTitle(title);
            }
            if (url != null && !url.isBlank()) {
                video.setUrl(url);
            }

            videoRepo.save(video);

            VideoDTO videoDTO = modelMapper.map(video, VideoDTO.class);

            response.setStatusCode(200);
            response.setMessage("Video updated successfully");
            response.setVideo(videoDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating Video: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteVideo(Long id) {
        Response response = new Response();

        try {
            videoRepo.findById(id).orElseThrow(() -> new OurException("Video Not Found"));
            videoRepo.deleteById(id);
            response.setStatusCode(200);
            response.setMessage("successful");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error delecting a Video " + e.getMessage());
        }
        return response;
    }
}
