package com.project.hutech_event.controller;

import com.project.hutech_event.dto.request.CompanyRequest;
import com.project.hutech_event.dto.response.CompanyResponse;
import com.project.hutech_event.service.CompanyService;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    // Tạo mới Company
    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(@ModelAttribute @Valid CompanyRequest requestDTO) {
        CompanyResponse response = companyService.createCompany(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Lấy tất cả Company
    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAllCompanies() {
        List<CompanyResponse> response = companyService.getAllCompanies();
        return ResponseEntity.ok(response);
    }

    // Lấy Company theo ID
    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Long companyId) {
        CompanyResponse response = companyService.getCompanyById(companyId);
        return ResponseEntity.ok(response);
    }

    // Cập nhật Company
    @PutMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> updateCompany(
            @PathVariable Long companyId,
            @ModelAttribute @Valid CompanyRequest requestDTO) {
        CompanyResponse response = companyService.updateCompany(companyId, requestDTO);
        return ResponseEntity.ok(response);
    }

    // Xóa Company
    @DeleteMapping("/{companyId}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long companyId) {
        companyService.deleteCompany(companyId);
        return ResponseEntity.noContent().build();
    }
}
