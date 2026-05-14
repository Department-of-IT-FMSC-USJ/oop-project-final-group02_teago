package com.teago.teago.Services;

import com.teago.teago.models.Cheque;
import com.teago.teago.models.FactoryOwner;
import com.teago.teago.models.LandOwner;
import com.teago.teago.models.Loan;
import com.teago.teago.Repositories.ChequeRepository;
import com.teago.teago.Repositories.FactoryOwnerRepository;
import com.teago.teago.Repositories.LandOwnerRepository;
import com.teago.teago.Repositories.LoanRepository;
import com.teago.teago.Repositories.SupervisorRepository;
import com.teago.teago.dto.ChequeIssueRequestDTO;
import com.teago.teago.dto.ChequeResponseDTO;
import com.teago.teago.dto.ChequeStatusUpdateDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ChequeService {

    private final ChequeRepository     chequeRepository;
    private final LoanRepository       loanRepository;
    private final LandOwnerRepository  landOwnerRepository;
        private final FactoryOwnerRepository factoryOwnerRepository;

    public ChequeService(ChequeRepository chequeRepository,
                         LoanRepository loanRepository,
                                                 SupervisorRepository supervisorRepository,
                                                 LandOwnerRepository landOwnerRepository,
                                                 FactoryOwnerRepository factoryOwnerRepository) {
        this.chequeRepository     = chequeRepository;
        this.loanRepository       = loanRepository;
        this.landOwnerRepository = landOwnerRepository;
                this.factoryOwnerRepository = factoryOwnerRepository;
    }


    public ChequeResponseDTO issueCheque(ChequeIssueRequestDTO request) {
        Loan loan = loanRepository.findById(request.getLoanId())
                .orElseThrow(() -> new RuntimeException("Loan not found: "
                        + request.getLoanId()));

        if (loan.getApprovalStatus() != Loan.ApprovalStatus.Approved) {
            throw new RuntimeException("Cannot issue cheque for a non-approved loan");
        }

        Cheque cheque = new Cheque();
        cheque.setLoan(loan);
        cheque.setIssueDate(LocalDate.now());
        cheque.setDeliveryStatus(Cheque.DeliveryStatus.Pending);
        cheque.setVerificationStatus(Cheque.VerificationStatus.Pending);
        chequeRepository.save(cheque);

        return toDTO(cheque);
    }



    public ChequeResponseDTO updateDeliveryStatus(Integer chequeId,
                                                   ChequeStatusUpdateDTO update) {
        Cheque cheque = findCheque(chequeId);
        cheque.setDeliveryStatus(
                Cheque.DeliveryStatus.valueOf(update.getStatus()));
        chequeRepository.save(cheque);
        return toDTO(cheque);
    }


    public ChequeResponseDTO markChequeAsIssued(Integer chequeId) {
        Cheque cheque = findCheque(chequeId);
        if (cheque.getIssueDate() != null) {
            throw new RuntimeException("Cheque is already issued");
        }
        cheque.setIssueDate(LocalDate.now());
        chequeRepository.save(cheque);
        return toDTO(cheque);
    }

  

    public ChequeResponseDTO updateVerificationStatus(Integer chequeId,
                                                       ChequeStatusUpdateDTO update) {
        Cheque cheque = findCheque(chequeId);
        cheque.setVerificationStatus(
                Cheque.VerificationStatus.valueOf(update.getStatus()));
        chequeRepository.save(cheque);
        return toDTO(cheque);
    }

 

    public List<ChequeResponseDTO> getAllCheques() {
        return chequeRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    public List<ChequeResponseDTO> getChequesBySupervisor(Integer supervisorId) {
        // Cheque entity no longer stores supervisor linkage directly.
        // Keep endpoint stable by returning all cheques for now.
        return chequeRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    public List<ChequeResponseDTO> getChequesByLandOwner(Integer landOwnerId) {
        LandOwner landOwner = landOwnerRepository.findById(landOwnerId)
                .orElseThrow(() -> new RuntimeException("Land owner not found"));

        return chequeRepository.findByLoan_LandOwner(landOwner).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    public List<ChequeResponseDTO> getChequesByFactoryOwner(Integer factoryOwnerId) {
        FactoryOwner factoryOwner = factoryOwnerRepository.findById(factoryOwnerId)
                .orElseThrow(() -> new RuntimeException("Factory owner not found"));

        return chequeRepository.findByLoan_FactoryOwner(factoryOwner).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    private Cheque findCheque(Integer chequeId) {
        return chequeRepository.findById(chequeId)
                .orElseThrow(() -> new RuntimeException("Cheque not found: " + chequeId));
    }

    private ChequeResponseDTO toDTO(Cheque c) {
        return new ChequeResponseDTO(
                c.getChequeId(),
                c.getLoan().getLoanId(),
                c.getLoan().getLoanAmount(),
                c.getLoan().getLandOwner().getLandOwnerId(),
                c.getLoan().getLandOwner().getUser().getName(),
                "Auto-assigned", // Demo supervisor name
                c.getIssueDate(),
                c.getDeliveryStatus().name(),
                c.getVerificationStatus().name()
        );
    }
}