package com.example.Sekai_Academy.service.interfac;


import com.example.Sekai_Academy.dto.Response;
import org.springframework.web.multipart.MultipartFile;

public interface IEventService {

    Response addEvent(MultipartFile file, String title, String content);
    Response getAllEvents();
    Response getEventById(Long id);
    Response updateEvent(Long id,MultipartFile file,String title,String content);
    Response deleteEvent(Long id);
    public long getTotalEventCount();

}
