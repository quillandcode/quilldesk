package com.quillandcode.quilldesk.service;

import com.quillandcode.quilldesk.dto.CreateTicketRequestDTO;
import com.quillandcode.quilldesk.dto.TicketResponseDTO;
import com.quillandcode.quilldesk.entity.Ticket;
import com.quillandcode.quilldesk.entity.TicketPriority;
import com.quillandcode.quilldesk.entity.TicketStatus;
import com.quillandcode.quilldesk.entity.User;
import com.quillandcode.quilldesk.exception.ResourceNotFoundException;
import com.quillandcode.quilldesk.repository.TicketPriorityRepository;
import com.quillandcode.quilldesk.repository.TicketRepository;
import com.quillandcode.quilldesk.repository.TicketStatusRepository;
import com.quillandcode.quilldesk.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final TicketStatusRepository statusRepository;
    private final TicketPriorityRepository priorityRepository;

    public TicketService(
        TicketRepository ticketRepository,
        UserRepository userRepository,
        TicketStatusRepository statusRepository,
        TicketPriorityRepository priorityRepository) {
        
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.statusRepository = statusRepository;
        this.priorityRepository = priorityRepository;
    }

    @Transactional
    public TicketResponseDTO createTicket(CreateTicketRequestDTO dto) {
        User creator = userRepository.findById(dto.creatorId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + dto.creatorId()));

        TicketPriority priority = priorityRepository.findById(dto.priorityId())
                .orElseThrow(() -> new ResourceNotFoundException("Priority not found with ID: " + dto.priorityId()));

        // Default new tickets to OPEN status
        TicketStatus status = statusRepository.findById(1L)
                .orElseThrow(() -> new ResourceNotFoundException("Default OPEN status not found"));

        Ticket ticket = new Ticket();
        ticket.setTitle(dto.title());
        ticket.setName(dto.name());
        ticket.setCreator(creator);
        ticket.setPriority(priority);
        ticket.setStatus(status);
        ticket.setCreatedDate(LocalDateTime.now());

        Ticket savedTicket = ticketRepository.save(ticket);
        return mapToDTO(savedTicket);
    }

    @Transactional(readOnly = true)
    public TicketResponseDTO getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ID: " + id));
        return mapToDTO(ticket);
    }

    @Transactional(readOnly = true)
    public List<TicketResponseDTO> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Helper method to flatten the Entity into a clean DTO
    private TicketResponseDTO mapToDTO(Ticket ticket) {
        return new TicketResponseDTO(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getName(),
                ticket.getStatus().getName(),
                ticket.getPriority().getName(),
                ticket.getCreator().getFirstName() + " " + ticket.getCreator().getLastName(),
                ticket.getAssignee() != null ? ticket.getAssignee().getFirstName() + " " + ticket.getAssignee().getLastName() : "Unassigned",
                ticket.getCreatedDate(),
                ticket.getUpdatedDate(),
                ticket.getClosedDate()
        );
    }
}