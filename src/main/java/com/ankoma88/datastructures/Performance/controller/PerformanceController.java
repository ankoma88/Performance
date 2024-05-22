package com.ankoma88.datastructures.performance.controller;

import com.ankoma88.datastructures.performance.domain.model.PerformanceDto;
import com.ankoma88.datastructures.performance.domain.model.PerformanceMeasurementDto;
import com.ankoma88.datastructures.performance.service.PerformanceAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/performance")
public class PerformanceController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final PerformanceAnalysisService performanceAnalysisService;

    @Autowired
    public PerformanceController(PerformanceAnalysisService performanceAnalysisService,
                                 KafkaTemplate<String, String> kafkaTemplate
    ) {
        this.performanceAnalysisService = performanceAnalysisService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping("measure")
    public ResponseEntity<PerformanceDto> measurePerformance(
            @RequestParam(value = "listSize", defaultValue = "1000") int listSize,
            @RequestParam(value = "repeats", defaultValue = "1") int repeats
    ) {
        try {
            ResponseEntity<PerformanceDto> response = ResponseEntity.ok(performanceAnalysisService.measureOperations(listSize, repeats));
            sendMessage("measurePerformance job done");
            return response;
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("getMeasurements")
    public ResponseEntity<List<PerformanceMeasurementDto>> getPerformanceMeasurements(
            @RequestParam(value = "size", defaultValue = "100") int size
    ) {
        try {
            ResponseEntity<List<PerformanceMeasurementDto>> response = ResponseEntity.ok(performanceAnalysisService.getLatestMeasurements(size));
            sendMessage("getPerformanceMeasurements job done");
            return response;
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    public void sendMessage(String msg) {
        kafkaTemplate.send("topic1", msg);
    }
}
