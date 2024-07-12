package com.ldms.amortisation.service;

import com.ldms.amortisation.exceptions.InvalidLoanDetailsException;
import com.ldms.amortisation.exceptions.ResourceNotFoundException;
import com.ldms.amortisation.model.AmortisationSchedule;
import com.ldms.amortisation.model.LoanDetails;
import com.ldms.amortisation.repository.AmortisationScheduleRepository;
import com.ldms.amortisation.repository.LoanDetailsRepository;
import com.ldms.amortisation.util.ConvertToBigDecimal;
import com.ldms.amortisation.validations.LoanValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AmortisationService {

    @Autowired
    private AmortisationScheduleRepository amortisationSchedulerepository;

    @Autowired
    private LoanDetailsRepository loanDetailsRepository;


    /**
     * Creates an amortization schedule based on the given loan details.
     *
     * @param loanDetails The details of the loan including asset cost, deposit, annual interest rate, number of payments, and balloon payment.
     * @return The created AmortisationSchedule object containing the payment schedule and financial details.
     * @throws InvalidLoanDetailsException if the loan details are invalid.
     */
    @Transactional
    public AmortisationSchedule createSchedule(LoanDetails loanDetails) {

        AmortisationSchedule schedule = new AmortisationSchedule();
        try {
            LoanValidations.validateLoanDetails(loanDetails);
        } catch (InvalidLoanDetailsException e) {
            throw new InvalidLoanDetailsException("Invalid loan details provided: " + e.getMessage());
        }
        LoanDetails savedLoanDetails = loanDetailsRepository.save(loanDetails);

        double principleAmount = loanDetails.getAssetCost() - loanDetails.getDeposit();
        double rateOfInterest = loanDetails.getAnnualInterestRate() / 12 / 100;
        int numberOfMonths = loanDetails.getNumberOfPayments();
        double balloonPayment = loanDetails.getBalloonPayment();

        double monthlyRepayment;
        if (balloonPayment > 0) {
            monthlyRepayment = (principleAmount - (balloonPayment / Math.pow(1 + rateOfInterest, numberOfMonths))) * (rateOfInterest / (1 - Math.pow(1 + rateOfInterest, -numberOfMonths)));
        } else {
            monthlyRepayment = principleAmount * ((rateOfInterest * Math.pow(1 + rateOfInterest, numberOfMonths)) / (Math.pow(1 + rateOfInterest, numberOfMonths) - 1));
        }

        List<AmortisationSchedule.AmortisationEntry> entries = new ArrayList<>();
        double balance = principleAmount;

        for (int i = 1; i <= numberOfMonths; i++) {
            double interest = balance * rateOfInterest;
            double principal = monthlyRepayment - interest;
            balance -= principal;

            if (i == numberOfMonths && balloonPayment > 0) {
                balance += balloonPayment;
                principal = monthlyRepayment + balloonPayment - interest;
            }

            entries.add(new AmortisationSchedule.AmortisationEntry(i, ConvertToBigDecimal.getBigDecimal(monthlyRepayment).doubleValue(),
                    ConvertToBigDecimal.getBigDecimal(principal).doubleValue(), ConvertToBigDecimal.getBigDecimal(interest).doubleValue(),
                    Math.max(ConvertToBigDecimal.getBigDecimal(balance).doubleValue(), 0)));
        }

        schedule.setLoanDetails(savedLoanDetails);
        schedule.setScheduleEntries(entries);
        schedule.setMonthlyRepayment(ConvertToBigDecimal.getBigDecimal(monthlyRepayment).doubleValue());
        schedule.setTotalInterestDue(entries.stream().mapToDouble(AmortisationSchedule.AmortisationEntry::getInterest).sum());
        schedule.setTotalPaymentsDue(ConvertToBigDecimal.getBigDecimal(monthlyRepayment * numberOfMonths + balloonPayment).doubleValue());

        return amortisationSchedulerepository.save(schedule);
    }


    /**
     * Retrieves all amortization schedules.
     *
     * @return A list of AmortisationSchedule objects.
     * @throws ResourceNotFoundException if no amortization schedules are found.
     */
    public List<AmortisationSchedule> listSchedules() {
        List<AmortisationSchedule> schedules = amortisationSchedulerepository.findAll();
        if (schedules.isEmpty()) {
            throw new ResourceNotFoundException("No amortization schedules found.");
        }
        return schedules;
    }

    /**
     * Retrieves a specific amortization schedule by its ID.
     *
     * @param id The ID of the amortization schedule to retrieve.
     * @return The AmortisationSchedule object with the specified ID.
     * @throws ResourceNotFoundException if the amortization schedule with the specified ID is not found.
     */
    public AmortisationSchedule getSchedule(Long id) {
        return amortisationSchedulerepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id " + id));
    }

}