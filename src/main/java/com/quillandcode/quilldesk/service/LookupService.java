package com.quillandcode.quilldesk.service;

import com.quillandcode.quilldesk.dto.LookupItemDTO;
import com.quillandcode.quilldesk.repository.TicketPriorityRepository;
import com.quillandcode.quilldesk.repository.TicketStatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LookupService {
    private final TicketStatusRepository statusRepository;
    private final TicketPriorityRepository priorityRepository;

    public LookupService(TicketStatusRepository statusRepository, TicketPriorityRepository priorityRepository) {
        this.statusRepository = statusRepository;
        this.priorityRepository = priorityRepository;
    }

    @Transactional(readOnly = true)
    public List<LookupItemDTO> getAllStatuses() {
        return statusRepository
        .findAll()
        .stream()
        .map(status -> new LookupItemDTO(status.getId(), status.getName(), status.getName()))
        .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LookupItemDTO> getAllPriorities() {
        return priorityRepository
        .findAll()
        .stream()
        .map(priority -> new LookupItemDTO(priority.getId(), priority.getName(), priority.getName()))
        .collect(Collectors.toList());
    }
}