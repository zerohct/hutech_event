package com.project.hutech_event.service;

import com.project.hutech_event.Config.JwtUtils;
import com.project.hutech_event.dto.request.EventRequest;
import com.project.hutech_event.dto.response.EventResponse;
import com.project.hutech_event.model.Event;
import com.project.hutech_event.model.EventType;
import com.project.hutech_event.model.User;
import com.project.hutech_event.repository.EventRepository;
import com.project.hutech_event.repository.EventTypeRepository;
import com.project.hutech_event.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventTypeRepository eventTypeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtils jwtUtils;

    public EventResponse createEvent(EventRequest request,String token) {

        String username = jwtUtils.getUsernameFromJwtToken(token);

        // Lấy thông tin EventType từ database
        EventType eventType = eventTypeRepository.findById(request.getTypeId())
                .orElseThrow(() -> new RuntimeException("Loại sự kiện không tồn tại!"));

        // Lấy thông tin User tạo sự kiện
        User createdBy = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Người tạo sự kiện không tồn tại!"));

        // Tạo sự kiện
        Event event = new Event();
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setEventType(eventType);
        event.setStartDate(request.getStartDate());
        event.setEndDate(request.getEndDate());
        event.setRegistrationDeadline(request.getRegistrationDeadline());
        event.setLocation(request.getLocation());
        event.setMaxParticipants(request.getMaxParticipants());
        event.setCurrentParticipants(0);
        event.setCreatedBy(createdBy);

        // Lưu vào database
        Event savedEvent = eventRepository.save(event);

        return mapToResponse(savedEvent);
    }

    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public EventResponse getEventById(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sự kiện!"));
        return mapToResponse(event);
    }

    public EventResponse updateEvent(Long eventId, EventRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sự kiện!"));

        EventType eventType = eventTypeRepository.findById(request.getTypeId())
                .orElseThrow(() -> new RuntimeException("Loại sự kiện không tồn tại!"));

        // Cập nhật thông tin
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setEventType(eventType);
        event.setStartDate(request.getStartDate());
        event.setEndDate(request.getEndDate());
        event.setRegistrationDeadline(request.getRegistrationDeadline());
        event.setLocation(request.getLocation());
        event.setMaxParticipants(request.getMaxParticipants());

        Event updatedEvent = eventRepository.save(event);
        return mapToResponse(updatedEvent);
    }

    public void deleteEvent(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new RuntimeException("Không tìm thấy sự kiện!");
        }
        eventRepository.deleteById(eventId);
    }

    private EventResponse mapToResponse(Event event) {
        return new EventResponse(
                event.getEventId(),
                event.getTitle(),
                event.getDescription(),
                event.getEventType().getTypeName(),
                event.getStartDate(),
                event.getEndDate(),
                event.getRegistrationDeadline(),
                event.getLocation(),
                event.getMaxParticipants(),
                event.getCurrentParticipants(),
                event.getStatus(),
                event.getCreatedBy().getUsername()
        );
    }


}
