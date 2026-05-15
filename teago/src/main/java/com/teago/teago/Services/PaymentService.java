package com.teago.teago.Services;

import com.teago.teago.models.FertilizerRequest;
import com.teago.teago.models.Delivery;
import com.teago.teago.models.FactoryOwner;
import com.teago.teago.models.LandOwner;
import com.teago.teago.models.LandOwnerFactory;
import com.teago.teago.models.Loan;
import com.teago.teago.models.MonthlyBill;
import com.teago.teago.models.Payment;
import com.teago.teago.Repositories.DeliveryRepository;
import com.teago.teago.Repositories.FactoryOwnerRepository;
import com.teago.teago.Repositories.FertilizerRequestRepository;
import com.teago.teago.Repositories.LandOwnerFactoryRepository;
import com.teago.teago.Repositories.LandOwnerRepository;
import com.teago.teago.Repositories.LoanRepository;
import com.teago.teago.Repositories.MonthlyBillRepository;
import com.teago.teago.Repositories.PaymentRepository;
import com.teago.teago.dto.IncomeStatementMonthDTO;
import com.teago.teago.dto.IncomeStatementResponseDTO;
import com.teago.teago.dto.MonthlyBillResponseDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;


@Service
public class MonthlyBillService {

    private final MonthlyBillRepository       billRepository;
    private final LandOwnerRepository         landOwnerRepository;
    private final DeliveryRepository          deliveryRepository;
    private final PaymentRepository           paymentRepository;
    private final LoanRepository              loanRepository;
    private final FertilizerRequestRepository fertilizerRepository;
        private final FactoryOwnerRepository      factoryOwnerRepository;
        private final LandOwnerFactoryRepository  landOwnerFactoryRepository;

    public MonthlyBillService(MonthlyBillRepository billRepository,
                               LandOwnerRepository landOwnerRepository,
                               DeliveryRepository deliveryRepository,
                               PaymentRepository paymentRepository,
                               LoanRepository loanRepository,
                                                           FertilizerRequestRepository fertilizerRepository,
                                                           FactoryOwnerRepository factoryOwnerRepository,
                                                           LandOwnerFactoryRepository landOwnerFactoryRepository) {
        this.billRepository       = billRepository;
        this.landOwnerRepository  = landOwnerRepository;
        this.deliveryRepository   = deliveryRepository;
        this.paymentRepository    = paymentRepository;
        this.loanRepository       = loanRepository;
        this.fertilizerRepository = fertilizerRepository;
                this.factoryOwnerRepository = factoryOwnerRepository;
                this.landOwnerFactoryRepository = landOwnerFactoryRepository;
    }

    

    public MonthlyBillResponseDTO generateBill(Integer landOwnerId,
                                               Integer month, Integer year) {
        validateMonthYear(month, year);

        LandOwner landOwner = landOwnerRepository.findById(landOwnerId)
                .orElseThrow(() -> new RuntimeException("Land owner not found"));

        return createOrUpdateBill(landOwner, month, year);
    }

    public MonthlyBillResponseDTO generateBillForFactoryOwner(Integer factoryOwnerId,
                                                              Integer landOwnerId,
                                                              Integer month,
                                                              Integer year) {
        validateMonthYear(month, year);

        FactoryOwner factoryOwner = factoryOwnerRepository.findById(factoryOwnerId)
                .orElseThrow(() -> new RuntimeException("Factory owner not found"));
        LandOwner landOwner = landOwnerRepository.findById(landOwnerId)
                .orElseThrow(() -> new RuntimeException("Land owner not found"));

        LandOwnerFactory registration = landOwnerFactoryRepository
                .findByLandOwnerAndFactory(landOwner, factoryOwner)
                .orElseThrow(() -> new RuntimeException("Land owner is not registered with this factory"));

        if (registration.getStatus() != LandOwnerFactory.Status.Active) {
            throw new RuntimeException("Land owner registration is not active for this factory");
        }

        return createOrUpdateBill(landOwner, month, year);
    }

