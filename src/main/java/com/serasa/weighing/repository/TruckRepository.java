package com.serasa.weighing.repository;

import com.serasa.weighing.entity.Truck;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface TruckRepository extends JpaRepository<Truck, UUID> {
    Optional<Truck> findByPlate(String plate);
}
