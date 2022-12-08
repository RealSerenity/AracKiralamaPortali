package rserenity.business.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rserenity.Exception.ResourceNotFoundException;
import rserenity.business.dto.ReservationDto;
import rserenity.business.services.ReservationServices;
import rserenity.business.services.UserServices;
import rserenity.business.services.VehicleServices;
import rserenity.data.entity.ReservationEntity;
import rserenity.data.entity.UserEntity;
import rserenity.data.entity.VehicleEntity;
import rserenity.data.repository.ReservationRepository;

import java.time.LocalDate;
import java.util.*;

@Service
public class ReservationServiceImpl implements ReservationServices {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    UserServices userServices;

    @Autowired
    VehicleServices vehicleServices;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<ReservationDto> getAll() {
        List<ReservationDto> reservationDtos = new ArrayList<>();
        Iterable<ReservationEntity> entityList= reservationRepository.findAll();
        for(ReservationEntity entity : entityList){
            ReservationDto reservationDto = entityToDto(entity);
            reservationDtos.add(reservationDto);
        }
        return reservationDtos;
    }

    @Override
    public Map<String, Object> createReservation(ReservationDto reservationDto){
        Map<String, Object> response = new HashMap<>();
        response.put("ok", false);
        if(reservationDto.getReservationStartDate().isBefore(LocalDate.now())){
            response.put("gecersizDate", true);
            return response;
        }
        if (checkDateIsAvailable(
                vehicleServices.dtoToEntity(vehicleServices.getVehicleById(reservationDto.getVehicleId()).getBody()),
                reservationDto.getReservationStartDate())
        ) {

            ReservationEntity reservationEntity = null;
            try {
                reservationEntity = dtoToEntity(reservationDto);
                reservationEntity = reservationRepository.save(reservationEntity);
            } catch (ResourceNotFoundException e) {
                System.out.println(e.getMessage());
            }
            response.put("object",reservationEntity);
            response.put("ok", true);
            return response;
        }
        return response;
    }

    @Override
    public List<ReservationDto> getUserReservations(Long userId){
        List<ReservationDto> dtos = new ArrayList<>();
        try {
            List<ReservationEntity> reservationEntities = reservationRepository.findReservationEntitiesByUserOrderByCreatedByDesc(userServices.dtoToEntity(
                    userServices.getUserById(userId).getBody()));
            for (ReservationEntity entity: reservationEntities) {
                    if(entity.getReservationStartDate().isAfter(LocalDate.now())){
                        dtos.add(entityToDto(entity));
                    }
            }
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return dtos;
    }

    @Override
    public ResponseEntity<ReservationDto> getReservationById(Long id) throws ResourceNotFoundException{
        ReservationEntity reservationEntity = reservationRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Reservation does not exist by given id " + id));
        ReservationDto reservationDto = entityToDto(reservationEntity);
        return ResponseEntity.ok(reservationDto);
    }

    @Override
    public ResponseEntity<Map<String, Boolean>> deleteReservation(Long id){
        ReservationEntity entity = null;
        try {
            entity = dtoToEntity(getReservationById(id).getBody());
            // Compare 1 day before reservationDate with now
            if (LocalDate.now().isBefore(entity.getReservationStartDate().minusDays(1L))){
                // cancelable
                reservationRepository.delete(entity);
                Map<String, Boolean> response = new HashMap<>();
                response.put("canceled", Boolean.TRUE);
                return ResponseEntity.ok(response);
            }
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }
        Map<String, Boolean> response = new HashMap<>();
        response.put("canceled", Boolean.FALSE);
        return ResponseEntity.ok(response);
    }

//    @Override
//    public boolean checkDateIsAvailable(VehicleEntity vehicle, LocalDate date) {
//        System.out.println(reservationRepository.findReservationEntityByVehicleAndReservationStartDate(vehicle,date));
//        return reservationRepository.findReservationEntityByVehicleAndReservationStartDate(vehicle,date) == null;
//    }

    @Override
    public boolean checkDateIsAvailable(VehicleEntity vehicle, LocalDate date) {
        for (ReservationEntity entity: reservationRepository.findReservationEntitiesByReservationStartDate(date)) {
            if(entity.getVehicle().getId().equals(vehicle.getId())){
                return false;
            }
        }
        return true;
    }

    @Override
    public ReservationDto entityToDto(ReservationEntity reservationEntity) {
        ReservationDto reservationDto = modelMapper.map(reservationEntity, ReservationDto.class);
        reservationDto.setUserId(reservationEntity.getUser().getId());
        reservationDto.setVehicleId(reservationEntity.getVehicle().getId());
        return reservationDto;
    }

    @Override
    public ReservationEntity dtoToEntity(ReservationDto reservationDto){
        ReservationEntity reservationEntity = modelMapper.map(reservationDto, ReservationEntity.class);
        try {
            reservationEntity.setUser(userServices.dtoToEntity(userServices.getUserById(reservationDto.getUserId()).getBody()));
            reservationEntity.setVehicle(vehicleServices.dtoToEntity(vehicleServices.getVehicleById(reservationDto.getVehicleId()).getBody()));
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return reservationEntity;
    }
}
