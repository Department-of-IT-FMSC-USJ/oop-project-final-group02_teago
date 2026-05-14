package com.teago.teago.Repositories;

import com.teago.teago.Models.FactoryOwner;
import com.teago.teago.Models.TeaRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeaRateRepository extends JpaRepository<TeaRate, Integer> {

    
    List<TeaRate> findByFactoryOwnerOrderByEffectiveFromDesc(FactoryOwner factoryOwner);

    
    Optional<TeaRate> findFirstByFactoryOwnerOrderByEffectiveFromDesc(FactoryOwner factoryOwner);
}