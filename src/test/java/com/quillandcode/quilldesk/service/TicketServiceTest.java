package com.quillandcode.quilldesk.service;

import com.quillandcode.quilldesk.dto.CreateTicketRequestDTO;
import com.quillandcode.quilldesk.dto.TicketResponseDTO;
import com.quillandcode.quilldesk.entity.Ticket;
import com.quillandcode.quilldesk.entity.TicketPriority;
import com.quillandcode.quilldesk.entity.TicketStatus;
import com.quillandcode.quilldesk.entity.User;
import com.quillandcode.quilldesk.entity.UserRole;
import com.quillandcode.quilldesk.exception.ResourceNotFoundException;
import com.quillandcode.quilldesk.repository.TicketPriorityRepository;
import com.quillandcode.quilldesk.repository.TicketRepository;
import com.quillandcode.quilldesk.repository.TicketStatusRepository;
import com.quillandcode.quilldesk.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TicketStatusRepository statusRepository;

    @Mock
    private TicketPriorityRepository priorityRepository;

    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        ticketService = new TicketService(ticketRepository, userRepository, statusRepository, priorityRepository);
    }

    @Test
    void createTicket_persistsTicketWithDefaultStatusAndMapsToDto() {
        User creator = createUser(7L, "Mina", "Smith", "mina@example.com", UserRole.CUSTOMER);
        TicketPriority priority = createPriority(3L, "HIGH");
        TicketStatus openStatus = createStatus(1L, "OPEN");
        CreateTicketRequestDTO request = new CreateTicketRequestDTO("Bug", "Login issue", 3L, 7L);

        when(userRepository.findById(7L)).thenReturn(Optional.of(creator));
        when(priorityRepository.findById(3L)).thenReturn(Optional.of(priority));
        when(statusRepository.findById(1L)).thenReturn(Optional.of(openStatus));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TicketResponseDTO result = ticketService.createTicket(request);

        ArgumentCaptor<Ticket> ticketCaptor = ArgumentCaptor.forClass(Ticket.class);
        verify(ticketRepository).save(ticketCaptor.capture());
        Ticket savedTicket = ticketCaptor.getValue();

        assertNotNull(result);
        assertEquals("Bug", result.title());
        assertEquals("Login issue", result.name());
        assertEquals("OPEN", result.status());
        assertEquals("HIGH", result.priority());
        assertEquals("Mina Smith", result.creatorName());
        assertEquals("Unassigned", result.assigneeName());
        assertNotNull(result.createdDate());

        assertEquals("Bug", savedTicket.getTitle());
        assertEquals("Login issue", savedTicket.getName());
        assertSame(creator, savedTicket.getCreator());
        assertSame(priority, savedTicket.getPriority());
        assertSame(openStatus, savedTicket.getStatus());
        assertNotNull(savedTicket.getCreatedDate());
    }

    @Test
    void createTicket_throwsExceptionWhenCreatorMissing() {
        CreateTicketRequestDTO request = new CreateTicketRequestDTO("Bug", "Login issue", 3L, 7L);
        when(userRepository.findById(7L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> ticketService.createTicket(request)
        );

        assertEquals("User not found with ID: 7", exception.getMessage());
        verify(priorityRepository, never()).findById(any());
        verify(statusRepository, never()).findById(any());
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void createTicket_throwsExceptionWhenPriorityMissing() {
        User creator = createUser(7L, "Mina", "Smith", "mina@example.com", UserRole.CUSTOMER);
        CreateTicketRequestDTO request = new CreateTicketRequestDTO("Bug", "Login issue", 3L, 7L);

        when(userRepository.findById(7L)).thenReturn(Optional.of(creator));
        when(priorityRepository.findById(3L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> ticketService.createTicket(request)
        );

        assertEquals("Priority not found with ID: 3", exception.getMessage());
        verify(statusRepository, never()).findById(any());
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void createTicket_throwsExceptionWhenDefaultStatusMissing() {
        User creator = createUser(7L, "Mina", "Smith", "mina@example.com", UserRole.CUSTOMER);
        TicketPriority priority = createPriority(3L, "HIGH");
        CreateTicketRequestDTO request = new CreateTicketRequestDTO("Bug", "Login issue", 3L, 7L);

        when(userRepository.findById(7L)).thenReturn(Optional.of(creator));
        when(priorityRepository.findById(3L)).thenReturn(Optional.of(priority));
        when(statusRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> ticketService.createTicket(request)
        );

        assertEquals("Default OPEN status not found", exception.getMessage());
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void getTicketById_returnsMappedDtoWhenTicketExists() {
        User creator = createUser(10L, "Rosa", "Parks", "rosa@example.com", UserRole.SUPPORT_AGENT);
        User assignee = createUser(11L, "Nelson", "Mandela", "nelson@example.com", UserRole.SUPPORT_AGENT);
        TicketStatus status = createStatus(2L, "IN_PROGRESS");
        TicketPriority priority = createPriority(4L, "MEDIUM");
        Ticket ticket = createTicket(100L, "Feature", "Need support", creator, assignee, status, priority, LocalDateTime.of(2024, 1, 1, 10, 0));
        when(ticketRepository.findById(100L)).thenReturn(Optional.of(ticket));

        TicketResponseDTO result = ticketService.getTicketById(100L);

        assertEquals(100L, result.id());
        assertEquals("Feature", result.title());
        assertEquals("Need support", result.name());
        assertEquals("IN_PROGRESS", result.status());
        assertEquals("MEDIUM", result.priority());
        assertEquals("Rosa Parks", result.creatorName());
        assertEquals("Nelson Mandela", result.assigneeName());
        assertEquals(LocalDateTime.of(2024, 1, 1, 10, 0), result.createdDate());
    }

    @Test
    void getAllTickets_mapsAllTicketsToDtos() {
        User creator = createUser(13L, "Alicia", "Keys", "alicia@example.com", UserRole.CUSTOMER);
        TicketStatus status = createStatus(1L, "OPEN");
        TicketPriority priority = createPriority(1L, "LOW");
        Ticket first = createTicket(1L, "One", "First ticket", creator, null, status, priority, LocalDateTime.of(2024, 2, 1, 9, 0));
        Ticket second = createTicket(2L, "Two", "Second ticket", creator, creator, status, priority, LocalDateTime.of(2024, 2, 2, 9, 0));
        when(ticketRepository.findAll()).thenReturn(List.of(first, second));

        List<TicketResponseDTO> result = ticketService.getAllTickets();

        assertEquals(2, result.size());
        assertEquals("One", result.get(0).title());
        assertEquals("Unassigned", result.get(0).assigneeName());
        assertEquals("Alicia Keys", result.get(1).assigneeName());
    }

    @Test
    void getTicketById_throwsExceptionWhenTicketMissing() {
        when(ticketRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> ticketService.getTicketById(999L)
        );

        assertEquals("Ticket not found with ID: 999", exception.getMessage());
    }

    private User createUser(Long id, String firstName, String lastName, String email, UserRole role) {
        User user = new User();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setRole(role);
        return user;
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

    private Ticket createTicket(Long id, String title, String name, User creator, User assignee, TicketStatus status, TicketPriority priority, LocalDateTime createdDate) {
        Ticket ticket = new Ticket();
        ticket.setId(id);
        ticket.setTitle(title);
        ticket.setName(name);
        ticket.setCreator(creator);
        ticket.setAssignee(assignee);
        ticket.setStatus(status);
        ticket.setPriority(priority);
        ticket.setCreatedDate(createdDate);
        ticket.setUpdatedDate(createdDate.plusHours(1));
        ticket.setClosedDate(null);
        return ticket;
    }
}
