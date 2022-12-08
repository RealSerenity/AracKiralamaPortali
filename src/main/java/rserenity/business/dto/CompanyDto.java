package rserenity.business.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import rserenity.enums.City;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Log4j2
@Builder
public class CompanyDto {
    private Long id;
    private String name;
    private City city;

    @Override
    public String toString() {
        return "id=" + id +
                ", name='" + name + '\'' +
                ", city=" + city ;
    }
}
