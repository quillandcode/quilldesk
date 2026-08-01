package com.quillandcode.quilldesk.dto;

import java.time.LocalDateTime;

public record TicketResponseDTO(
    Long id,
    String title,
    String name,
    String status,
    String priority,
    String creatorName,
    String assigneeName,
    LocalDateTime createdDate,
    LocalDateTime updatedDate,
    LocalDateTime closedDate
) {}