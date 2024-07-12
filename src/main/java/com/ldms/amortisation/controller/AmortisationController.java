package com.ldms.amortisation.controller;

import com.ldms.amortisation.model.AmortisationSchedule;
import com.ldms.amortisation.model.LoanDetails;
import com.ldms.amortisation.service.AmortisationService;
import com.ldms.amortisation.validations.LoanValidations;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@OpenAPIDefinition(info = @Info(title = "Amortisation API", version = "1.0", description = "Amortisation Information"))
public class AmortisationController {

    @Autowired
    private AmortisationService service;

    @Operation(summary = "Post loan request details, like amount, deposit, rate of interest, months and balloon payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = " AmortisationSchedules",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AmortisationSchedule.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid entries supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "AmortisationSchedule not found",
                    content = @Content) })
    @PostMapping
    public AmortisationSchedule createSchedule(@RequestBody LoanDetails loanDetails) {
        LoanValidations.validateLoanDetails(loanDetails);
        return service.createSchedule(loanDetails);
    }

    @Operation(summary = "List all existing schedules")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Amortisation Schedules",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AmortisationSchedule.class)) }),
            @ApiResponse(responseCode = "404", description = "Amortisation Schedule not found",
                    content = @Content) })
    @GetMapping
    public List<AmortisationSchedule> listSchedules() {
        return service.listSchedules();
    }

    @Operation(summary = "List schedules by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Amortisation Schedules by Id",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AmortisationSchedule.class)) }),
            @ApiResponse(responseCode = "404", description = "Amortisation Schedule not found",
                    content = @Content) })
    @GetMapping("/{id}")
    public AmortisationSchedule getSchedule(@PathVariable Long id) {
        return service.getSchedule(id);
    }
}
