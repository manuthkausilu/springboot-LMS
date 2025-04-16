package com.example.Sekai_Academy.repo;

import com.example.Sekai_Academy.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PaymentRepo extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderId(String orderId);
}
