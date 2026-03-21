package com.teago.teago.controllers;

import com.teago.teago.Services.FactoryRegistrationService;
import com.teago.teago.dto.FactoryRegistrationRequestDTO;
import com.teago.teago.dto.FactoryRegistrationResponseDTO;
import com.teago.teago.dto.FactoryRegistrationStatusUpdateDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST endpoints for factory registration management.
 *
 * GET  /api/factories                          → Land Owner browses all factories
 * GET  /api/factories/registrations/{loId}     → Land Owner views their registrations
 * POST /api/factories/register                 → Land Owner registers with a factory
 * GET  /api/factories/pending/{factoryId}      → Factory Owner views pending requests
 * PUT  /api/factories/registrations/{id}/status → Factory Owner approves/rejects
 */
@RestController
@RequestMapping("/api/factories")
public class FactoryRegistrationController {

    private final FactoryRegistrationService service;

    public FactoryRegistrationController(FactoryRegistrationService service) {
        this.service = service;
    }

    // Land Owner — browse all available factories
    @GetMapping
    public ResponseEntity<List<FactoryRegistrationResponseDTO>> getAllFactories() {
        return ResponseEntity.ok(service.getAllFactories());
    }

    // Land Owner — view all their own registrations
    @GetMapping("/registrations/{landOwnerId}")
    public ResponseEntity<?> getMyFactories(@PathVariable Integer landOwnerId) {
        try {
            return ResponseEntity.ok(service.getMyFactories(landOwnerId));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    // Land Owner — submit a registration request to a factory
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

    // Factory Owner — view pending registration requests
    @GetMapping("/pending/{factoryOwnerId}")
    public ResponseEntity<?> getPendingRegistrations(
            @PathVariable Integer factoryOwnerId) {
        try {
            return ResponseEntity.ok(service.getPendingRegistrations(factoryOwnerId));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    // Factory Owner — approve or reject a registration
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
}