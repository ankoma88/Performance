package com.ankoma88.datastructures.performance.domain.model;

import java.sql.Timestamp;

public record PerformanceMeasurement (
    Long id,
    String dataStructure,
    String operation,
    Long start,
    Long middle,
    Long end,
    Timestamp timeStamp
){}
