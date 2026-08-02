package com.quillandcode.quilldesk.service;

import com.quillandcode.quilldesk.dto.LookupItemDTO;
import com.quillandcode.quilldesk.entity.TicketPriority;
import com.quillandcode.quilldesk.entity.TicketStatus;
import com.quillandcode.quilldesk.repository.TicketPriorityRepository;
import com.quillandcode.quilldesk.repository.TicketStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LookupServiceTest {

    @Mock
    private TicketStatusRepository statusRepository;

    @Mock
    private TicketPriorityRepository priorityRepository;

    private LookupService lookupService;

    @BeforeEach
    void setUp() {
        lookupService = new LookupService(statusRepository, priorityRepository);
    }

    @Test
    void getAllStatuses_mapsStatusesToLookupItems() {
        TicketStatus open = createStatus(1L, "OPEN");
        TicketStatus inProgress = createStatus(2L, "IN_PROGRESS");
        when(statusRepository.findAll()).thenReturn(List.of(open, inProgress));

        List<LookupItemDTO> result = lookupService.getAllStatuses();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals("OPEN", result.get(0).name());
        assertEquals("OPEN", result.get(0).description());
        assertEquals(2L, result.get(1).id());
        assertEquals("IN_PROGRESS", result.get(1).name());
    }

    @Test
    void getAllPriorities_mapsPrioritiesToLookupItems() {
        TicketPriority low = createPriority(1L, "LOW");
        TicketPriority high = createPriority(2L, "HIGH");
        when(priorityRepository.findAll()).thenReturn(List.of(low, high));

        List<LookupItemDTO> result = lookupService.getAllPriorities();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals("LOW", result.get(0).name());
        assertEquals("LOW", result.get(0).description());
        assertEquals(2L, result.get(1).id());
        assertEquals("HIGH", result.get(1).name());
    }

    private TicketStatus createStatus(Long id, String name) {
        TicketStatus status = new TicketStatus();
        status.setId(id);
        status.setName(name);
        return status;
    }

    private TicketPriority createPriority(Long id, String name) {
        TicketPriority priority = new TicketPriority();
        priority.setId(id);
        priority.setName(name);
        return priority;
    }
}
