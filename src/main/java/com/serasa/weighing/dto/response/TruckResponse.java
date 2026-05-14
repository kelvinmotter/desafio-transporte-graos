package com.serasa.weighing.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data @Builder
public class TruckResponse {
    private UUID id;
    private String plate;
    private Double tare;
    private UUID branchId;
    private String branchName;
}
