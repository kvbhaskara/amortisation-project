package com.ldms.amortisation.validations;

import com.ldms.amortisation.exceptions.InvalidLoanDetailsException;
import com.ldms.amortisation.model.LoanDetails;
import org.springframework.stereotype.Component;

public class LoanValidations {


    public static void validateLoanDetails(LoanDetails loanDetails) {
        if (loanDetails.getAssetCost() <= 0) {
            throw new InvalidLoanDetailsException("Asset cost must be greater than zero.");
        }
        if (loanDetails.getDeposit() < 0) {
            throw new InvalidLoanDetailsException("Deposit cannot be negative.");
        }
        if (loanDetails.getAnnualInterestRate() <= 0) {
            throw new InvalidLoanDetailsException("Annual interest rate must be greater than zero.");
        }
        if (loanDetails.getNumberOfPayments() <= 0) {
            throw new InvalidLoanDetailsException("Number of payments must be greater than zero.");
        }
        if (loanDetails.getBalloonPayment() < 0) {
            throw new InvalidLoanDetailsException("Balloon payment cannot be negative.");
        }
    }
}

