package com.quillandcode.quilldesk.repository;

import com.quillandcode.quilldesk.entity.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketStatusRepository extends JpaRepository<TicketStatus, Long> {

    Optional<TicketStatus> findByName(String name);
}