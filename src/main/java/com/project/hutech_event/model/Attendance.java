package com.project.hutech_event.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "Attendance")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    @ManyToOne
    @JoinColumn(name = "registration_id", nullable = false)
    private EventRegistration registration;

    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String ipAddress;
    private String deviceInfo;

}
