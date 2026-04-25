package com.ta.orders.controller;

import com.ta.orders.model.Customer;
import com.ta.orders.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/customer")
@Tag(name = "Customers", description = "APIs for managing customers")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new customer", description = "Creates a new customer with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Customer create(@RequestBody Customer customer) {
        log.debug("Saving customer {}", customer);
        return customerService.save(customer);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all customers", description = "Retrieves a list of all customers in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of customers retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<Customer> getCustomers() {
        log.debug("Getting list of customers");
        return customerService.getCustomers();
    }
}


