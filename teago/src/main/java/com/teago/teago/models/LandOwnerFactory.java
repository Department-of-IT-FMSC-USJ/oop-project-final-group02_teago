package com.teago.teago.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Join entity between LandOwner and FactoryOwner.
 *
 * A land owner can register with many factories (e.g. they supply tea
 * to multiple processing plants). Each registration tracks the land
 * size for that specific factory relationship and its approval status.
 *
 * Inheritance: uses a composite-style surrogate PK (auto-generated)
 * while logically representing a many-to-many association.
 */
@Entity
@Table(name = "LandOwnerFactory",
       uniqueConstraints = @UniqueConstraint(
               columnNames = {"LandOwnerID", "FactoryOwnerID"}))
@Getter
@Setter
public class LandOwnerFactory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RegistrationID")
    private Integer registrationId;

    @ManyToOne
    @JoinColumn(name = "LandOwnerID", nullable = false)
    private LandOwner landOwner;

    @ManyToOne
    @JoinColumn(name = "FactoryOwnerID", nullable = false)
    private FactoryOwner factory;

    /** Land size (in acres) the owner supplies to THIS factory. */
    @Column(name = "LandSize", precision = 10, scale = 2)
    private BigDecimal landSize;

    @Column(name = "RegistrationDate")
    private LocalDate registrationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status")
    private Status status;

    public enum Status {
        Pending, Active, Rejected
    }
}