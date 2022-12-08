package rserenity.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rserenity.data.entity.ReservationEntity;
import rserenity.data.entity.UserEntity;
import rserenity.data.entity.VehicleEntity;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity,Long>{

    public List<ReservationEntity> findReservationEntitiesByUserOrderByCreatedByDesc(UserEntity user);

    public List<ReservationEntity> findReservationEntitiesByReservationStartDate(LocalDate date);
}
