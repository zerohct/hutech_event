package com.project.hutech_event.controller;


import com.project.hutech_event.dto.response.EventRegistrationResponse;
import com.project.hutech_event.service.EventRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/event_registry")
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
}
