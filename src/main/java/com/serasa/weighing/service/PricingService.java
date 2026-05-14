package com.serasa.weighing.service;

import com.serasa.weighing.entity.GrainType;
import com.serasa.weighing.repository.WeighingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Calcula o custo da carga e a margem de lucro.
 *
 * Regra: margem entre 5% e 20%, inversamente proporcional ao estoque do grão na
 * doca.
 * - Grão escasso (pouco estoque) → margem alta (até 20%)
 * - Grão abundante (muito estoque) → margem baixa (até 5%)
 */
@Service
@RequiredArgsConstructor
public class PricingService {

    private final WeighingRepository weighingRepository;

    @Value("${pricing.min-margin}")
    private double minMargin;

    @Value("${pricing.max-margin}")
    private double maxMargin;

    @Value("${pricing.max-stock-tons}")
    private double maxStockTons;

    public BigDecimal calculateCargoCost(double netWeightKg, GrainType grainType) {
        double netWeightTons = netWeightKg / 1000.0;
        return grainType.getPurchasePricePerTon()
                .multiply(BigDecimal.valueOf(netWeightTons))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
