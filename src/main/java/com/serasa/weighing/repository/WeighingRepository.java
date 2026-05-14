package com.serasa.weighing.repository;

import com.serasa.weighing.entity.Weighing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface WeighingRepository extends JpaRepository<Weighing, UUID> {

    List<Weighing> findByWeighedAtBetween(Instant start, Instant end);

    List<Weighing> findByPlate(String plate);

    @Query("SELECT w.grainType, COUNT(w), SUM(w.netWeight), SUM(w.cargoCost) FROM Weighing w GROUP BY w.grainType")
    List<Object[]> getGrainSummary();
}
