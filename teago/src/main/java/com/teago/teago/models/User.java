package com.teago.teago.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID")
    private Integer userId;

    @Column(name = "Name", length = 100)
    private String name;

    @Column(name = "NIC", length = 12)
    private String nic;

    @Column(name = "Address", length = 255)
    private String address;

    @Column(name = "ContactNumber", length = 15)
    private String contactNumber;

    @Column(name = "Username", length = 50)
    private String username;

    @Column(name = "Password", length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "Role")
    private Role role;

    public enum Role {
        LandOwner, FactoryOwner, Supervisor
    }

    
}
