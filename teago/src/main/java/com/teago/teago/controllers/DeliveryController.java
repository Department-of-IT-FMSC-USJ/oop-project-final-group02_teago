package com.teago.teago.controllers;

import com.teago.teago.Services.DeliveryService;
import com.teago.teago.dto.DeliveryRequestDTO;
import com.teago.teago.dto.DeliveryResponseDTO;
import com.teago.teago.dto.DeliveryUpdateDTO;
import com.teago.teago.dto.TeaRateRequestDTO;
import com.teago.teago.dto.TeaRateResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    
    @PostMapping
    public ResponseEntity<?> recordDelivery(
            @Valid @RequestBody DeliveryRequestDTO request) {
        try {
            DeliveryResponseDTO response = deliveryService.recordDelivery(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    
    @GetMapping("/factory/{factoryOwnerId}")
    public ResponseEntity<?> getDeliveriesForFactory(
            @PathVariable Integer factoryOwnerId) {
        try {
            List<DeliveryResponseDTO> list =
                    deliveryService.getDeliveriesForFactory(factoryOwnerId);
            return ResponseEntity.ok(list);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    
    @GetMapping("/factory/{factoryOwnerId}/owner/{landOwnerId}")
    public ResponseEntity<?> getDeliveriesForFactoryAndOwner(
            @PathVariable Integer factoryOwnerId,
            @PathVariable Integer landOwnerId) {
        try {
            List<DeliveryResponseDTO> list =
                    deliveryService.getDeliveriesForFactoryAndOwner(factoryOwnerId, landOwnerId);
            return ResponseEntity.ok(list);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    
    @GetMapping("/owner/{landOwnerId}")
    public ResponseEntity<?> getDeliveriesForLandOwner(
            @PathVariable Integer landOwnerId) {
        try {
            List<DeliveryResponseDTO> list =
                    deliveryService.getDeliveriesForLandOwner(landOwnerId);
            return ResponseEntity.ok(list);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    
    @PutMapping("/{deliveryId}")
    public ResponseEntity<?> updateDeliveryWeight(
            @PathVariable Integer deliveryId,
            @Valid @RequestBody DeliveryUpdateDTO request) {
        try {
            DeliveryResponseDTO response = deliveryService.updateDeliveryWeight(
                    deliveryId, request.getTeaWeight());
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    
    @PostMapping("/rate")
    public ResponseEntity<?> setRate(
            @Valid @RequestBody TeaRateRequestDTO request) {
        try {
            TeaRateResponseDTO response = deliveryService.setRate(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    
    @GetMapping("/rate/{factoryOwnerId}")
    public ResponseEntity<?> getCurrentRate(@PathVariable Integer factoryOwnerId) {
        try {
            TeaRateResponseDTO response = deliveryService.getCurrentRate(factoryOwnerId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    
    @GetMapping("/rate/{factoryOwnerId}/history")
    public ResponseEntity<?> getRateHistory(@PathVariable Integer factoryOwnerId) {
        try {
            List<TeaRateResponseDTO> history = deliveryService.getRateHistory(factoryOwnerId);
            return ResponseEntity.ok(history);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }
}