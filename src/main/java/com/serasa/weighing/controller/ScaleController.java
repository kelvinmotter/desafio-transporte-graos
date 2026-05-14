package com.serasa.weighing.controller;

import com.serasa.weighing.dto.request.ScaleRequest;
import com.serasa.weighing.dto.response.ScaleResponse;
import com.serasa.weighing.service.ScaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/scales")
@RequiredArgsConstructor
public class ScaleController {

    private final ScaleService service;

    @GetMapping
    public List<ScaleResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ScaleResponse findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScaleResponse create(@Valid @RequestBody ScaleRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public ScaleResponse update(@PathVariable UUID id, @Valid @RequestBody ScaleRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
