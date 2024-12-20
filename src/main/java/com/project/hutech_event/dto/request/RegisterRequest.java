package com.project.hutech_event.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String studentCode;
    private String faculty;
    private String clazz;
    private String phoneNumber;
    private String dateOfBirth; // Dạng chuỗi, ví dụ: "2000-01-01"
    private String gender;
}
