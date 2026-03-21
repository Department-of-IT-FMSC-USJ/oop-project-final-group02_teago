package com.teago.teago.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * REQUEST DTO — used by the Factory Owner to approve or reject
 * a land owner's registration request.
 */
public class FactoryRegistrationStatusUpdateDTO {

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "Active|Rejected", message = "Status must be 'Active' or 'Rejected'")
    private String status;

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getStatus()             { return status; }
    public void   setStatus(String status){ this.status = status; }
}