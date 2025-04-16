package com.example.Sekai_Academy.service.interfac;

import com.example.Sekai_Academy.dto.Response;

import java.util.Map;

public interface IPaymentService {
    Response initiatePayment(Long userId, Long courseId);
    void handlePaymentNotification(Map<String, String> params);
}