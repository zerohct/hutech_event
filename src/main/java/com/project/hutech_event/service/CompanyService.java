package com.project.hutech_event.service;

import com.project.hutech_event.dto.request.CompanyRequest;
import com.project.hutech_event.dto.response.CompanyResponse;
import com.project.hutech_event.model.Company;
import com.project.hutech_event.repository.CompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public CompanyResponse createCompany(CompanyRequest requestDTO) {
        Company company = new Company();

        // Gán các giá trị thông thường
        company.setName(requestDTO.getName());
        company.setDescription(requestDTO.getDescription());

        // Xử lý file ảnh (nếu có)
        MultipartFile imageFile = requestDTO.getImage();
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // Chuyển đổi file ảnh sang chuỗi Base64
                byte[] imageBytes = imageFile.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);

                // Gán Base64 vào thuộc tính image
                company.setImage(base64Image);
            } catch (Exception e) {
                throw new RuntimeException("Error processing image file", e);
            }
        }

        // Lưu công ty vào database
        Company savedCompany = companyRepository.save(company);

        // Chuyển đổi và trả về DTO
        return convertToDTO(savedCompany);
    }

    // Lấy tất cả Company
    public List<CompanyResponse> getAllCompanies() {
        return companyRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy Company theo ID
    public CompanyResponse getCompanyById(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with ID: " + companyId));
        return convertToDTO(company);
    }

    public CompanyResponse updateCompany(Long companyId, CompanyRequest requestDTO) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with ID: " + companyId));

        // Chỉ cập nhật nếu giá trị mới không trống hoặc khác với giá trị cũ
        if (requestDTO.getName() != null && !requestDTO.getName().isEmpty() && !requestDTO.getName().equals(company.getName())) {
            company.setName(requestDTO.getName());
        }

        if (requestDTO.getDescription() != null && !requestDTO.getDescription().isEmpty() && !requestDTO.getDescription().equals(company.getDescription())) {
            company.setDescription(requestDTO.getDescription());
        }

        MultipartFile imageFile = requestDTO.getImage();
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                if (imageFile.getSize() > 5 * 1024 * 1024) { // 5MB limit
                    throw new RuntimeException("File size exceeds 5MB limit.");
                }
                byte[] imageBytes = imageFile.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                company.setImage(base64Image); // Cập nhật Base64 image nếu có thay đổi
            } catch (Exception e) {
                throw new RuntimeException("Error processing image file", e);
            }
        }

        Company updatedCompany = companyRepository.save(company);
        return convertToDTO(updatedCompany);
    }

    // Xóa Company
    public void deleteCompany(Long companyId) {
        if (!companyRepository.existsById(companyId)) {
            throw new EntityNotFoundException("Company not found with ID: " + companyId);
        }
        companyRepository.deleteById(companyId);
    }

    // Chuyển đổi Entity -> DTO
    private CompanyResponse convertToDTO(Company company) {
        return new CompanyResponse(company.getCompanyId(), company.getName(), company.getDescription(),company.getImage());
    }

}
