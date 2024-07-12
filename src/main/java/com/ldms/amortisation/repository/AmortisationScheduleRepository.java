package com.ldms.amortisation.repository;

import com.ldms.amortisation.model.AmortisationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmortisationScheduleRepository extends JpaRepository<AmortisationSchedule, Long> {
}
