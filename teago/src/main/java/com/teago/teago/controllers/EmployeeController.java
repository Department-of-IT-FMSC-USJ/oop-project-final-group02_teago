package com.teago.teago.controllers;

import com.teago.teago.Services.EmployeeService;
import com.teago.teago.dto.EmployeeRequestDTO;
import com.teago.teago.dto.EmployeeResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<?> addEmployee(
            @Valid @RequestBody EmployeeRequestDTO request) {
        try {
            EmployeeResponseDTO response = employeeService.addEmployee(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    @GetMapping("/owner/{landOwnerId}")
    public ResponseEntity<?> getEmployeesByOwner(@PathVariable Integer landOwnerId) {
        try {
            List<EmployeeResponseDTO> list =
                    employeeService.getEmployeesByLandOwner(landOwnerId);
            return ResponseEntity.ok(list);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    
    @PutMapping("/{employeeId}")
    public ResponseEntity<?> updateEmployee(@PathVariable Integer employeeId,
                                             @Valid @RequestBody EmployeeRequestDTO request) {
        try {
            EmployeeResponseDTO response =
                    employeeService.updateEmployee(employeeId, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Integer employeeId) {
        try {
            employeeService.deleteEmployee(employeeId);
            return ResponseEntity.ok(Map.of("message", "Employee deleted successfully"));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }
}