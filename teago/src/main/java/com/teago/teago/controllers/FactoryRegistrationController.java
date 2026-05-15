package com.teago.teago.controllers;

import com.teago.teago.Services.FactoryRegistrationService;
import com.teago.teago.dto.FactoryRegistrationRequestDTO;
import com.teago.teago.dto.FactoryRegistrationResponseDTO;
import com.teago.teago.dto.FactoryRegistrationStatusUpdateDTO;
import com.teago.teago.dto.LandOwnerDetailsDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/factories")
public class FactoryRegistrationController {

    private final FactoryRegistrationService service;

    public FactoryRegistrationController(FactoryRegistrationService service) {
        this.service = service;
    }

    
    @GetMapping
    public ResponseEntity<List<FactoryRegistrationResponseDTO>> getAllFactories() {
        return ResponseEntity.ok(service.getAllFactories());
    }

    
    @GetMapping("/registrations/{landOwnerId}")
    public ResponseEntity<?> getMyFactories(@PathVariable Integer landOwnerId) {
        try {
            return ResponseEntity.ok(service.getMyFactories(landOwnerId));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody FactoryRegistrationRequestDTO request) {
        try {
            FactoryRegistrationResponseDTO response = service.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    
    @GetMapping("/pending/{factoryOwnerId}")
    public ResponseEntity<?> getPendingRegistrations(
            @PathVariable Integer factoryOwnerId) {
        try {
            return ResponseEntity.ok(service.getPendingRegistrations(factoryOwnerId));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

   
    @GetMapping("/owners/{factoryOwnerId}")
    public ResponseEntity<?> getFactoryRegistrations(@PathVariable Integer factoryOwnerId) {
        try {
            return ResponseEntity.ok(service.getFactoryRegistrations(factoryOwnerId));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    
    @PutMapping("/registrations/{registrationId}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Integer registrationId,
            @Valid @RequestBody FactoryRegistrationStatusUpdateDTO request) {
        try {
            return ResponseEntity.ok(service.updateStatus(registrationId, request));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    @GetMapping("/active-owners/{factoryOwnerId}")
    public ResponseEntity<?> getActiveLandOwners(@PathVariable Integer factoryOwnerId) {
        try {
            return ResponseEntity.ok(service.getActiveLandOwners(factoryOwnerId));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    
    @GetMapping("/{factoryOwnerId}/owner-details/{landOwnerId}")
    public ResponseEntity<?> getLandOwnerDetails(
            @PathVariable Integer factoryOwnerId,
            @PathVariable Integer landOwnerId) {
        try {
            LandOwnerDetailsDTO details = service.getLandOwnerDetails(factoryOwnerId, landOwnerId);
            return ResponseEntity.ok(details);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }
}