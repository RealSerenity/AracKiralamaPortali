package rserenity.business.services;

import org.springframework.http.ResponseEntity;
import rserenity.Exception.ResourceNotFoundException;
import rserenity.business.dto.VehicleDto;
import rserenity.data.entity.VehicleEntity;

import java.util.List;
import java.util.Map;

public interface VehicleServices {
    public List<VehicleDto> getAll();

    public ResponseEntity<Map<String,Object>> createVehicle(VehicleDto vehicleDto);

    public ResponseEntity<VehicleDto> getVehicleById(Long id) throws ResourceNotFoundException;

    public List<VehicleDto> getVehiclesByCompanyId(Long companyId);

    public ResponseEntity<Map<String,Boolean>> deleteVehicle(Long id);

    public VehicleDto entityToDto(VehicleEntity vehicleEntity);
    public VehicleEntity dtoToEntity(VehicleDto vehicleDto);
}
