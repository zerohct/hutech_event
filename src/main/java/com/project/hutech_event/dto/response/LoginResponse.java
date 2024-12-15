package com.project.hutech_event.dto.response;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private boolean authenticated;


}
