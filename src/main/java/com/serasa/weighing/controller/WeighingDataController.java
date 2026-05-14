package com.serasa.weighing.controller;

import com.serasa.weighing.dto.request.ScaleReadingRequest;
import com.serasa.weighing.service.WeighingDataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weighing")
@RequiredArgsConstructor
public class WeighingDataController {

    private final WeighingDataService service;

    @PostMapping("/readings")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void receiveReading(@Valid @RequestBody ScaleReadingRequest request) {
        service.processReading(request);
    }
}
