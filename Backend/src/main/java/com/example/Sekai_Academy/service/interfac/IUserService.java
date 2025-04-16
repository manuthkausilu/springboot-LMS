package com.example.Sekai_Academy.service.interfac;


import com.example.Sekai_Academy.dto.LoginRequest;
import com.example.Sekai_Academy.dto.Response;
import com.example.Sekai_Academy.entity.User;

import java.util.Optional;

public interface IUserService {
    Response register(User user);

    Response login(LoginRequest loginRequest);

    Response getAllUsers();

    Response deleteUser(String userId);

    Response getUserById(String userId);

    Response getMyInfo(String email);

    Response update(User user);

    long getTotalUserCount();


}
