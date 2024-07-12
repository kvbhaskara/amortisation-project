package com.ldms.amortisation.repository;

import com.ldms.amortisation.model.LoanDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanDetailsRepository extends JpaRepository<LoanDetails, Long> {
}
