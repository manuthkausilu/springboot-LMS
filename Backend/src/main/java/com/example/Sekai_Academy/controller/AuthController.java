package com.example.Sekai_Academy.controller;



import com.example.Sekai_Academy.dto.LoginRequest;
import com.example.Sekai_Academy.dto.Response;
import com.example.Sekai_Academy.entity.User;
import com.example.Sekai_Academy.repo.UserRepo;
import com.example.Sekai_Academy.service.interfac.IOtpService;
import com.example.Sekai_Academy.service.interfac.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IOtpService otpService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody User user) {
        Response response = userService.register(user);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest) {
        Response response = userService.login(loginRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Forgot Password - Generate and Send OTP
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {

        Optional<User> userOptional = userRepo.findByEmail(email);
        User user = modelMapper.map(userOptional.get(), User.class);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        String otp = otpService.generateOtp();
        otpService.saveOtp(email, otp);
        otpService.sendOtpEmail(email, otp);
        return ResponseEntity.ok("OTP sent to your email");
    }

    // Verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        if (otpService.verifyOtp(email, otp)) {
            return ResponseEntity.ok("OTP verified successfully");
        }
        return ResponseEntity.badRequest().body("Invalid or expired OTP");
    }

    // Reset Password
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email,
                                                @RequestParam String otp,
                                                @RequestParam String newPassword) {
        if (!otpService.verifyOtp(email, otp)) {
            return ResponseEntity.badRequest().body("Invalid or expired OTP");
        }

        Optional<User> userOptional = userRepo.findByEmail(email);
        User user = modelMapper.map(userOptional.get(), User.class);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        user.setPassword(passwordEncoder.encode(newPassword)); // Hash the new password
        userRepo.save(user);
        otpService.clearOtp(email); // Clear OTP after successful reset
        return ResponseEntity.ok("Password reset successfully");
    }
}
