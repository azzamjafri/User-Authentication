package com.gpch.login.controller;

import com.gpch.login.model.Events;
import com.gpch.login.exception.ResourceNotFoundException;
import com.gpch.login.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class EventsController {
   @Autowired
   private EventRepository eventRepository;

    @GetMapping("/events")
    public Page<Events> getAllEvents(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    @PostMapping("/events")
    public Events createEvent(@Valid @RequestBody Events event) {
        return eventRepository.save(event);
    }

    @PutMapping("/events/{eventId}")
    public Events updateEvent(@PathVariable int eventId, @Valid @RequestBody Events eventRequest) {
        return eventRepository.findById(eventId).map(post -> {
            post.setTitle(eventRequest.getTitle());
            post.setDescription(eventRequest.getDescription());
            post.setPlace(eventRequest.getPlace());
            post.setPrice(eventRequest.getPrice());
            return eventRepository.save(post);

        }).orElseThrow(() -> new ResourceNotFoundException("EventId " + eventId + " not found"));
    }


    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<?> deleteEvent(@PathVariable int eventId) {
        return eventRepository.findById(eventId).map(post -> {
            eventRepository.delete(post);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("eventId " + eventId + " not found"));
    }

}
