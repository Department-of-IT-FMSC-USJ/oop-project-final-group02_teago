package com.teago.teago.Controllers;

import com.teago.teago.Services.FertilizerService;
import com.teago.teago.dto.FertilizerRequestDTO;
import com.teago.teago.dto.FertilizerResponseDTO;
import com.teago.teago.dto.FertilizerStatusUpdateDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/fertilizer")
public class FertilizerController {

    private final FertilizerService fertilizerService;

    public FertilizerController(FertilizerService fertilizerService) {
        this.fertilizerService = fertilizerService;
    }

   
    @PostMapping
    public ResponseEntity<?> requestFertilizer(
            @Valid @RequestBody FertilizerRequestDTO request) {
        try {
            FertilizerResponseDTO response = fertilizerService.requestFertilizer(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    
    @GetMapping
    public ResponseEntity<List<FertilizerResponseDTO>> getAllRequests() {
        return ResponseEntity.ok(fertilizerService.getAllRequests());
    }

   
    @GetMapping("/pending")
    public ResponseEntity<List<FertilizerResponseDTO>> getPendingRequests() {
        return ResponseEntity.ok(fertilizerService.getPendingRequests());
    }

    
    @GetMapping("/pending/{factoryOwnerId}")
    public ResponseEntity<?> getPendingRequestsByFactoryOwner(@PathVariable Integer factoryOwnerId) {
        try {
            return ResponseEntity.ok(fertilizerService.getPendingRequestsByFactoryOwner(factoryOwnerId));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    @GetMapping("/factory/{factoryOwnerId}")
    public ResponseEntity<?> getAllRequestsByFactoryOwner(@PathVariable Integer factoryOwnerId) {
        try {
            return ResponseEntity.ok(fertilizerService.getAllRequestsByFactoryOwner(factoryOwnerId));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

   
    @GetMapping("/owner/{landOwnerId}")
    public ResponseEntity<?> getRequestsByOwner(@PathVariable Integer landOwnerId) {
        try {
            return ResponseEntity.ok(
                    fertilizerService.getRequestsByLandOwner(landOwnerId));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    
    @PutMapping("/{requestId}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Integer requestId,
                                           @Valid @RequestBody FertilizerStatusUpdateDTO request) {
        try {
            FertilizerResponseDTO response =
                    fertilizerService.updateStatus(requestId, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }
}