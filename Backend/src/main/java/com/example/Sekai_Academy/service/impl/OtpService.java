package com.example.Sekai_Academy.service.impl;

import com.example.Sekai_Academy.entity.Otp;
import com.example.Sekai_Academy.repo.OtpRepo;
import com.example.Sekai_Academy.repo.UserRepo;
import com.example.Sekai_Academy.service.interfac.IOtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class OtpService implements IOtpService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OtpRepo otpRepo;

    @Autowired
    private JavaMailSender mailSender;

    // Generate a 6-digit OTP
    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Range: 100000 - 999999
        return String.valueOf(otp);
    }

    // Send OTP to email
    public void sendOtpEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom("manuthkausilu20031018@gmail.com");
        message.setSubject("Password Reset OTP");
        message.setText("Your OTP for password reset is: " + otp + ". It is valid for 10 minutes.");
        mailSender.send(message);
    }

    // Save OTP to database
    @Transactional
    public void saveOtp(String email, String otp) {

        // Fetch all OTPs for this email (could be multiple)
        List<Otp> existingOtps = otpRepo.findAllByEmail(email); // Change to return List
        if (!existingOtps.isEmpty()) {
            // Delete all existing OTPs
            otpRepo.deleteAll(existingOtps);
        }

        Otp otpEntity = new Otp();
        otpEntity.setEmail(email);
        otpEntity.setOtp(otp);
        otpEntity.setCreatedAt(LocalDateTime.now());
        otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(10)); // OTP expires in 10 minutes
        otpRepo.save(otpEntity);
    }

    // Verify OTP
    @Transactional
    public boolean verifyOtp(String email, String otp) {
        List<Otp> otpEntities = otpRepo.findAllByEmail(email);
        if (otpEntities.isEmpty()) {
            return false;
        }
        // Sort by createdAt descending and take the latest
        Otp latestOtp = otpEntities.stream()
                .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                .findFirst()
                .orElse(null);
        if (latestOtp == null || !latestOtp.getOtp().equals(otp)) {
            return false;
        }
        return latestOtp.getExpiresAt().isAfter(LocalDateTime.now());
    }

    // Clear OTP after use
    public void clearOtp(String email) {
        List<Otp> otpEntities = otpRepo.findAllByEmail(email);
        if (!otpEntities.isEmpty()) {
            otpRepo.deleteAll(otpEntities);
        }
    }

}
