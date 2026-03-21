package com.teago.teago.Repositories;

import com.teago.teago.models.LandOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LandOwnerRepository extends JpaRepository<LandOwner, Integer> {

    // Look up a land owner by their linked User ID
    Optional<LandOwner> findByUser_UserId(Integer userId);

    // NOTE: findByFactory() has been removed because LandOwner no longer has
    // a direct @ManyToOne factory field. Factory associations are now managed
    // through the LandOwnerFactory join entity.
    // Use LandOwnerFactoryRepository.findByFactory(factory) instead.
}