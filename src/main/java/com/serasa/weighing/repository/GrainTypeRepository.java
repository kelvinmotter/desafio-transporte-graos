package com.serasa.weighing.repository;

import com.serasa.weighing.entity.GrainType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface GrainTypeRepository extends JpaRepository<GrainType, UUID> {
}
