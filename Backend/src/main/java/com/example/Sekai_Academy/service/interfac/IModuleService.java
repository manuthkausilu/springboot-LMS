package com.example.Sekai_Academy.service.interfac;

import com.example.Sekai_Academy.dto.Response;

public interface IModuleService {

    Response addModule(Long id,String title, String content);
    Response getAllModules();
    Response getModuleById(Long id);
    Response updateModule(Long id,String title, String content);
    Response deleteModule(Long id);
}
