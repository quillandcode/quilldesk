-- Seed Ticket Statuses
INSERT INTO ticket_statuses (id, name) VALUES (1, 'OPEN');
INSERT INTO ticket_statuses (id, name) VALUES (2, 'IN_PROGRESS');
INSERT INTO ticket_statuses (id, name) VALUES (3, 'RESOLVED');
INSERT INTO ticket_statuses (id, name) VALUES (4, 'CLOSED');

-- Seed Ticket Priorities
INSERT INTO ticket_priorities (id, name) VALUES (1, 'LOW');
INSERT INTO ticket_priorities (id, name) VALUES (2, 'MEDIUM');
INSERT INTO ticket_priorities (id, name) VALUES (3, 'HIGH');
INSERT INTO ticket_priorities (id, name) VALUES (4, 'URGENT');