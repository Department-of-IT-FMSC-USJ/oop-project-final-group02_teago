package com.teago.teago.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * RESPONSE DTO — returned for every LandOwnerFactory record.
 *
 * Used by the Land Owner (My Factories page) and the
 * Factory Owner (pending registrations list).
 *
 * Abstraction: callers never touch the LandOwnerFactory entity.
 */
public class FactoryRegistrationResponseDTO {

    private Integer    registrationId;
    private Integer    landOwnerId;
    private String     landOwnerName;
    private Integer    factoryId;
    private String     factoryName;
    private String     factoryLocation;
    private BigDecimal landSize;
    private String     status;           // "Pending" | "Active" | "Rejected"
    private LocalDate  registrationDate;

    // ── Constructor ───────────────────────────────────────────────────────────

    public FactoryRegistrationResponseDTO(Integer registrationId,
                                           Integer landOwnerId, String landOwnerName,
                                           Integer factoryId, String factoryName,
                                           String factoryLocation, BigDecimal landSize,
                                           String status, LocalDate registrationDate) {
        this.registrationId   = registrationId;
        this.landOwnerId      = landOwnerId;
        this.landOwnerName    = landOwnerName;
        this.factoryId        = factoryId;
        this.factoryName      = factoryName;
        this.factoryLocation  = factoryLocation;
        this.landSize         = landSize;
        this.status           = status;
        this.registrationDate = registrationDate;
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public Integer    getRegistrationId()   { return registrationId; }
    public Integer    getLandOwnerId()      { return landOwnerId; }
    public String     getLandOwnerName()    { return landOwnerName; }
    public Integer    getFactoryId()        { return factoryId; }
    public String     getFactoryName()      { return factoryName; }
    public String     getFactoryLocation()  { return factoryLocation; }
    public BigDecimal getLandSize()         { return landSize; }
    public String     getStatus()           { return status; }
    public LocalDate  getRegistrationDate() { return registrationDate; }
}