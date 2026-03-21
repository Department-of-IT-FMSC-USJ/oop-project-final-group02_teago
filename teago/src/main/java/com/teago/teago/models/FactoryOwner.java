package com.teago.teago.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "FactoryName", length = 100)
    private String factoryName;

    @Column(name = "FactoryLocation", length = 255)
    private String factoryLocation;

}
