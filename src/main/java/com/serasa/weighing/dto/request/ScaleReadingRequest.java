package com.serasa.weighing.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScaleReadingRequest {
    @NotBlank
    private String id;
    @NotBlank
    private String plate;
    @NotNull
    private Double weight;
}
