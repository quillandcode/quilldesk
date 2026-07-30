package com.quillandcode.quilldesk.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ticket_priorities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketPriority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}