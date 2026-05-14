package com.serasa.weighing.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class TransportTransactionRequest {
    @NotNull
    private UUID truckId;
    @NotNull
    private UUID grainTypeId;
}
