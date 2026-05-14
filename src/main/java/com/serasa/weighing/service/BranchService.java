package com.serasa.weighing.service;

import com.serasa.weighing.dto.request.BranchRequest;
import com.serasa.weighing.dto.response.BranchResponse;
import com.serasa.weighing.entity.Branch;
import com.serasa.weighing.exception.ResourceNotFoundException;
import com.serasa.weighing.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository repository;

    public List<BranchResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public BranchResponse findById(UUID id) {
        return toResponse(findEntityById(id));
    }

    public BranchResponse create(BranchRequest request) {
        var branch = Branch.builder().name(request.getName()).build();
        return toResponse(repository.save(branch));
    }

    public BranchResponse update(UUID id, BranchRequest request) {
        var branch = findEntityById(id);
        branch.setName(request.getName());
        return toResponse(repository.save(branch));
    }

    public void delete(UUID id) {
        findEntityById(id);
        repository.deleteById(id);
    }

    public Branch findEntityById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Filial não encontrada: " + id));
    }

    private BranchResponse toResponse(Branch branch) {
        return BranchResponse.builder()
                .id(branch.getId())
                .name(branch.getName())
                .build();
    }
}
