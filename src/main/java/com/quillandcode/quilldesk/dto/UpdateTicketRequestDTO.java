package com.quillandcode.quilldesk.dto;

public record UpdateTicketRequestDTO(
    Long statusId,
    Long priorityId,
    Long assigneeId
) {}