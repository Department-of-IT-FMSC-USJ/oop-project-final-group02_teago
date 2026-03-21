package com.teago.teago.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * FactoryOwner — stores factory owner profile data.
 * Inherits identity from User via @MapsId (shared primary key).
 *
 * OOP Encapsulation: all factory-specific fields are kept here,
 * separate from generic User credentials.
 */
@Entity
@Getter
@Setter
@Table(name = "FactoryOwner")
public class FactoryOwner {

    @Id
    @Column(name = "FactoryOwnerID")
    private Integer factoryOwnerId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "FactoryOwnerID")
    private User user;

    /** Full name of the factory owner */
    @Column(name = "FullName", length = 150)
    private String fullName;

    /** Official factory / company name */
    @Column(name = "FactoryName", length = 150)
    private String factoryName;

    /** Physical location of the factory */
    @Column(name = "FactoryLocation", length = 255)
    private String factoryLocation;

    /** Official business / company registration number */
    @Column(name = "RegistrationNumber", length = 100, unique = true)
    private String registrationNumber;
}