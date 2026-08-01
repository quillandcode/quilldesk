package com.quillandcode.quilldesk.dto;

public record UserResponseDTO(
    Long id,
    String firstName,
    String lastName,
    String email,
    String role
) {}