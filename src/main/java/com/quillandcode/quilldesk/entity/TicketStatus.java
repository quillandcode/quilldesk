package com.quillandcode.quilldesk.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ticket_statuses")
@Getter
@Setter
@NoArgsConstructor  // Needed by JPA
@AllArgsConstructor // Needed by @Builder
@Builder
public class TicketStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincrement on this column
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}