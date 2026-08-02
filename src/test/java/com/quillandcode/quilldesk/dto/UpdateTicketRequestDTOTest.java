package com.quillandcode.quilldesk.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UpdateTicketRequestDTOTest {

    @Test
    void recordHoldsProvidedValues() {
        UpdateTicketRequestDTO dto = new UpdateTicketRequestDTO(3L, 7L, 11L);

        assertEquals(3L, dto.statusId());
        assertEquals(7L, dto.priorityId());
        assertEquals(11L, dto.assigneeId());
    }

    @Test
    void recordAllowsNullValues() {
        UpdateTicketRequestDTO dto = new UpdateTicketRequestDTO(null, null, null);

        assertNull(dto.statusId());
        assertNull(dto.priorityId());
        assertNull(dto.assigneeId());
    }
}
