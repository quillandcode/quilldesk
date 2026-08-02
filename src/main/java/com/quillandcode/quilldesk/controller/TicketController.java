package com.quillandcode.quilldesk.controller;

import com.quillandcode.quilldesk.dto.CreateTicketRequestDTO;
import com.quillandcode.quilldesk.dto.TicketResponseDTO;
import com.quillandcode.quilldesk.exception.ResourceNotFoundException;
import com.quillandcode.quilldesk.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HTTP endpoints for ticket operations.
 *
 * This controller exposes a minimal support-ticket API used by the Swagger OpenAPI frontend.
 *
 * @author cwroberson
 */
@RestController
@RequestMapping("/api/v1/tickets")
@Tag(name = "Ticket API", description = "Operations for creating and retrieving support tickets")
public class TicketController {

    // Keep the controller focused on HTTP concerns so the service layer remains responsible for domain logic.

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Retrieve all tickets from the system.
     *
     * @return a list of ticket response DTOs
     */
    @GetMapping
    @Operation(summary = "List all tickets", description = "Returns all tickets currently stored in Quilldesk")
    public List<TicketResponseDTO> getAllTickets() {
        return ticketService.getAllTickets();
    }

    /**
     * Retrieve a ticket by its identifier.
     *
     * @param id the ticket id to fetch
     * @return the ticket response DTO
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get ticket by ID", description = "Returns the details for the specified ticket")
    public TicketResponseDTO getTicketById(@PathVariable Long id) {
        return ticketService.getTicketById(id);
    }

    /**
     * Create a new ticket.
     *
     * @param request the ticket create request payload
     * @return the created ticket with generated id and metadata
     */
    @PostMapping
    @Operation(summary = "Create a ticket", description = "Creates a new support ticket and returns the created resource")
    public ResponseEntity<TicketResponseDTO> createTicket(@Valid @RequestBody CreateTicketRequestDTO request) {
        // Returning 201 Created makes the API contract clear for clients creating a new resource.
        TicketResponseDTO createdTicket = ticketService.createTicket(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Validation failures are translated into a predictable response so callers understand what to fix.
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFound(ResourceNotFoundException ex) {
        // Domain-level missing-resource errors should appear as API 404s rather than leaking server exceptions.
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
