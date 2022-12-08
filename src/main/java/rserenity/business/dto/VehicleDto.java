package rserenity.business.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import rserenity.enums.VehicleType;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Log4j2
@Builder
public class VehicleDto {
    private Long id;
    private Double dailyPrice;
    private LocalDate availableUntil;
    private VehicleType type;
    private Long companyId;

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", dailyPrice=" + dailyPrice +
                ", availableUntil=" + availableUntil +
                ", type=" + type +
                ", companyId=" + companyId +
                '}';
    }
}
