package com.serasa.weighing.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data @Builder
public class WeighingResponse {
    private UUID id;
    private String plate;
    private Double grossWeight;
    private Double tare;
    private Double netWeight;
    private String grainType;
    private BigDecimal cargoCost;
    private UUID scaleId;
    private Instant weighedAt;
}
