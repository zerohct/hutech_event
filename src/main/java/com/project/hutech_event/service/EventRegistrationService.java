package com.project.hutech_event.service;

import com.project.hutech_event.dto.response.EventRegistrationResponse;
import com.project.hutech_event.model.EventRegistration;
import com.project.hutech_event.repository.EventRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventRegistrationService {

    @Autowired
    private EventRegistrationRepository eventRegistrationRepository;

    // Lấy tất cả
    public List<EventRegistrationResponse> getAllEventRegistrations() {
        List<EventRegistration> registrations = eventRegistrationRepository.findAll();
        return registrations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy theo event ID
    public List<EventRegistrationResponse> getEventRegistrationsByEventId(Long eventId) {
        List<EventRegistration> registrations = eventRegistrationRepository.findByEvent_EventId(eventId);
        return registrations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Hàm chuyển đổi Entity -> DTO
    private EventRegistrationResponse convertToDTO(EventRegistration registration) {
        return new EventRegistrationResponse(
                registration.getRegistrationId(),
                registration.getEvent().getEventId(),
                registration.getEvent().getTitle(), // Lấy tên sự kiện từ Entity Event
                registration.getUser().getUserId(),
                registration.getRegistrationDate()
        );
    }
}
