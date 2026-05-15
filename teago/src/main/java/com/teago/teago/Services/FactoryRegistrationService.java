package com.teago.teago.Services;

import com.teago.teago.models.FactoryOwner;
import com.teago.teago.models.LandOwner;
import com.teago.teago.models.LandOwnerFactory;
import com.teago.teago.Repositories.FactoryOwnerRepository;
import com.teago.teago.Repositories.LandOwnerFactoryRepository;
import com.teago.teago.Repositories.LandOwnerRepository;
import com.teago.teago.dto.FactoryRegistrationRequestDTO;
import com.teago.teago.dto.FactoryRegistrationResponseDTO;
import com.teago.teago.dto.FactoryRegistrationStatusUpdateDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class FactoryRegistrationService {

    private final LandOwnerFactoryRepository registrationRepository;
    private final LandOwnerRepository        landOwnerRepository;
    private final FactoryOwnerRepository     factoryOwnerRepository;

    public FactoryRegistrationService(LandOwnerFactoryRepository registrationRepository,
                                       LandOwnerRepository landOwnerRepository,
                                       FactoryOwnerRepository factoryOwnerRepository) {
        this.registrationRepository = registrationRepository;
        this.landOwnerRepository    = landOwnerRepository;
        this.factoryOwnerRepository = factoryOwnerRepository;
    }

    // ── Land Owner: register with a factory ──────────────────────────────────

    public FactoryRegistrationResponseDTO register(FactoryRegistrationRequestDTO request) {
        LandOwner landOwner = landOwnerRepository.findById(request.getLandOwnerId())
                .orElseThrow(() -> new RuntimeException("Land owner not found"));

        FactoryOwner factory = factoryOwnerRepository.findById(request.getFactoryId())
                .orElseThrow(() -> new RuntimeException("Factory not found: "
                        + request.getFactoryId()));

        // Encapsulation: prevent duplicate registrations
        registrationRepository.findByLandOwnerAndFactory(landOwner, factory)
                .ifPresent(existing -> {
                    throw new RuntimeException(
                            "Already registered with " + factory.getFactoryName()
                            + " — status: " + existing.getStatus().name());
                });

        LandOwnerFactory reg = new LandOwnerFactory();
        reg.setLandOwner(landOwner);
        reg.setFactory(factory);
        reg.setLandSize(request.getLandSize());
        reg.setRegistrationDate(LocalDate.now());
        reg.setStatus(LandOwnerFactory.Status.Pending);  // always starts Pending
        registrationRepository.save(reg);

        return toDTO(reg);
    }

    // ── Land Owner: view all their factory registrations ─────────────────────

    public List<FactoryRegistrationResponseDTO> getMyFactories(Integer landOwnerId) {
        LandOwner landOwner = landOwnerRepository.findById(landOwnerId)
                .orElseThrow(() -> new RuntimeException("Land owner not found"));

        return registrationRepository.findByLandOwner(landOwner).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ── Land Owner: browse all available factories to register with ───────────

    public List<FactoryRegistrationResponseDTO> getAllFactories() {
        // Returns all factories as "unregistered" DTOs for browsing
        return factoryOwnerRepository.findAll().stream()
                .map(f -> new FactoryRegistrationResponseDTO(
                        null,
                        null, null,
                        f.getFactoryOwnerId(),
                        f.getFactoryName(),
                        f.getFactoryLocation(),
                        null,
                        null,
                        null))
                .collect(Collectors.toList());
    }

    // ── Factory Owner: view all pending registrations for their factory ───────

    public List<FactoryRegistrationResponseDTO> getPendingRegistrations(Integer factoryOwnerId) {
        FactoryOwner factory = factoryOwnerRepository.findById(factoryOwnerId)
                .orElseThrow(() -> new RuntimeException("Factory not found"));

        return registrationRepository
                .findByFactoryAndStatus(factory, LandOwnerFactory.Status.Pending).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ── Factory Owner: approve or reject a registration ───────────────────────

    public FactoryRegistrationResponseDTO updateStatus(Integer registrationId,
                                                        FactoryRegistrationStatusUpdateDTO req) {
        LandOwnerFactory reg = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registration not found: "
                        + registrationId));

        reg.setStatus(LandOwnerFactory.Status.valueOf(req.getStatus()));
        registrationRepository.save(reg);
        return toDTO(reg);
    }

    // ── Private helper ────────────────────────────────────────────────────────

    private FactoryRegistrationResponseDTO toDTO(LandOwnerFactory r) {
        return new FactoryRegistrationResponseDTO(
                r.getRegistrationId(),
                r.getLandOwner().getLandOwnerId(),
                r.getLandOwner().getUser().getName(),
                r.getFactory().getFactoryOwnerId(),
                r.getFactory().getFactoryName(),
                r.getFactory().getFactoryLocation(),
                r.getLandSize(),
                r.getStatus() != null ? r.getStatus().name() : null,
                r.getRegistrationDate()
        );
    }
}