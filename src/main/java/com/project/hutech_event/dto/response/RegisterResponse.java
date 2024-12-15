package com.project.hutech_event.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class RegisterResponse {
    private String message;
    private String username;
    private String email;
}
