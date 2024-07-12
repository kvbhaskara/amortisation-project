package com.ldms.amortisation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ldms.amortisation.controller.AmortisationController;
import com.ldms.amortisation.exceptions.ResourceNotFoundException;
import com.ldms.amortisation.model.AmortisationSchedule;
import com.ldms.amortisation.model.LoanDetails;
import com.ldms.amortisation.service.AmortisationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AmortisationController.class)
public class AmortisationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AmortisationService service;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Positive test case for createSchedule endpoint
    @Test
    public void testCreateScheduleSuccess() throws Exception {
        LoanDetails loanDetails = new LoanDetails();
        loanDetails.setAssetCost(100000);
        loanDetails.setDeposit(10000);
        loanDetails.setAnnualInterestRate(5);
        loanDetails.setNumberOfPayments(12);
        loanDetails.setBalloonPayment(0);

        AmortisationSchedule schedule = new AmortisationSchedule();
        when(service.createSchedule(any(LoanDetails.class))).thenReturn(schedule);

        mockMvc.perform(post("/api/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanDetails)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    // Negative test case for createSchedule endpoint with invalid data
    @Test
    public void testCreateScheduleInvalidLoanDetails() throws Exception {
        LoanDetails loanDetails = new LoanDetails();
        loanDetails.setAssetCost(0); // Invalid asset cost

        mockMvc.perform(post("/api/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanDetails)))
                .andExpect(status().isBadRequest());
    }

    // Positive test case for listSchedules endpoint
    @Test
    public void testListSchedulesSuccess() throws Exception {
        AmortisationSchedule schedule = new AmortisationSchedule();
        List<AmortisationSchedule> schedules = Collections.singletonList(schedule);
        when(service.listSchedules()).thenReturn(schedules);

        mockMvc.perform(get("/api/schedules"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    // Negative test case for listSchedules endpoint with no schedules found
    @Test
    public void testListSchedulesNotFound() throws Exception {
        when(service.listSchedules()).thenThrow(new ResourceNotFoundException("No amortization schedules found."));

        mockMvc.perform(get("/api/schedules"))
                .andExpect(status().isNotFound());
    }

    // Positive test case for getSchedule endpoint
    @Test
    public void testGetScheduleSuccess() throws Exception {
        AmortisationSchedule schedule = new AmortisationSchedule();
        when(service.getSchedule(anyLong())).thenReturn(schedule);

        mockMvc.perform(get("/api/schedules/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    // Negative test case for getSchedule endpoint with invalid ID
    @Test
    public void testGetScheduleNotFound() throws Exception {
        when(service.getSchedule(anyLong())).thenThrow(new ResourceNotFoundException("Schedule not found with id 1"));

        mockMvc.perform(get("/api/schedules/{id}", 1L))
                .andExpect(status().isNotFound());
    }
}

