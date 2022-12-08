package rserenity.business.services.impl;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rserenity.Exception.ResourceNotFoundException;
import rserenity.business.dto.CompanyDto;
import rserenity.business.dto.VehicleDto;
import rserenity.business.services.CompanyServices;
import rserenity.business.services.VehicleServices;
import rserenity.data.entity.CompanyEntity;
import rserenity.data.entity.VehicleEntity;
import rserenity.data.repository.VehicleRepository;

import java.time.LocalDate;
import java.util.*;

@Service
public class VehicleServiceImpl implements VehicleServices {

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    CompanyServices companyServices;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<VehicleDto> getAll() {
        List<VehicleDto> vehicleDtos = new ArrayList<>();
        Iterable<VehicleEntity> entityList = vehicleRepository.findAll();
        for (VehicleEntity entity : entityList) {
            VehicleDto vehicleDto = entityToDto(entity);
            vehicleDtos.add(vehicleDto);
        }
        return vehicleDtos;
    }

    @Override
    public ResponseEntity<Map<String,Object>> createVehicle(VehicleDto vehicleDto){
        Map<String, Object> response = new HashMap<>();
        response.put("ok",false);
        VehicleEntity vehicleEntity = dtoToEntity(vehicleDto);
        if(vehicleDto.getAvailableUntil().isAfter(LocalDate.now())) {
            vehicleEntity = vehicleRepository.save(vehicleEntity);
            response.put("object", entityToDto(vehicleEntity));
            response.put("ok", true);
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<VehicleDto> getVehicleById(Long id) throws ResourceNotFoundException{
        VehicleEntity vehicleEntity =vehicleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Vehicle does not exist by given id " + id));;
        VehicleDto vehicleDto = entityToDto(vehicleEntity);
            return ResponseEntity.ok(vehicleDto);
    }

    @Override
    public List<VehicleDto> getVehiclesByCompanyId(Long companyId){
        List<VehicleEntity> vehicleEntities;
        List<VehicleDto> vehicleDtos = new ArrayList<>();
        try {
            vehicleEntities = vehicleRepository.findVehicleEntitiesByCompany(companyServices.dtoToEntity(companyServices.getCompanyById(companyId).getBody()));
            for(VehicleEntity entity : vehicleEntities){
                if(entity.getAvailableUntil().isAfter(LocalDate.now())){
                     vehicleDtos.add(entityToDto(entity));
                }
            }
            return vehicleDtos;
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public ResponseEntity<Map<String, Boolean>> deleteVehicle(Long id){
        return null;
    }

    @Override
    public VehicleDto entityToDto(VehicleEntity vehicleEntity) {
        VehicleDto vehicleDto = modelMapper.map(vehicleEntity, VehicleDto.class);
        vehicleDto.setCompanyId(vehicleEntity.getCompany().getId());
        return vehicleDto;
    }

    @Override
    public VehicleEntity dtoToEntity(VehicleDto vehicleDto) {
        VehicleEntity vehicleEntity = modelMapper.map(vehicleDto, VehicleEntity.class);
        try {
            vehicleEntity.setCompany(companyServices.dtoToEntity(companyServices.getCompanyById(vehicleDto.getCompanyId()).getBody()));
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return vehicleEntity;
}
}
