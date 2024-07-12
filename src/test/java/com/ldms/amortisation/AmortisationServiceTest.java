package com.ldms.amortisation;

import com.ldms.amortisation.exceptions.InvalidLoanDetailsException;
import com.ldms.amortisation.exceptions.ResourceNotFoundException;
import com.ldms.amortisation.model.AmortisationSchedule;
import com.ldms.amortisation.model.LoanDetails;
import com.ldms.amortisation.repository.AmortisationScheduleRepository;
import com.ldms.amortisation.repository.LoanDetailsRepository;
import com.ldms.amortisation.service.AmortisationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AmortisationServiceTest {

    @InjectMocks
    private AmortisationService service;

    @Mock
    private AmortisationScheduleRepository scheduleRepository;

    @Mock
    private LoanDetailsRepository loanDetailsRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Positive test case for createSchedule
    @Test
    public void testCreateScheduleSuccess() {
        LoanDetails loanDetails = new LoanDetails();
        loanDetails.setAssetCost(100000);
        loanDetails.setDeposit(10000);
        loanDetails.setAnnualInterestRate(5);
        loanDetails.setNumberOfPayments(12);
        loanDetails.setBalloonPayment(0);

        when(loanDetailsRepository.save(any(LoanDetails.class))).thenReturn(loanDetails);
        when(scheduleRepository.save(any(AmortisationSchedule.class))).thenReturn(new AmortisationSchedule());

        AmortisationSchedule schedule = service.createSchedule(loanDetails);
        assertNotNull(schedule);
        verify(loanDetailsRepository, times(1)).save(loanDetails);
        verify(scheduleRepository, times(1)).save(any(AmortisationSchedule.class));
    }

    // Negative test case for createSchedule with invalid loan details
    @Test
    public void testCreateScheduleInvalidLoanDetails() {
        LoanDetails loanDetails = new LoanDetails();
        loanDetails.setAssetCost(0); // Invalid asset cost

        assertThrows(InvalidLoanDetailsException.class, () -> {
            service.createSchedule(loanDetails);
        });
    }

    // Positive test case for listSchedules
    @Test
    public void testListSchedulesSuccess() {
        List<AmortisationSchedule> schedules = new ArrayList<>();
        schedules.add(new AmortisationSchedule());

        when(scheduleRepository.findAll()).thenReturn(schedules);

        List<AmortisationSchedule> result = service.listSchedules();
        assertEquals(1, result.size());
        verify(scheduleRepository, times(1)).findAll();
    }

    // Negative test case for listSchedules with no schedules found
    @Test
    public void testListSchedulesNotFound() {
        when(scheduleRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(ResourceNotFoundException.class, () -> {
            service.listSchedules();
        });
    }

    // Positive test case for getSchedule
    @Test
    public void testGetScheduleSuccess() {
        AmortisationSchedule schedule = new AmortisationSchedule();
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));

        AmortisationSchedule result = service.getSchedule(1L);
        assertNotNull(result);
        verify(scheduleRepository, times(1)).findById(1L);
    }

    // Negative test case for getSchedule with invalid ID
    @Test
    public void testGetScheduleNotFound() {
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            service.getSchedule(1L);
        });
    }
}