package com.serasa.weighing.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class ScaleRequest {
    @NotBlank
    private String code;
    @NotNull
    private UUID branchId;
}
