package rserenity.data.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Log4j2
@Entity
@Table(name = "reservations")
public class ReservationEntity extends BaseEntity{

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    private VehicleEntity vehicle;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @Column(name = "reservationStartDate",nullable = false)
    private LocalDate reservationStartDate;

    public ReservationEntity(VehicleEntity vehicle, UserEntity user, LocalDate reservationStartDate) {
        this.vehicle = vehicle;
        this.user = user;
        this.reservationStartDate = reservationStartDate;
    }
}
