package com.ldms.amortisation.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class AmortisationSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private LoanDetails loanDetails;

    @ElementCollection
    private List<AmortisationEntry> scheduleEntries;

    private double monthlyRepayment;
    private double totalInterestDue;
    private double totalPaymentsDue;


    @Data
    @Embeddable
    public static class AmortisationEntry {

        public AmortisationEntry() {
        }

        public AmortisationEntry(int period, double payment, double principal, double interest, double balance) {
            this.period = period;
            this.payment = payment;
            this.principal = principal;
            this.interest = interest;
            this.balance = balance;
        }

        private int period;
        private double payment;
        private double principal;
        private double interest;
        private double balance;

    }
}
