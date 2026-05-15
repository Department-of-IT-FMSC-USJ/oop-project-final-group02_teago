package com.teago.teago.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teago.teago.Services.PaymentService;
import com.teago.teago.dto.PaymentDTO;
import com.teago.teago.dto.PaymentRequest;
import com.teago.teago.dto.PaymentSummaryDTO;
import com.teago.teago.dto.FactoryPaymentSummaryDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<?> createPayment(@Valid @RequestBody PaymentRequest request) {
        try {
            PaymentDTO response = paymentService.createPayment(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/delivery/{deliveryId}")
    public ResponseEntity<?> getPaymentByDelivery(@PathVariable Integer deliveryId) {
        try {
            PaymentDTO response = paymentService.getPaymentByDelivery(deliveryId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    @GetMapping("/owner/{landOwnerId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByLandOwner(@PathVariable Integer landOwnerId) {
        return ResponseEntity.ok(paymentService.getPaymentsByLandOwner(landOwnerId));
    }

    @GetMapping("/factory/{factoryOwnerId}")
    public ResponseEntity<?> getPaymentSummariesByFactory(@PathVariable Integer factoryOwnerId) {
        try {
            List<PaymentSummaryDTO> summaries = paymentService.getPaymentSummariesByFactory(factoryOwnerId);
            return ResponseEntity.ok(summaries);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    @GetMapping("/factory/{factoryOwnerId}/summary")
    public ResponseEntity<?> getFactoryPaymentSummary(@PathVariable Integer factoryOwnerId) {
        try {
            FactoryPaymentSummaryDTO summary = paymentService.getFactoryPaymentSummary(factoryOwnerId);
            return ResponseEntity.ok(summary);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }
}
