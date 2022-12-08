package rserenity.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import rserenity.enums.City;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Log4j2
@Builder
@Entity
@Table(name = "companies")
public class CompanyEntity extends BaseEntity{

    @Column(name = "city", nullable = false)
    private City city;

    @Column(name = "name", nullable = false)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "company")
    private Set<VehicleEntity> vehicles = new HashSet<>();

    public CompanyEntity(City city, String name, Set<VehicleEntity> vehicles) {
        this.city = city;
        this.name = name;
        this.vehicles = vehicles;
    }
}
