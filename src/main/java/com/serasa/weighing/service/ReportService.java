package com.serasa.weighing.service;

import com.serasa.weighing.dto.response.WeighingResponse;
import com.serasa.weighing.dto.response.report.GrainSummaryReport;
import com.serasa.weighing.entity.Weighing;
import com.serasa.weighing.repository.WeighingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final WeighingRepository weighingRepository;

    public List<GrainSummaryReport> getGrainSummary() {
        return weighingRepository.getGrainSummary().stream()
                .map(row -> GrainSummaryReport.builder()
                        .grainType((String) row[0])
                        .totalWeighings((Long) row[1])
                        .totalNetWeightKg((Double) row[2])
                        .totalCost((BigDecimal) row[3])
                        .build())
                .toList();
    }

    public List<WeighingResponse> getWeighingHistory(Instant start, Instant end) {
        return weighingRepository.findByWeighedAtBetween(start, end).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<WeighingResponse> getWeighingsByPlate(String plate) {
        return weighingRepository.findByPlate(plate).stream()
                .map(this::toResponse)
                .toList();
    }

    private WeighingResponse toResponse(Weighing w) {
        return WeighingResponse.builder()
                .id(w.getId())
                .plate(w.getPlate())
                .grossWeight(w.getGrossWeight())
                .tare(w.getTare())
                .netWeight(w.getNetWeight())
                .grainType(w.getGrainType())
                .cargoCost(w.getCargoCost())
                .scaleId(w.getScale().getId())
                .weighedAt(w.getWeighedAt())
                .build();
    }
}
