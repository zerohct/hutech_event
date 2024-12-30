package com.project.hutech_event.service;

import com.project.hutech_event.dto.request.CompanyRequest;
import com.project.hutech_event.dto.response.CompanyResponse;
import com.project.hutech_event.model.Company;
import com.project.hutech_event.repository.CompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    // Tạo mới Company
    public CompanyResponse createCompany(CompanyRequest requestDTO) {
        Company company = new Company();
        company.setName(requestDTO.getName());
        company.setDescription(requestDTO.getDescription());
        Company savedCompany = companyRepository.save(company);
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

    // Cập nhật Company
    public CompanyResponse updateCompany(Long companyId, CompanyRequest requestDTO) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with ID: " + companyId));
        company.setName(requestDTO.getName());
        company.setDescription(requestDTO.getDescription());
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
        return new CompanyResponse(company.getCompanyId(), company.getName(), company.getDescription());
    }

}
