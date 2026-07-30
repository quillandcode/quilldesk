package com.quillandcode.quilldesk.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTicketRequestDTO(
    @NotBlank(message = "Title is required") 
    String title,
    
    @NotBlank(message = "Description is required") 
    String description,
    
    @NotNull(message = "Priority ID is required") 
    Long priorityId,
    
    @NotNull(message = "Creator ID is required") 
    Long creatorId
) {}
