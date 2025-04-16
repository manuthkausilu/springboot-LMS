package com.example.Sekai_Academy.controller;

import com.example.Sekai_Academy.dto.MaillDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/mail")
public class MailController {

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/send")
    public ResponseEntity<String> sendMail(@RequestBody MaillDetailsDTO maillDetailsDTO) {
        try {
            // Validate input
            if (maillDetailsDTO.getName() == null || maillDetailsDTO.getEmail() == null ||
                    maillDetailsDTO.getSubject() == null || maillDetailsDTO.getMessage() == null) {
                return ResponseEntity.badRequest().body("All fields are required");
            }

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("rayff60@gmail.com");
            message.setFrom("manuthkausilu20031018@gmail.com");
            message.setSubject(maillDetailsDTO.getSubject());
            message.setText("Email: " + maillDetailsDTO.getEmail()
                    + "\nName: " + maillDetailsDTO.getName()
                    + "\nMessage: " + maillDetailsDTO.getMessage()
            );

            mailSender.send(message);
            return ResponseEntity.ok("Mail sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error sending email: " + e.getMessage());
        }
    }


}
