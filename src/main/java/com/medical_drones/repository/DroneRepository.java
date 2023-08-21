package com.medical_drones.repository;

import com.medical_drones.model.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DroneRepository extends JpaRepository<Drone, Long> {

    Optional<Drone> getBySerialNumber(String serialNumber);

    @Query(nativeQuery = true,
            value = "select * from medical_drone d " +
                    "where d.weight_limit > coalesce(" +
                    "select sum(weight) from medication m where m.drone_id = d.id," +
                    "0)" +
                    "and battery_level >= 25")
    List<Drone> findAvailableForLoading();
}
