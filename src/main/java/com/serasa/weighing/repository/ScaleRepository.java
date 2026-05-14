package com.serasa.weighing.repository;

import com.serasa.weighing.entity.Scale;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ScaleRepository extends JpaRepository<Scale, UUID> {
    Optional<Scale> findByCode(String code);
    Optional<Scale> findByApiKey(String apiKey);
}
