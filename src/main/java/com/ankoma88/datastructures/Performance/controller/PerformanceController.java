package com.ankoma88.datastructures.Performance.controller;

import com.ankoma88.datastructures.Performance.domain.model.Performance;
import com.ankoma88.datastructures.Performance.domain.model.PerformanceMeasurement;
import com.ankoma88.datastructures.Performance.service.PerformanceAnalysisService;
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
                                 KafkaTemplate<String, String> kafkaTemplate)
    {
        this.performanceAnalysisService = performanceAnalysisService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping("measure")
    public ResponseEntity<Performance> measurePerformance(
            @RequestParam(value = "listSize", defaultValue = "1000") int listSize,
            @RequestParam(value = "repeats", defaultValue = "1") int repeats
    ) {
        try {
            ResponseEntity<Performance> response = ResponseEntity.ok(performanceAnalysisService.measureOperations(listSize, repeats));
            sendMessage("measurePerformance job done");
            return response;
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("getMeasurements")
    public ResponseEntity<List<PerformanceMeasurement>> getPerformanceMeasurements(
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        try {
            ResponseEntity<List<PerformanceMeasurement>> response = ResponseEntity.ok(performanceAnalysisService.getLatestMeasurements(size)) ;
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
