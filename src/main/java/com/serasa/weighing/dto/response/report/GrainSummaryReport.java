package com.serasa.weighing.dto.response.report;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data @Builder
public class GrainSummaryReport {
    private String grainType;
    private Long totalWeighings;
    private Double totalNetWeightKg;
    private BigDecimal totalCost;
}
