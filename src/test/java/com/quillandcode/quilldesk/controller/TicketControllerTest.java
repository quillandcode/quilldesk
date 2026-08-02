package com.quillandcode.quilldesk.controller;

import com.quillandcode.quilldesk.dto.CreateTicketRequestDTO;
import com.quillandcode.quilldesk.dto.TicketResponseDTO;
import com.quillandcode.quilldesk.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TicketControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        // A standalone MVC setup keeps these tests focused on controller behavior rather than application bootstrapping.
        mockMvc = MockMvcBuilders.standaloneSetup(new TicketController(ticketService)).build();
    }

    @Test
    void getAllTickets_returnsListOfTickets() throws Exception {
        TicketResponseDTO ticket = new TicketResponseDTO(
                1L,
                "Bug",
                "Login issue",
                "OPEN",
                "HIGH",
                "Jane Doe",
                "Unassigned",
                LocalDateTime.of(2024, 1, 1, 10, 0),
                null,
                null
        );
        when(ticketService.getAllTickets()).thenReturn(List.of(ticket));

        mockMvc.perform(get("/api/v1/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Bug"));

        verify(ticketService).getAllTickets();
    }

    @Test
    void getTicketById_returnsTicketWhenFound() throws Exception {
        TicketResponseDTO ticket = new TicketResponseDTO(
                2L,
                "Feature",
                "Add export",
                "IN_PROGRESS",
                "MEDIUM",
                "Jane Doe",
                "John Doe",
                LocalDateTime.of(2024, 1, 2, 8, 30),
                null,
                null
        );
        when(ticketService.getTicketById(2L)).thenReturn(ticket);

        mockMvc.perform(get("/api/v1/tickets/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Add export"));

        verify(ticketService).getTicketById(2L);
    }

    @Test
    void createTicket_returnsCreatedTicket() throws Exception {
        CreateTicketRequestDTO request = new CreateTicketRequestDTO("Bug", "Login issue", 3L, 7L);
        TicketResponseDTO created = new TicketResponseDTO(
                3L,
                "Bug",
                "Login issue",
                "OPEN",
                "HIGH",
                "Jane Doe",
                "Unassigned",
                LocalDateTime.of(2024, 1, 3, 9, 0),
                null,
                null
        );
        when(ticketService.createTicket(request)).thenReturn(created);

        mockMvc.perform(post("/api/v1/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Bug\",\"name\":\"Login issue\",\"priorityId\":3,\"creatorId\":7}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.title").value("Bug"));

        verify(ticketService).createTicket(request);
    }

    @Test
    void createTicket_returnsBadRequestForInvalidPayload() throws Exception {
        // Invalid input should be rejected with a client-facing 400 response before the service layer runs.
        mockMvc.perform(post("/api/v1/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTicket_returnsNotFoundWhenServiceThrowsResourceNotFound() throws Exception {
        // Missing related resources should surface as 404 responses so clients can distinguish not-found from bad input.
        CreateTicketRequestDTO request = new CreateTicketRequestDTO("Bug", "Login issue", 3L, 7L);
        when(ticketService.createTicket(any(CreateTicketRequestDTO.class)))
                .thenThrow(new com.quillandcode.quilldesk.exception.ResourceNotFoundException("User not found"));

        mockMvc.perform(post("/api/v1/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Bug\",\"name\":\"Login issue\",\"priorityId\":3,\"creatorId\":7}"))
                .andExpect(status().isNotFound());
    }
}
