package com.project.hutech_event.dto.response;

import com.project.hutech_event.enums.Gender;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserResponse {
    private Long userId;
    private String username;
    private String email;
    private String fullName;
    private String studentCode;
    private String faculty;
    private String clazz;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String avatarUrl;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
}
