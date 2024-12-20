package com.project.hutech_event.dto.response;


import com.project.hutech_event.enums.EventStatus;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponse {
    private Long eventId;
    private String title;
    private String description;
    private String eventType; // Tên của EventType
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime registrationDeadline;
    private String location;
    private Integer maxParticipants;
    private Integer currentParticipants;
    private EventStatus status;
    private String createdBy;
}
