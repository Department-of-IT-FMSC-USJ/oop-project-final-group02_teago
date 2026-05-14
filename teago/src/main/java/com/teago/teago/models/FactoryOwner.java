package com.teago.teago.models;

public class FactoryOwner {

}
    @Id
    @Column(name = "FactoryOwnerID")
    private Integer factoryOwnerId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "FactoryOwnerID")
    private User user;


    @Column(name = "FullName", length = 150)
    private String fullName;

    @Column(name = "FactoryName", length = 150)
    private String factoryName;

   
    @Column(name = "FactoryLocation", length = 255)
    private String factoryLocation;

   
    @Column(name = "RegistrationNumber", length = 100, unique = true)
    private String registrationNumber;
}


    