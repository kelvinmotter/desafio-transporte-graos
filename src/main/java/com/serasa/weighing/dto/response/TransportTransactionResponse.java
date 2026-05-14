package com.serasa.weighing.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.Instant;
import java.util.UUID;

@Data @Builder
public class TransportTransactionResponse {
    private UUID id;
    private UUID truckId;
    private String truckPlate;
    private UUID grainTypeId;
    private String grainTypeName;
    private String status;
    private Instant startedAt;
    private Instant finishedAt;
}
