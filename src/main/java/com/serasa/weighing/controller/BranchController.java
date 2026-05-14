package com.serasa.weighing.controller;

import com.serasa.weighing.dto.request.BranchRequest;
import com.serasa.weighing.dto.response.BranchResponse;
import com.serasa.weighing.service.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService service;

    @GetMapping
    public List<BranchResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public BranchResponse findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BranchResponse create(@Valid @RequestBody BranchRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public BranchResponse update(@PathVariable UUID id, @Valid @RequestBody BranchRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
