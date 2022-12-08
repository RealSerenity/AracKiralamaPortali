package rserenity.business.services.impl;

import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rserenity.Exception.ResourceNotFoundException;
import rserenity.business.dto.CompanyDto;
import rserenity.business.services.CompanyServices;
import rserenity.data.entity.CompanyEntity;
import rserenity.data.repository.CompanyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class CompanyServiceImpl  implements CompanyServices {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CompanyDto> getAll() {
        List<CompanyDto> companyDtos = new ArrayList<>();
        Iterable<CompanyEntity> entityList= companyRepository.findAll();
        for(CompanyEntity entity : entityList){
            CompanyDto companyDto = entityToDto(entity);
            companyDtos.add(companyDto);
        }
        return companyDtos;
    }

    @Override
    public CompanyDto createCompany(CompanyDto companyDto) {
        CompanyEntity companyEntity = dtoToEntity(companyDto);
        System.out.println("New Company created");
        companyRepository.save(companyEntity);
        return companyDto;
    }

    @Override
    public ResponseEntity<CompanyDto> getCompanyById(Long id) throws ResourceNotFoundException{
        CompanyEntity companyEntity = companyRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Company does not exist by given id " + id));
        CompanyDto companyDto = entityToDto(companyEntity);
        return ResponseEntity.ok(companyDto);
    }

    @Override
    public ResponseEntity<Map<String, Boolean>> deleteCompany(Long id){
        return null;
    }

    @Override
    public CompanyDto entityToDto(CompanyEntity companyEntity) {
        return modelMapper.map(companyEntity, CompanyDto.class);
    }

    @Override
    public CompanyEntity dtoToEntity(CompanyDto companyDto) {
        return modelMapper.map(companyDto, CompanyEntity.class);
    }
}
