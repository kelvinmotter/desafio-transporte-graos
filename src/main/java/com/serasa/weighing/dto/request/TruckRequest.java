package com.serasa.weighing.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class TruckRequest {
    @NotBlank
    private String plate;
    @NotNull
    private Double tare;
    private UUID branchId;
}
