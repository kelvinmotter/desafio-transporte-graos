package com.serasa.weighing.controller;

import com.serasa.weighing.dto.request.GrainTypeRequest;
import com.serasa.weighing.dto.response.GrainTypeResponse;
import com.serasa.weighing.service.GrainTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/grain-types")
@RequiredArgsConstructor
public class GrainTypeController {

    private final GrainTypeService service;

    @GetMapping
    public List<GrainTypeResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public GrainTypeResponse findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GrainTypeResponse create(@Valid @RequestBody GrainTypeRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public GrainTypeResponse update(@PathVariable UUID id, @Valid @RequestBody GrainTypeRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
