package com.serasa.weighing.repository;

import com.serasa.weighing.entity.TransportTransaction;
import com.serasa.weighing.entity.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface TransportTransactionRepository extends JpaRepository<TransportTransaction, UUID> {
    Optional<TransportTransaction> findByTruckPlateAndStatus(String plate, TransactionStatus status);
}
