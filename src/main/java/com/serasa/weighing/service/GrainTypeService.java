package com.serasa.weighing.service;

import com.serasa.weighing.dto.request.GrainTypeRequest;
import com.serasa.weighing.dto.response.GrainTypeResponse;
import com.serasa.weighing.entity.GrainType;
import com.serasa.weighing.exception.ResourceNotFoundException;
import com.serasa.weighing.repository.GrainTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GrainTypeService {

    private final GrainTypeRepository repository;

    public List<GrainTypeResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public GrainTypeResponse findById(UUID id) {
        return toResponse(findEntityById(id));
    }

    public GrainTypeResponse create(GrainTypeRequest request) {
        var grain = GrainType.builder()
                .name(request.getName())
                .purchasePricePerTon(request.getPurchasePricePerTon())
                .build();
        return toResponse(repository.save(grain));
    }

    public GrainTypeResponse update(UUID id, GrainTypeRequest request) {
        var grain = findEntityById(id);
        grain.setName(request.getName());
        grain.setPurchasePricePerTon(request.getPurchasePricePerTon());
        return toResponse(repository.save(grain));
    }

    public void delete(UUID id) {
        findEntityById(id);
        repository.deleteById(id);
    }

    public GrainType findEntityById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de grão não encontrado: " + id));
    }

    private GrainTypeResponse toResponse(GrainType grain) {
        return GrainTypeResponse.builder()
                .id(grain.getId())
                .name(grain.getName())
                .purchasePricePerTon(grain.getPurchasePricePerTon())
                .build();
    }
}
