package com.example.Sekai_Academy.dto;

import com.example.Sekai_Academy.entity.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentDTO {

    private Long id;
    private double amount;
    private String currency;
    private PaymentStatus status;
    private String orderId;
    private String paymentId;
    private String hash;
    private String merchantId; // For PayHere form
    private Long enrollmentId;
}
