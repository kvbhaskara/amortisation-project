package com.ldms.amortisation.exceptions;

public class InvalidLoanDetailsException extends RuntimeException {
    public InvalidLoanDetailsException(String message) {
        super(message);
    }
}
