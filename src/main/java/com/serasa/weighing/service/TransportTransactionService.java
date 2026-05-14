package com.serasa.weighing.service;

import com.serasa.weighing.dto.request.TransportTransactionRequest;
import com.serasa.weighing.dto.response.TransportTransactionResponse;
import com.serasa.weighing.entity.TransportTransaction;
import com.serasa.weighing.entity.enums.TransactionStatus;
import com.serasa.weighing.exception.ResourceNotFoundException;
import com.serasa.weighing.repository.TransportTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransportTransactionService {

    private final TransportTransactionRepository repository;
    private final TruckService truckService;
    private final GrainTypeService grainTypeService;

    public List<TransportTransactionResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public TransportTransactionResponse findById(UUID id) {
        return toResponse(findEntityById(id));
    }

    public TransportTransactionResponse create(TransportTransactionRequest request) {
        var transaction = TransportTransaction.builder()
                .truck(truckService.findEntityById(request.getTruckId()))
                .grainType(grainTypeService.findEntityById(request.getGrainTypeId()))
                .status(TransactionStatus.IN_TRANSIT)
                .startedAt(Instant.now())
                .build();
        return toResponse(repository.save(transaction));
    }

    public TransportTransaction findActiveByPlate(String plate) {
        return repository.findByTruckPlateAndStatus(plate, TransactionStatus.IN_TRANSIT)
                .or(() -> repository.findByTruckPlateAndStatus(plate, TransactionStatus.WEIGHING))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Nenhuma transação ativa para o caminhão com placa: " + plate));
    }

    public void complete(TransportTransaction transaction) {
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setFinishedAt(Instant.now());
        repository.save(transaction);
    }

    public TransportTransaction findEntityById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada: " + id));
    }

    private TransportTransactionResponse toResponse(TransportTransaction t) {
        return TransportTransactionResponse.builder()
                .id(t.getId())
                .truckId(t.getTruck().getId())
                .truckPlate(t.getTruck().getPlate())
                .grainTypeId(t.getGrainType().getId())
                .grainTypeName(t.getGrainType().getName())
                .status(t.getStatus().name())
                .startedAt(t.getStartedAt())
                .finishedAt(t.getFinishedAt())
                .build();
    }
}
