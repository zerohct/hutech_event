package com.project.hutech_event.service;

import com.project.hutech_event.Config.JwtUtils;
import com.project.hutech_event.dto.response.EventRegistrationResponse;
import com.project.hutech_event.model.Event;
import com.project.hutech_event.model.EventRegistration;
import com.project.hutech_event.model.User;
import com.project.hutech_event.repository.EventRegistrationRepository;
import com.project.hutech_event.repository.EventRepository;
import com.project.hutech_event.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventRegistrationService {

    @Autowired
    private EventRegistrationRepository eventRegistrationRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

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

    public void toggleEventRegistration(Long eventId, String token) {
        // Lấy userId từ token
        Long userId = jwtUtils.getUserIdFromJwtToken(token); // Lấy userId từ JWT

        // Kiểm tra xem người dùng đã đăng ký sự kiện chưa
        Optional<EventRegistration> existingRegistration = eventRegistrationRepository.findByEventIdAndUserId(eventId, userId);

        if (existingRegistration.isPresent()) {
            // Nếu đã đăng ký, xóa bản ghi
            eventRegistrationRepository.delete(existingRegistration.get());
        } else {
            // Nếu chưa đăng ký, tạo mới bản ghi
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new EntityNotFoundException("Event not found with ID: " + eventId));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

            EventRegistration newRegistration = new EventRegistration();
            newRegistration.setEvent(event);
            newRegistration.setUser(user);
            eventRegistrationRepository.save(newRegistration);
        }
    }

}
