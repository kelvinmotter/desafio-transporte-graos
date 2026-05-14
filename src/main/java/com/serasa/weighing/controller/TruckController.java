package com.serasa.weighing.controller;

import com.serasa.weighing.dto.request.TruckRequest;
import com.serasa.weighing.dto.response.TruckResponse;
import com.serasa.weighing.service.TruckService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/trucks")
@RequiredArgsConstructor
public class TruckController {

    private final TruckService service;

    @GetMapping
    public List<TruckResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public TruckResponse findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TruckResponse create(@Valid @RequestBody TruckRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public TruckResponse update(@PathVariable UUID id, @Valid @RequestBody TruckRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
