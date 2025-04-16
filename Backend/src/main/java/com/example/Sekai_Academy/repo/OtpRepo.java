package com.example.Sekai_Academy.repo;

import com.example.Sekai_Academy.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OtpRepo extends JpaRepository<Otp, Long> {
    List<Otp> findAllByEmail(String email); // Returns all OTPs for an email
}
