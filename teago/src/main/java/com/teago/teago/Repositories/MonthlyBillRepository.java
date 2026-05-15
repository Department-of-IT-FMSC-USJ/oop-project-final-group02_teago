package com.teago.teago.Repositories;

import com.teago.teago.models.LandOwner;
import com.teago.teago.models.MonthlyBill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MonthlyBillRepository extends JpaRepository<MonthlyBill, Integer> {

    
    List<MonthlyBill> findByLandOwner(LandOwner landOwner);

    
    Optional<MonthlyBill> findByLandOwnerAndMonthAndYear(LandOwner landOwner,
                                                          Integer month, Integer year);

    
    List<MonthlyBill> findByLandOwnerIn(Collection<LandOwner> landOwners);
}