package com.ankoma88.datastructures.performance.domain.performance;

import com.ankoma88.datastructures.performance.domain.model.DataStructureEnum;
import com.ankoma88.datastructures.performance.domain.model.ScenarioEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PerformanceAnalysisTests {

    private PerformanceAnalysis performanceAnalysis;

    @BeforeEach
    public void setup() {
        performanceAnalysis = new PerformanceAnalysis(1000000, 1);
    }

    @Test
    public void testOperations() {
        Long result1 = performanceAnalysis.operationCreate(ScenarioEnum.START, DataStructureEnum.ARRAYLIST);
        Long result2 = performanceAnalysis.operationCreate(ScenarioEnum.MIDDLE, DataStructureEnum.ARRAYLIST);
        Long result3 = performanceAnalysis.operationCreate(ScenarioEnum.END, DataStructureEnum.ARRAYLIST);
        Long result5 = performanceAnalysis.operationRead(ScenarioEnum.START, DataStructureEnum.ARRAYLIST);
        Long result6 = performanceAnalysis.operationRead(ScenarioEnum.MIDDLE, DataStructureEnum.ARRAYLIST);
        Long result4 = performanceAnalysis.operationRead(ScenarioEnum.END, DataStructureEnum.ARRAYLIST);
        Long result7 = performanceAnalysis.operationDelete(ScenarioEnum.START, DataStructureEnum.ARRAYLIST);
        Long result8 = performanceAnalysis.operationDelete(ScenarioEnum.MIDDLE, DataStructureEnum.ARRAYLIST);
        Long result9 = performanceAnalysis.operationDelete(ScenarioEnum.END, DataStructureEnum.ARRAYLIST);
        Long result10 = performanceAnalysis.operationUpdate(ScenarioEnum.START, DataStructureEnum.ARRAYLIST);
        Long result11 = performanceAnalysis.operationUpdate(ScenarioEnum.MIDDLE, DataStructureEnum.ARRAYLIST);
        Long result12 = performanceAnalysis.operationUpdate(ScenarioEnum.END, DataStructureEnum.ARRAYLIST);
        long total = result1 + result2 + result3 + result4 + result5 + result6 + result7 + result8 + result9 + result10 + result11 + result12;
        assertTrue(total > 0);
    }

}