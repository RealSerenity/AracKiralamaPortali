package rserenity.business.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Log4j2
@Builder
public class ReservationDto {
    private Long id;
    private Long vehicleId;
    private Long userId;
    private LocalDate reservationStartDate;


}
