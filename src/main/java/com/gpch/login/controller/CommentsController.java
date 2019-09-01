package com.gpch.login.controller;
import com.gpch.login.exception.ResourceNotFoundException;
import com.gpch.login.model.Comments;
import com.gpch.login.model.Events;
import com.gpch.login.model.User;
import com.gpch.login.repository.CommentRepository;
import com.gpch.login.repository.EventRepository;
import com.gpch.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;
@RestController
public class CommentsController {

        @Autowired
        private CommentRepository commentRepository;

        @Autowired

        private EventRepository eventRepository;

        @Autowired
        public UserRepository userRepository;

        @GetMapping("/events/{eventId}/comments")
        public Page<Comments> getAllCommentsByEventId(@PathVariable (value = "eventId") int eventId,
                                                      Pageable pageable) {
                return commentRepository.findByEventId(eventId, pageable);
        }

        @GetMapping("/users/{userId}/comments")
        public Page<Comments> getAllCommentsByUserId(@PathVariable (value = "userId") int userId,
                                                      Pageable pageable) {
                return commentRepository.findByUserId(userId, pageable);
        }

        @PostMapping("/events/{eventId}/users/{userId}/comments")
        public Comments createComment(@PathVariable (value = "eventId") int eventId,
                                      @PathVariable (value = "userId") int userId,
                                     @Valid @RequestBody Comments comment) {
                return eventRepository.findById(eventId).map(event -> {
                        if(!userRepository.existsById(userId)) {
                                throw new ResourceNotFoundException("userId " + userId + " not found");
                        }
                        comment.setUser(userRepository.findById(userId));
                        comment.setEvent(event);
                        return commentRepository.save(comment);
                }).orElseThrow(() -> new ResourceNotFoundException("EventId " + eventId + " not found"));
        }

        @PutMapping("/events/{eventId}/comments/{commentId}")
        public Comments updateComment(@PathVariable (value = "eventId") int eventId,
                                     @PathVariable (value = "commentId") int commentId,
                                     @Valid @RequestBody Comments commentRequest) {
                if(!eventRepository.existsById(eventId)) {
                        throw new ResourceNotFoundException("EventId " + eventId + " not found");
                }

                return commentRepository.findById(commentId).map(comment -> {
                        comment.setText(commentRequest.getText());
                        return commentRepository.save(comment);
                }).orElseThrow(() -> new ResourceNotFoundException("CommentId " + commentId + "not found"));
        }

        @DeleteMapping("/events/{eventId}/comments/{commentId}")
        public ResponseEntity<?> deleteComment(@PathVariable (value = "eventId") int eventId,
                                               @PathVariable (value = "commentId") int commentId) {
                return commentRepository.findByIdAndEventId(commentId, eventId).map(comment -> {
                        commentRepository.delete(comment);
                        return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " + commentId + " and eventId " + eventId));
        }
}