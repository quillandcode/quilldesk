package com.quillandcode.quilldesk.repository;

import com.quillandcode.quilldesk.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Useful for fetching comments in chronological order for a single ticket view
    List<Comment> findByTicketIdOrderByCreatedDateAsc(Long ticketId);

    // Useful for paginated comment lists
    Page<Comment> findByTicketId(Long ticketId, Pageable pageable);
}