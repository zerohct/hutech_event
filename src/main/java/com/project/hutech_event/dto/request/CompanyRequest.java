package com.project.hutech_event.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRequest {
    private String name;
    private String description;
    private MultipartFile image;
}
