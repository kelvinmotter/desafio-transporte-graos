package com.serasa.weighing.service;

import com.serasa.weighing.dto.request.ScaleRequest;
import com.serasa.weighing.dto.response.ScaleResponse;
import com.serasa.weighing.entity.Scale;
import com.serasa.weighing.exception.ResourceNotFoundException;
import com.serasa.weighing.repository.ScaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScaleService {

    private final ScaleRepository repository;
    private final BranchService branchService;

    public List<ScaleResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public ScaleResponse findById(UUID id) {
        return toResponse(findEntityById(id));
    }

    public ScaleResponse create(ScaleRequest request) {
        var scale = Scale.builder()
                .code(request.getCode())
                .branch(branchService.findEntityById(request.getBranchId()))
                .apiKey(UUID.randomUUID().toString())
                .build();
        return toResponse(repository.save(scale));
    }

    public ScaleResponse update(UUID id, ScaleRequest request) {
        var scale = findEntityById(id);
        scale.setCode(request.getCode());
        scale.setBranch(branchService.findEntityById(request.getBranchId()));
        return toResponse(repository.save(scale));
    }

    public void delete(UUID id) {
        findEntityById(id);
        repository.deleteById(id);
    }

    public Scale findEntityById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Balança não encontrada: " + id));
    }

    public Scale findByCode(String code) {
        return repository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Balança não encontrada com código: " + code));
    }

    public Scale findByApiKey(String apiKey) {
        return repository.findByApiKey(apiKey)
                .orElseThrow(() -> new ResourceNotFoundException("Balança não autorizada"));
    }

    private ScaleResponse toResponse(Scale scale) {
        return ScaleResponse.builder()
                .id(scale.getId())
                .code(scale.getCode())
                .branchId(scale.getBranch().getId())
                .branchName(scale.getBranch().getName())
                .apiKey(scale.getApiKey())
                .build();
    }
}
