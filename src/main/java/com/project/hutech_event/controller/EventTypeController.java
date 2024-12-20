package com.project.hutech_event.controller;

import com.project.hutech_event.model.EventType;
import com.project.hutech_event.service.EventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event-types")
public class EventTypeController {

    @Autowired
    private EventTypeService eventTypeService;

    // Lấy danh sách tất cả EventTypes
    @GetMapping
    public List<EventType> getAllEventTypes() {
        return eventTypeService.getAllEventTypes();
    }

    // Lấy một EventType theo ID
    @GetMapping("/{id}")
    public ResponseEntity<EventType> getEventTypeById(@PathVariable Long id) {
        return eventTypeService.getEventTypeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Tạo một EventType mới
    @PostMapping
    public ResponseEntity<EventType> createEventType(@RequestBody EventType eventType) {
        // Validate that typeName is not null or empty before saving
        if (eventType == null || eventType.getTypeName() == null || eventType.getTypeName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            EventType savedEventType = eventTypeService.saveEventType(eventType);
            return ResponseEntity.ok(savedEventType);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Cập nhật một EventType
    @PutMapping("/{id}")
    public ResponseEntity<EventType> updateEventType(@PathVariable Long id, @RequestBody EventType eventType) {
        if (eventTypeService.getEventTypeById(id).isPresent()) {
            eventType.setTypeId(id);
            return ResponseEntity.ok(eventTypeService.saveEventType(eventType));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Xóa một EventType
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventType(@PathVariable Long id) {
        if (eventTypeService.getEventTypeById(id).isPresent()) {
            eventTypeService.deleteEventType(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

