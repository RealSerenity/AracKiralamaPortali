package rserenity.business.services;

import org.springframework.http.ResponseEntity;
import rserenity.Exception.ResourceNotFoundException;
import rserenity.business.dto.CompanyDto;
import rserenity.data.entity.CompanyEntity;

import java.util.List;
import java.util.Map;

public interface CompanyServices {
    public List<CompanyDto> getAll();

    public CompanyDto createCompany(CompanyDto companyDto);

    public ResponseEntity<CompanyDto> getCompanyById(Long id) throws ResourceNotFoundException;

    public ResponseEntity<Map<String,Boolean>> deleteCompany(Long id);

    public CompanyDto entityToDto(CompanyEntity companyEntity);
    public CompanyEntity dtoToEntity(CompanyDto companyDto);
}