    public List<MonthlyBillResponseDTO> generateBillsForFactoryOwner(Integer factoryOwnerId,
                                                                      Integer month,
                                                                      Integer year) {
        validateMonthYear(month, year);

        FactoryOwner factoryOwner = factoryOwnerRepository.findById(factoryOwnerId)
                .orElseThrow(() -> new RuntimeException("Factory owner not found"));

        List<LandOwnerFactory> activeRegs = landOwnerFactoryRepository
                .findByFactoryAndStatus(factoryOwner, LandOwnerFactory.Status.Active);

        return activeRegs.stream()
                .map(LandOwnerFactory::getLandOwner)
                .map(owner -> createOrUpdateBill(owner, month, year))
                .sorted(Comparator.comparing(MonthlyBillResponseDTO::getLandOwnerName,
                        String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    

    public List<MonthlyBillResponseDTO> getBillsByLandOwner(Integer landOwnerId) {
        LandOwner landOwner = landOwnerRepository.findById(landOwnerId)
                .orElseThrow(() -> new RuntimeException("Land owner not found"));

        return billRepository.findByLandOwner(landOwner).stream()
                .map(b -> toDTOWithBreakdown(b, true))
                .sorted(Comparator.comparing(MonthlyBillResponseDTO::getYear).reversed()
                        .thenComparing(MonthlyBillResponseDTO::getMonth).reversed())
                .collect(Collectors.toList());
    }

    public List<MonthlyBillResponseDTO> getBillsByFactoryOwner(Integer factoryOwnerId) {
        FactoryOwner factoryOwner = factoryOwnerRepository.findById(factoryOwnerId)
                .orElseThrow(() -> new RuntimeException("Factory owner not found"));

        List<LandOwner> landOwners = landOwnerFactoryRepository
                .findByFactory(factoryOwner)
                .stream()
                .map(LandOwnerFactory::getLandOwner)
                .distinct()
                .collect(Collectors.toList());

        if (landOwners.isEmpty()) {
            return List.of();
        }

        return billRepository.findByLandOwnerIn(landOwners).stream()
                .map(b -> toDTOWithBreakdown(b, true))
                .sorted(Comparator.comparing(MonthlyBillResponseDTO::getYear).reversed()
                        .thenComparing(MonthlyBillResponseDTO::getMonth).reversed()
                        .thenComparing(MonthlyBillResponseDTO::getLandOwnerName,
                                String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public IncomeStatementResponseDTO generateIncomeStatement(Integer landOwnerId,
                                                              Integer periodMonths,
                                                              Integer endMonth,
                                                              Integer endYear) {
        validateMonthYear(endMonth, endYear);
        validatePeriodMonths(periodMonths);

        LandOwner landOwner = landOwnerRepository.findById(landOwnerId)
                .orElseThrow(() -> new RuntimeException("Land owner not found"));

        return buildIncomeStatement(landOwner, periodMonths, endMonth, endYear);
    }

    public IncomeStatementResponseDTO generateIncomeStatementForFactoryOwner(Integer factoryOwnerId,
                                                                             Integer landOwnerId,
                                                                             Integer periodMonths,
                                                                             Integer endMonth,
                                                                             Integer endYear) {
        validateMonthYear(endMonth, endYear);
        validatePeriodMonths(periodMonths);

        FactoryOwner factoryOwner = factoryOwnerRepository.findById(factoryOwnerId)
                .orElseThrow(() -> new RuntimeException("Factory owner not found"));
        LandOwner landOwner = landOwnerRepository.findById(landOwnerId)
                .orElseThrow(() -> new RuntimeException("Land owner not found"));

        LandOwnerFactory registration = landOwnerFactoryRepository
                .findByLandOwnerAndFactory(landOwner, factoryOwner)
                .orElseThrow(() -> new RuntimeException("Land owner is not registered with this factory"));

        if (registration.getStatus() != LandOwnerFactory.Status.Active) {
            throw new RuntimeException("Land owner registration is not active for this factory");
        }

        return buildIncomeStatement(landOwner, periodMonths, endMonth, endYear);
    }

    

        private MonthlyBillResponseDTO createOrUpdateBill(LandOwner landOwner,
                                                                                                           Integer month, Integer year) {
                BigDecimal totalEarnings = calculateTotalEarnings(landOwner, month, year);
                BigDecimal deliveryDeductions = calculateDeliveryDeductions(landOwner, month, year);
                BigDecimal loanDeductions = calculateLoanDeductions(landOwner, month, year);
                BigDecimal fertDeductions = calculateFertilizerDeductions(landOwner, month, year);

                BigDecimal totalDeductions = deliveryDeductions.add(loanDeductions).add(fertDeductions);
                BigDecimal netPayable = totalEarnings.subtract(totalDeductions);
                if (netPayable.compareTo(BigDecimal.ZERO) < 0) {
                        netPayable = BigDecimal.ZERO;
                }

                MonthlyBill bill = billRepository
                                .findByLandOwnerAndMonthAndYear(landOwner, month, year)
                                .orElseGet(MonthlyBill::new);

                bill.setLandOwner(landOwner);
                bill.setMonth(month);
                bill.setYear(year);
                bill.setTotalEarnings(totalEarnings);
                bill.setDeliveryDeductions(deliveryDeductions);
                bill.setTotalDeductions(totalDeductions);
                bill.setNetPayable(netPayable);
                bill.setGeneratedDate(LocalDate.now());

                MonthlyBill saved = billRepository.save(bill);
                return toDTO(saved, deliveryDeductions, loanDeductions, fertDeductions);
    }

        private BigDecimal calculateTotalEarnings(LandOwner landOwner, Integer month, Integer year) {
                return deliveryRepository.findByLandOwnerAndMonthAndYear(landOwner, month, year)
                                .stream()
                                .map(delivery -> paymentRepository.findByDelivery(delivery)
                                                .map(Payment::getTotalAmount)
                                                .orElse(BigDecimal.ZERO))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        private BigDecimal calculateDeliveryDeductions(LandOwner landOwner, Integer month, Integer year) {
                return deliveryRepository.findByLandOwnerAndMonthAndYear(landOwner, month, year)
                                .stream()
                                .map(Delivery::getDeductionAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        private BigDecimal calculateLoanDeductions(LandOwner landOwner, Integer month, Integer year) {
                BigDecimal totalDeductions = BigDecimal.ZERO;
                List<Loan> approvedLoans = loanRepository.findByLandOwnerAndApprovalStatus(landOwner, Loan.ApprovalStatus.Approved);
                
                for (Loan loan : approvedLoans) {
                    if (loan.getApprovalDate() == null) {
                        continue; 
                    }
                    
                    int approvalMonth = loan.getApprovalDate().getMonthValue();
                    int approvalYear = loan.getApprovalDate().getYear();
                    
                    if (loan.getRequestType() == Loan.RequestType.LOAN) {
                        
                        if (approvalMonth == month && approvalYear == year) {
                            BigDecimal deduction = loan.getLoanAmount().multiply(new BigDecimal("0.10"));
                            totalDeductions = totalDeductions.add(deduction);
                        }
                    } else if (loan.getRequestType() == Loan.RequestType.ADVANCE) {
                       
                        YearMonth approvalYearMonth = YearMonth.of(approvalYear, approvalMonth);
                        YearMonth currentYearMonth = YearMonth.of(year, month);
                        
                        if (approvalYearMonth.equals(currentYearMonth.minusMonths(1))) {
                            
                            totalDeductions = totalDeductions.add(loan.getLoanAmount());
                        } else if (approvalYearMonth.isBefore(currentYearMonth.minusMonths(1))) {
                            
                            if (loan.getAdvanceDeductedMonth() == null || 
                                loan.getAdvanceDeductedMonth() != month || 
                                loan.getAdvanceDeductedYear() != year) {
                                totalDeductions = totalDeductions.add(loan.getLoanAmount());
                            }
                        }
                       
                    }
                }
                
                return totalDeductions;
        }

        private BigDecimal calculateFertilizerDeductions(LandOwner landOwner, Integer month, Integer year) {
                return fertilizerRepository.findByLandOwner(landOwner)
                                .stream()
                                .filter(f -> f.getApprovalStatus() == FertilizerRequest.ApprovalStatus.Approved)
                                .filter(f -> isSameMonthAndYear(f.getRequestDate(), month, year))
                                .map(FertilizerRequest::getCost)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        private boolean isSameMonthAndYear(LocalDate date, Integer month, Integer year) {
                return date != null && date.getMonthValue() == month && date.getYear() == year;
        }

            private IncomeStatementResponseDTO buildIncomeStatement(LandOwner landOwner,
                                                                    Integer periodMonths,
                                                                    Integer endMonth,
                                                                    Integer endYear) {
                YearMonth end = YearMonth.of(endYear, endMonth);
                List<IncomeStatementMonthDTO> monthRows = new ArrayList<>();

                BigDecimal totalEarnings = BigDecimal.ZERO;
                BigDecimal totalDeductions = BigDecimal.ZERO;
                BigDecimal totalNetPayable = BigDecimal.ZERO;

                for (int i = periodMonths - 1; i >= 0; i--) {
                    YearMonth target = end.minusMonths(i);
                    MonthlyBillResponseDTO bill = createOrUpdateBill(
                            landOwner,
                            target.getMonthValue(),
                            target.getYear()
                    );

                    monthRows.add(new IncomeStatementMonthDTO(
                            bill.getMonth(),
                            bill.getYear(),
                            bill.getTotalEarnings(),
                            bill.getTotalDeductions(),
                            bill.getNetPayable()
                    ));

                    totalEarnings = totalEarnings.add(bill.getTotalEarnings());
                    totalDeductions = totalDeductions.add(bill.getTotalDeductions());
                    totalNetPayable = totalNetPayable.add(bill.getNetPayable());
                }

                return new IncomeStatementResponseDTO(
                        landOwner.getLandOwnerId(),
                        landOwner.getUser().getName(),
                        periodMonths,
                        endMonth,
                        endYear,
                        monthRows,
                        totalEarnings,
                        totalDeductions,
                        totalNetPayable,
                        LocalDate.now()
                );
            }

        private void validateMonthYear(Integer month, Integer year) {
                if (month == null || month < 1 || month > 12) {
                        throw new RuntimeException("Month must be between 1 and 12");
                }
                if (year == null || year < 2000 || year > 3000) {
                        throw new RuntimeException("Year is invalid");
                }
        }

        private void validatePeriodMonths(Integer periodMonths) {
                if (periodMonths == null || periodMonths < 1 || periodMonths > 24) {
                        throw new RuntimeException("Period months must be between 1 and 24");
                }
        }

        private MonthlyBillResponseDTO toDTOWithBreakdown(MonthlyBill bill, boolean recalculate) {
                BigDecimal deliveryDeductions = BigDecimal.ZERO;
                BigDecimal loanDeductions = BigDecimal.ZERO;
                BigDecimal fertDeductions = BigDecimal.ZERO;

                if (recalculate) {
                        LandOwner owner = bill.getLandOwner();
                        deliveryDeductions = calculateDeliveryDeductions(owner, bill.getMonth(), bill.getYear());
                        loanDeductions = calculateLoanDeductions(owner, bill.getMonth(), bill.getYear());
                        fertDeductions = calculateFertilizerDeductions(owner, bill.getMonth(), bill.getYear());
                }

                return toDTO(bill, deliveryDeductions, loanDeductions, fertDeductions);
        }

        private MonthlyBillResponseDTO toDTO(MonthlyBill bill,
                                                                                 BigDecimal deliveryDeductions,
                                                                                 BigDecimal loanDeductions,
                                                                                 BigDecimal fertDeductions) {
                return new MonthlyBillResponseDTO(
                                bill.getBillId(),
                                bill.getLandOwner().getLandOwnerId(),
                                bill.getLandOwner().getUser().getName(),
                                bill.getMonth(),
                                bill.getYear(),
                                bill.getTotalEarnings(),
                                deliveryDeductions,
                                loanDeductions,
                                fertDeductions,
                                bill.getTotalDeductions(),
                                bill.getNetPayable(),
                                bill.getGeneratedDate()
                );
    }
}