package com.teago.teago.Repositories;

import com.teago.teago.models.LandOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LandOwnerRepository extends JpaRepository<LandOwner, Integer> {

    
    Optional<LandOwner> findByUser_UserId(Integer userId);

    
}