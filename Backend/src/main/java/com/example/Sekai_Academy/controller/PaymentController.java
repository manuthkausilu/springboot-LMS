package com.example.Sekai_Academy.controller;

import com.example.Sekai_Academy.dto.Response;
import com.example.Sekai_Academy.service.interfac.IPaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    @Autowired
    private IPaymentService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<Response> initiatePayment(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "courseId") Long courseId
    ) {
        Response response = paymentService.initiatePayment(userId, courseId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/notify")
    public ResponseEntity<String> handleNotify(HttpServletRequest request) {
        try {
            Map<String, String> params = new HashMap<>();
            request.getParameterMap().forEach((key, value) -> params.put(key, value[0]));
            paymentService.handlePaymentNotification(params);
            System.out.println("Successfully processed payment notification");
            return ResponseEntity.ok("Notification processed");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing notification: " + e.getMessage());
        }
    }

    @GetMapping("/return")
    public RedirectView handleReturn() {
        System.out.println("return");
        return new RedirectView("http://localhost:63342/AAD%20Coursework/Frontend/pages/courses.html");
    }

    @GetMapping("/cancel")
    public RedirectView handleCancel() {
        System.out.println("cancel");
        return new RedirectView("http://localhost:63342/AAD%20Coursework/Frontend/pages/courses.html");
    }
}