package com.example.Sekai_Academy.service.interfac;

import com.example.Sekai_Academy.dto.Response;

public interface IVideoService {

    Response addVideo(Long moduleId, String title, String url);
    Response getAllVideos();
    Response getVideoById(Long id);
    Response updateVideo(Long id, String title, String url);
    Response deleteVideo(Long id);
}
