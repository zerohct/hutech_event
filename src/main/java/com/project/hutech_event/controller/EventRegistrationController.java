package com.project.hutech_event.controller;


import com.project.hutech_event.dto.response.EventRegistrationResponse;
import com.project.hutech_event.service.EventRegistrationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event_registry")
public class EventRegistrationController {
    @Autowired
    private EventRegistrationService eventRegistrationService;

    // API lấy tất cả
    @GetMapping
    public ResponseEntity<List<EventRegistrationResponse>> getAllEventRegistrations() {
        List<EventRegistrationResponse> response = eventRegistrationService.getAllEventRegistrations();
        return ResponseEntity.ok(response);
    }

    // API lấy theo event ID
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<EventRegistrationResponse>> getEventRegistrationsByEventId(@PathVariable Long eventId) {
        List<EventRegistrationResponse> response = eventRegistrationService.getEventRegistrationsByEventId(eventId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<String> toggleEventRegistration(@PathVariable Long eventId, @RequestHeader("Authorization") String authHeader) {
        // Trích xuất token từ authHeader
        String token = authHeader.substring(7);
        try {
            eventRegistrationService.toggleEventRegistration(eventId, token);
            return ResponseEntity.ok("Event registration toggled successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("Event or User not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }
}
