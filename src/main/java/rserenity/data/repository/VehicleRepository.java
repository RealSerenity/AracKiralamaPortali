package rserenity.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import rserenity.data.entity.CompanyEntity;
import rserenity.data.entity.VehicleEntity;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity,Long> {

    public List<VehicleEntity> findVehicleEntitiesByCompany(CompanyEntity entity);
}
