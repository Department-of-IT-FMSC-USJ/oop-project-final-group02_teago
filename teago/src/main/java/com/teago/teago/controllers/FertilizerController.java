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

    // Land Owner — submit a fertilizer request
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

    // Factory Owner — view all fertilizer requests
    @GetMapping
    public ResponseEntity<List<FertilizerResponseDTO>> getAllRequests() {
        return ResponseEntity.ok(fertilizerService.getAllRequests());
    }

    // Factory Owner — view only pending requests
    @GetMapping("/pending")
    public ResponseEntity<List<FertilizerResponseDTO>> getPendingRequests() {
        return ResponseEntity.ok(fertilizerService.getPendingRequests());
    }

    // Factory Owner — view only pending requests routed to this factory owner
    @GetMapping("/pending/{factoryOwnerId}")
    public ResponseEntity<?> getPendingRequestsByFactoryOwner(@PathVariable Integer factoryOwnerId) {
        try {
            return ResponseEntity.ok(fertilizerService.getPendingRequestsByFactoryOwner(factoryOwnerId));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    // Factory Owner — view all requests routed to this factory owner
    @GetMapping("/factory/{factoryOwnerId}")
    public ResponseEntity<?> getAllRequestsByFactoryOwner(@PathVariable Integer factoryOwnerId) {
        try {
            return ResponseEntity.ok(fertilizerService.getAllRequestsByFactoryOwner(factoryOwnerId));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    // Land Owner — view their own fertilizer requests
    @GetMapping("/owner/{landOwnerId}")
    public ResponseEntity<?> getRequestsByOwner(@PathVariable Integer landOwnerId) {
        try {
            return ResponseEntity.ok(
                    fertilizerService.getRequestsByLandOwner(landOwnerId));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    // Factory Owner — approve or reject a fertilizer request
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