package com.serasa.weighing.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data @Builder
public class ScaleResponse {
    private UUID id;
    private String code;
    private UUID branchId;
    private String branchName;
    private String apiKey;
}
