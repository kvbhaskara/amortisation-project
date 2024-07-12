package com.ldms.amortisation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class LoanDetails {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private double assetCost;
    private double deposit;
    private double annualInterestRate;
    private int numberOfPayments;
    private double balloonPayment;

}