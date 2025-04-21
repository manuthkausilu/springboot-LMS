package com.example.Sekai_Academy.service.impl;

import com.example.Sekai_Academy.dto.EventDTO;
import com.example.Sekai_Academy.dto.Response;
import com.example.Sekai_Academy.entity.Event;
import com.example.Sekai_Academy.exception.OurException;
import com.example.Sekai_Academy.repo.EventRepo;
import com.example.Sekai_Academy.service.interfac.IEventService;
import com.example.Sekai_Academy.utils.FileUploadUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class EventService implements IEventService {

    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Response addEvent(MultipartFile file, String title, String content) {
        Response response = new Response();
        String imgUrl = "";
        String uploadDir = "ImagesFolder";

        try {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            System.out.println(fileName);
            imgUrl = FileUploadUtil.saveFile(uploadDir, fileName, file);


            Event event = new Event();
            event.setTitle(title);
            event.setContent(content);
            event.setImgUrl(imgUrl);

            Event savedEvent = eventRepo.save(event);
            EventDTO eventDTO = modelMapper.map(savedEvent, EventDTO.class);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setEventDTO(eventDTO);

        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (IOException e) {
            response.setStatusCode(500);
            response.setMessage("Error uploading file: " + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error adding a event: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllEvents() {
        Response response = new Response();

        try {
            List<Event> eventList = eventRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<EventDTO> eventDTOList = modelMapper.map(eventList, new TypeToken<List<EventDTO>>() {}.getType());
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setEventDTOList(eventDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error retrieving event: " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getEventById(Long id) {
        Response response = new Response();

        try {
            Event blog = eventRepo.findById(id).orElseThrow(() -> new OurException("Event Not Found"));
            EventDTO eventDTO = modelMapper.map(blog, EventDTO.class);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setEventDTO(eventDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error get EventDetails " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateEvent(Long id, MultipartFile file, String title, String content) {
        Response response = new Response();
        String uploadDir = "ImagesFolder";

        try {
            Event event = eventRepo.findById(id).orElseThrow(() -> new OurException("Event Not Found"));

            if (title != null) event.setTitle(title);
            if (content != null) event.setContent(content);

            if (file != null && !file.isEmpty()) {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                String imageUrl = FileUploadUtil.saveFile(uploadDir, fileName, file);
                event.setImgUrl(imageUrl);
            }

            Event updatedEvent = eventRepo.save(event);
            EventDTO eventDTO = modelMapper.map(updatedEvent, EventDTO.class);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setEventDTO(eventDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating a Event " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteEvent(Long id) {
        Response response = new Response();

        try {
            eventRepo.findById(id).orElseThrow(() -> new OurException("Event Not Found"));
            eventRepo.deleteById(id);
            response.setStatusCode(200);
            response.setMessage("successful");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error delecting a Event " + e.getMessage());
        }
        return response;
    }

    @Override
    public long getTotalEventCount() {
        return eventRepo.countAllEvents();
    }
}