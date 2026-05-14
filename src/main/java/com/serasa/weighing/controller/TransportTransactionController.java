package com.serasa.weighing.controller;

import com.serasa.weighing.dto.request.TransportTransactionRequest;
import com.serasa.weighing.dto.response.TransportTransactionResponse;
import com.serasa.weighing.service.TransportTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransportTransactionController {

    private final TransportTransactionService service;

    @GetMapping
    public List<TransportTransactionResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public TransportTransactionResponse findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransportTransactionResponse create(@Valid @RequestBody TransportTransactionRequest request) {
        return service.create(request);
    }
}
