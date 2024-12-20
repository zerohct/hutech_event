package com.project.hutech_event.controller;


import com.project.hutech_event.dto.request.EventRequest;
import com.project.hutech_event.dto.response.EventResponse;
import com.project.hutech_event.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {
    @Autowired
    private  EventService eventService;


    @PostMapping()
    public ResponseEntity<?> createEvent(@RequestBody EventRequest request,@RequestHeader(value = "Authorization", required = false) String authHeader) {
        // Kiểm tra xem authHeader có tồn tại hay không
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Bạn không có quyền thực hiện hành động này!");
        }

        // Trích xuất token từ authHeader
        String token = authHeader.substring(7);

        // Gọi service để xử lý yêu cầu
        return ResponseEntity.ok(eventService.createEvent(request, token));
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }


    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getEventById(eventId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable Long eventId, @RequestBody EventRequest request) {
        return ResponseEntity.ok(eventService.updateEvent(eventId, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }

}
