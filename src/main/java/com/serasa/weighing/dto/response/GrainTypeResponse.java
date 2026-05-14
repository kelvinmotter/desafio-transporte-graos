package com.serasa.weighing.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data @Builder
public class GrainTypeResponse {
    private UUID id;
    private String name;
    private BigDecimal purchasePricePerTon;
}
