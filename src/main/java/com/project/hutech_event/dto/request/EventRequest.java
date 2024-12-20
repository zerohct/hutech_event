package com.project.hutech_event.dto.request;


import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {
    private String title;
    private String description;
    private Long typeId; // ID của EventType
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime registrationDeadline;
    private String location;
    private Integer maxParticipants;
}
