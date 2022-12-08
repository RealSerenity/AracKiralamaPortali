package rserenity.business.services;

import org.springframework.http.ResponseEntity;
import rserenity.Exception.ResourceNotFoundException;
import rserenity.business.dto.CompanyDto;
import rserenity.business.dto.ReservationDto;
import rserenity.data.entity.ReservationEntity;
import rserenity.data.entity.UserEntity;
import rserenity.data.entity.VehicleEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReservationServices {

    public List<ReservationDto> getAll();

    public Map<String, Object> createReservation(ReservationDto userDto);

    public List<ReservationDto> getUserReservations(Long userId);

    public ResponseEntity<ReservationDto> getReservationById(Long id) throws ResourceNotFoundException;

    public ResponseEntity<Map<String,Boolean>> deleteReservation(Long id);

    public boolean checkDateIsAvailable(VehicleEntity entity, LocalDate date);

    public ReservationDto entityToDto(ReservationEntity reservationEntity);
    public ReservationEntity dtoToEntity(ReservationDto reservationDto);

}
