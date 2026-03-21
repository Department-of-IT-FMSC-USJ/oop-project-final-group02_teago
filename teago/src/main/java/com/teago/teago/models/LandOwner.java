package com.teago.teago.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * LandOwner — stores tea land owner profile data.
 * Inherits identity from User via @MapsId (shared primary key).
 *
 * OOP: Encapsulation — land-specific fields (size, location) are
 * kept here, separate from the generic User credentials.
 */
@Entity
@Table(name = "LandOwner")
@Getter
@Setter
public class LandOwner {

    @Id
    @Column(name = "LandOwnerID")
    private Integer landOwnerId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "LandOwnerID")
    private User user;

    /** Total land area in acres */
    @Column(name = "LandSize", precision = 10, scale = 2)
    private BigDecimal landSize;

    /** Physical location / address of the land */
    @Column(name = "LandLocation", length = 255)
    private String landLocation;

    /** Factory registrations (many-to-many via LandOwnerFactory) */
    @OneToMany(mappedBy = "landOwner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LandOwnerFactory> factoryRegistrations = new ArrayList<>();
}