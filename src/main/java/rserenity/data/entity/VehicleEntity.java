package rserenity.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import rserenity.enums.City;
import rserenity.enums.VehicleType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Log4j2
@Entity
@Table(name = "vehicles")
@Builder
public class VehicleEntity extends BaseEntity{
    @Column(name = "dailyPrice",nullable = false)
    private Double dailyPrice;
    @Column(name = "availableUntil",nullable = false)
    private LocalDate availableUntil;

    @Column(name = "type",nullable = false)
    private VehicleType type;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private CompanyEntity company;

    @JsonIgnore
    @OneToMany(mappedBy = "vehicle")
    private Set<ReservationEntity> reservations = new HashSet<>();

    public VehicleEntity(Double dailyPrice, LocalDate availableUntil, VehicleType type, CompanyEntity company, Set<ReservationEntity> reservations) {
        this.dailyPrice = dailyPrice;
        this.availableUntil = availableUntil;
        this.type = type;
        this.company = company;
        this.reservations = reservations;
    }
}
