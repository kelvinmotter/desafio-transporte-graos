package com.serasa.weighing.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class GrainTypeRequest {
    @NotBlank
    private String name;
    @NotNull
    private BigDecimal purchasePricePerTon;
}
