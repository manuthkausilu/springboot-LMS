package com.example.Sekai_Academy.service.impl;

import com.example.Sekai_Academy.dto.PaymentDTO;
import com.example.Sekai_Academy.dto.Response;
import com.example.Sekai_Academy.entity.*;
import com.example.Sekai_Academy.exception.OurException;
import com.example.Sekai_Academy.repo.CourseRepo;
import com.example.Sekai_Academy.repo.EnrollmentRepo;
import com.example.Sekai_Academy.repo.PaymentRepo;
import com.example.Sekai_Academy.repo.UserRepo;
import com.example.Sekai_Academy.service.interfac.IPaymentService;
import com.example.Sekai_Academy.utils.PayHereHashUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentService implements IPaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private EnrollmentRepo enrollmentRepo;

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${payhere.merchant.id}")
    private String merchantId;

    @Value("${payhere.merchant.secret}")
    private String merchantSecret;

    // Temporary storage for pending payments (in-memory for simplicity; use a DB in production)
    private final Map<String, PendingPaymentData> pendingPayments = new HashMap<>();

    private static class PendingPaymentData {
        Long userId;
        Long courseId;
        Double amount;
        String currency;
        String orderId;

        PendingPaymentData(Long userId, Long courseId, Double amount, String currency, String orderId) {
            this.userId = userId;
            this.courseId = courseId;
            this.amount = amount;
            this.currency = currency;
            this.orderId = orderId;
        }
    }

    @Override
    public Response initiatePayment(Long userId, Long courseId) {
        Response response = new Response();

        try {
            // Fetch user and course (no transaction needed yet)
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new OurException("User not found"));
            Course course = courseRepo.findById(courseId)
                    .orElseThrow(() -> new OurException("Course not found"));

            if (enrollmentRepo.existsByStudentIdAndCourseId(userId, courseId)) {
                throw new OurException("Student is already enrolled in this course");
            }

            // Generate unique order ID
            String orderId = userId + UUID.randomUUID().toString();
            logger.info("Generated Order ID: {}", orderId);

            // Store temporary data
            Double amount = course.getPrice().doubleValue();
            String currency = "LKR";
            pendingPayments.put(orderId, new PendingPaymentData(userId, courseId, amount, currency, orderId));
            logger.info("Stored pending payment data for Order ID: {}", orderId);

            // Generate PayHere hash
            DecimalFormat df = new DecimalFormat("0.00");
            String amountFormatted = df.format(amount);
            String hashInput = merchantId + orderId + amountFormatted + currency +
                    PayHereHashUtil.getMd5(merchantSecret);
            logger.info("Hash Input: {}", hashInput);
            String hash = PayHereHashUtil.getMd5(hashInput);
            logger.info("Generated Hash: {}", hash);

            // Create a PaymentDTO for the response
            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setAmount(amount);
            paymentDTO.setCurrency(currency);
            paymentDTO.setOrderId(orderId);
            paymentDTO.setHash(hash);
            paymentDTO.setMerchantId(merchantId);

            response.setStatusCode(200);
            response.setMessage("Payment initiated, proceed to PayHere");
            response.setPayment(paymentDTO);

        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
            logger.error("OurException: {}", e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error initiating payment: " + e.getMessage());
            logger.error("Exception in initiatePayment: {}", e.getMessage(), e);
        }
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void handlePaymentNotification(Map<String, String> params) {
        try {
            logger.info("Received PayHere Notification: {}", params);

            String merchantId = params.get("merchant_id");
            String orderId = params.get("order_id");
            String payhereAmount = params.get("payhere_amount");
            String payhereCurrency = params.get("payhere_currency");
            String statusCode = params.get("status_code");
            String md5sig = params.get("md5sig");
            String paymentId = params.get("payment_id");

            // Check if the order exists in pending payments
            PendingPaymentData pendingData = pendingPayments.get(orderId);
            if (pendingData == null) {
                logger.warn("No pending payment data found for Order ID: {}. Possibly already processed.", orderId);
                return; // Skip processing
            }

            // Verify hash
            String localMd5sig = PayHereHashUtil.getMd5(
                    merchantId + orderId + payhereAmount + payhereCurrency +
                            statusCode + PayHereHashUtil.getMd5(merchantSecret)
            );
            logger.info("Generated Local MD5Sig: {}, Received MD5Sig: {}", localMd5sig, md5sig);

            if (localMd5sig.equalsIgnoreCase(md5sig) && "2".equals(statusCode)) {
                // Payment successful - create Enrollment and Payment
                User user = userRepo.findById(pendingData.userId)
                        .orElseThrow(() -> new OurException("User not found"));
                Course course = courseRepo.findById(pendingData.courseId)
                        .orElseThrow(() -> new OurException("Course not found"));

                if (enrollmentRepo.existsByStudentIdAndCourseId(pendingData.userId, pendingData.courseId)) {
                    throw new OurException("Student is already enrolled in this course");
                }

                // Create enrollment
                Enrollment enrollment = new Enrollment();
                enrollment.setStudent(user);
                enrollment.setCourse(course);
                enrollmentRepo.save(enrollment);
                logger.info("Created Enrollment with ID: {}", enrollment.getEnrollmentId());

                // Create payment
                Payment payment = new Payment();
                payment.setAmount(pendingData.amount);
                payment.setCurrency(pendingData.currency);
                payment.setStatus(PaymentStatus.COMPLETED);
                payment.setEnrollment(enrollment);
                payment.setOrderId(orderId);
                payment.setPaymentId(paymentId);
                payment.setHash(localMd5sig);
                paymentRepo.save(payment);
                logger.info("Created Payment with ID: {} and status COMPLETED", payment.getPaymentId());

                //send success mail
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(user.getEmail());
                message.setFrom("manuthkausilu20031018@gmail.com");
                message.setSubject("Sekai Academy");
                message.setText("Course Payment Successfully!");

                mailSender.send(message);

                // Remove from pending payments
                pendingPayments.remove(orderId);
                logger.info("Removed Order ID: {} from pending payments", orderId);
            } else {
                // Payment failed - remove from pending payments and roll back
                logger.warn("Payment failed for Order ID: {}. Status Code: {}, Hash Match: {}",
                        orderId, statusCode, localMd5sig.equalsIgnoreCase(md5sig));
//                pendingPayments.remove(orderId);
//                logger.info("Removed Order ID: {} from pending payments due to failure", orderId);
                // Since no entities were created, nothing to roll back in the database
            }
        } catch (OurException e) {
            logger.error("OurException: {}", e.getMessage());
            throw new OurException("OurException: " + e.getMessage());
        }catch (Exception e) {
            logger.error("Error processing notification: {}", e.getMessage(), e);
            throw new RuntimeException("Error processing notification: " + e.getMessage(), e); // Trigger rollback
        }
    }
}