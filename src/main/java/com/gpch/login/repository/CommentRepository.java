package com.gpch.login.repository;

import com.gpch.login.model.Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comments, Integer> {
    Page<Comments> findByEventId(int eventId, Pageable pageable);
    Page<Comments> findByUserId(int userId, Pageable pageable);
    Optional<Comments> findByIdAndEventId(int id, int eventId);
}