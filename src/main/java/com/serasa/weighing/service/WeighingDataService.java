package com.serasa.weighing.service;

import com.serasa.weighing.dto.request.ScaleReadingRequest;
import com.serasa.weighing.entity.Weighing;
import com.serasa.weighing.repository.WeighingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Serviço principal de ingestão de dados das balanças.
 * Recebe leitura → buffer Redis → estabiliza → persiste pesagem.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WeighingDataService {

    private final StabilizationService stabilizationService;
    private final ScaleService scaleService;
    private final TruckService truckService;
    private final TransportTransactionService transactionService;
    private final PricingService pricingService;
    private final WeighingRepository weighingRepository;

    @Transactional
    public void processReading(ScaleReadingRequest request) {
        var result = stabilizationService.addReadingAndCheck(
                request.getId(), request.getPlate(), request.getWeight());

        result.ifPresent(grossWeight -> persistWeighing(request.getId(), request.getPlate(), grossWeight));
    }

    private void persistWeighing(String scaleCode, String plate, double grossWeight) {
        var scale = scaleService.findByCode(scaleCode);
        var truck = truckService.findByPlate(plate);
        var transaction = transactionService.findActiveByPlate(plate);

        double tare = truck.getTare();
        double netWeight = grossWeight - tare;
        var grainType = transaction.getGrainType();
        var cargoCost = pricingService.calculateCargoCost(netWeight, grainType);

        var weighing = Weighing.builder()
                .scale(scale)
                .transaction(transaction)
                .plate(plate)
                .grossWeight(grossWeight)
                .tare(tare)
                .netWeight(netWeight)
                .grainType(grainType.getName())
                .cargoCost(cargoCost)
                .weighedAt(Instant.now())
                .build();

        weighingRepository.save(weighing);
        transactionService.complete(transaction);

        log.info("Pesagem registrada: placa={}, pesoLíquido={}kg, grão={}, custo=R${}",
                plate, netWeight, grainType.getName(), cargoCost);
    }
}
