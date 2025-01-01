package com.project.hutech_event.dto.request;

import com.project.hutech_event.enums.Gender;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String studentCode;
    private String faculty;
    private String clazz;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private Gender gender;
    private MultipartFile avatarUrl;
}
