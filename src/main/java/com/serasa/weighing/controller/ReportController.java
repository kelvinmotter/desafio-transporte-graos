package com.serasa.weighing.controller;

import com.serasa.weighing.dto.response.WeighingResponse;
import com.serasa.weighing.dto.response.report.GrainSummaryReport;
import com.serasa.weighing.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService service;

    @GetMapping("/grain-summary")
    public List<GrainSummaryReport> getGrainSummary() {
        return service.getGrainSummary();
    }

    @GetMapping("/weighing-history")
    public List<WeighingResponse> getWeighingHistory(
            @RequestParam Instant startDate,
            @RequestParam Instant endDate) {
        return service.getWeighingHistory(startDate, endDate);
    }

    @GetMapping("/weighing-history/plate/{plate}")
    public List<WeighingResponse> getWeighingsByPlate(@PathVariable String plate) {
        return service.getWeighingsByPlate(plate);
    }
}
