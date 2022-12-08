package rserenity.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Log4j2
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity{

    @Column(name = "username",nullable = false, unique = true)
    private String username;

    @Column(name = "password",nullable = false)
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<ReservationEntity> reservations = new HashSet<>();

    public UserEntity(String username, String password, Set<ReservationEntity> reservations) {
        this.username = username;
        this.password = password;
        this.reservations = reservations;
    }
}
