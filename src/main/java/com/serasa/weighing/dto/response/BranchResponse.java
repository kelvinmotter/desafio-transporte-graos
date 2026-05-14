package com.serasa.weighing.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data @Builder
public class BranchResponse {
    private UUID id;
    private String name;
}
