package com.project.hutech_event.dto.request;


import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    private String username;
    private String password;

}
