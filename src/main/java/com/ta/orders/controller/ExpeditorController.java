package com.ta.orders.controller;

import com.ta.orders.dto.CustomerDto;
import com.ta.orders.dto.ExpeditorDto;
import com.ta.orders.service.ExpeditorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/v1/expeditors")
@Tag(name = "Expeditors", description = "APIs for managing expeditors")
public class ExpeditorController {

    private final ExpeditorService expeditorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new expeditor", description = "Creates a new expeditor with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Expeditor created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExpeditorDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ExpeditorDto create(@RequestBody ExpeditorDto expeditorDto) {
        log.debug("Creating expeditor: {}", expeditorDto);
        return expeditorService.create(expeditorDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all expeditors", description = "Retrieves a list of all expeditors in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of expeditors retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExpeditorDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<ExpeditorDto> getAll() {
        log.debug("Getting all expeditors");
        return expeditorService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get expeditor by ID", description = "Retrieves an expeditor by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expeditor retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExpeditorDto.class))),
            @ApiResponse(responseCode = "404", description = "Expeditor not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ExpeditorDto getById(
            @Parameter(description = "Expeditor ID", example = "1")
            @PathVariable Long id) {
        log.debug("Getting expeditor by id: {}", id);
        return expeditorService.getById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update expeditor", description = "Updates an existing expeditor with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expeditor updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExpeditorDto.class))),
            @ApiResponse(responseCode = "404", description = "Expeditor not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ExpeditorDto update(
            @Parameter(description = "Expeditor ID", example = "1")
            @PathVariable Long id,
            @RequestBody ExpeditorDto expeditorDto) {
        log.debug("Updating expeditor with id: {}", id);
        return expeditorService.update(id, expeditorDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete expeditor", description = "Deletes an expeditor by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Expeditor deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Expeditor not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public void delete(
            @Parameter(description = "Expeditor ID", example = "1")
            @PathVariable Long id) {
        log.debug("Deleting expeditor with id: {}", id);
        expeditorService.delete(id);
    }

    @GetMapping("/{id}/customers")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get customers by expeditor", description = "Retrieves all customers assigned to a specific expeditor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of customers retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerDto.class))),
            @ApiResponse(responseCode = "404", description = "Expeditor not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<CustomerDto> getCustomersByExpeditor(
            @Parameter(description = "Expeditor ID", example = "1")
            @PathVariable Long id) {
        log.debug("Getting customers for expeditor with id: {}", id);
        return expeditorService.getCustomersByExpeditorId(id);
    }
}

