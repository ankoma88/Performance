package com.ankoma88.datastructures.performance.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class PerformanceMeasurementDto {
    private Long id;
    private String dataStructure;
    private String operation;
    private Long start;
    private Long middle;
    private Long end;
    private Timestamp timeStamp;
}
