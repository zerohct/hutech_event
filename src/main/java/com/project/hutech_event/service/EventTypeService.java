package com.project.hutech_event.service;

import com.project.hutech_event.model.EventType;
import com.project.hutech_event.repository.EventTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventTypeService {

    @Autowired
    private EventTypeRepository eventTypeRepository;

    // Lấy danh sách tất cả các EventType
    public List<EventType> getAllEventTypes() {
        return eventTypeRepository.findAll();
    }

    // Lấy một EventType theo ID
    public Optional<EventType> getEventTypeById(Long id) {
        return eventTypeRepository.findById(id);
    }

    // Tạo hoặc cập nhật một EventType
    public EventType saveEventType(EventType eventType) {
        if (eventType == null || eventType.getTypeName() == null || eventType.getTypeName().trim().isEmpty()) {
            throw new IllegalArgumentException("Event type name cannot be null or empty");
        }
        return eventTypeRepository.save(eventType);
    }

    // Xóa một EventType theo ID
    public void deleteEventType(Long id) {
        eventTypeRepository.deleteById(id);
    }
}
