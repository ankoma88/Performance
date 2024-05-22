package com.ankoma88.datastructures.performance;

import com.ankoma88.datastructures.performance.controller.PerformanceController;
import com.ankoma88.datastructures.performance.domain.model.*;
import com.ankoma88.datastructures.performance.service.PerformanceAnalysisService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PerformanceController.class)
class PerformanceControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PerformanceAnalysisService service;

    private List<PerformanceMeasurement> createMockResponseForMeasurements() {
        PerformanceMeasurement someMeasurement0 = new PerformanceMeasurement(
                123L,
                DataStructure.ARRAYLIST.name(),
                Operation.CREATE.name(),
                1L,
                20L,
                0L,
                Timestamp.from(Instant.now())
        );
        PerformanceMeasurement someMeasurement1 = new PerformanceMeasurement(
                124L,
                DataStructure.LINKEDLIST.name(),
                Operation.READ.name(),
                1L,
                0L,
                2L,
                Timestamp.from(Instant.now())
        );
        final List<PerformanceMeasurement> mockResponse = new ArrayList<>();
        mockResponse.add(someMeasurement0);
        mockResponse.add(someMeasurement1);
        return mockResponse;
    }

    private Performance createMockResponseForPerformanceTest() {
        return new Performance(
                new Details(
                        new Analysis(new Measurement(1L, 20L, 1L), new Measurement(0L, 5L, 2L), new Measurement(0L, 0L, 1L), new Measurement(3L, 0L, 0L)),
                        new Analysis(new Measurement(0L, 0L, 1L), new Measurement(0L, 0L, 1L), new Measurement(10L, 20L, 1L), new Measurement(0L, 0L, 1L))
                ),
                new OverallComparison("some recommendation", new AverageTime(10L, 8L))
        );
    }

    @Test
    public void testMeasureOperationsReturns200Ok() throws Exception {
        Mockito.when(service.measureOperations(1000, 1)).thenReturn(createMockResponseForPerformanceTest());

        mvc.perform(MockMvcRequestBuilders
                        .get("/performance/measure")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testMeasureOperationsResponseIsCorrect() throws Exception {
        Mockito.when(service.measureOperations(1000, 1)).thenReturn(createMockResponseForPerformanceTest());

        mvc.perform(MockMvcRequestBuilders
                        .get("/performance/measure")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().json("{\"details\":{\"arrayList\":{\"create\":{\"start\":1,\"middle\":20,\"end\":1},\"read\":{\"start\":0,\"middle\":5,\"end\":2},\"update\":{\"start\":0,\"middle\":0,\"end\":1},\"delete\":{\"start\":3,\"middle\":0,\"end\":0}},\"linkedList\":{\"create\":{\"start\":0,\"middle\":0,\"end\":1},\"read\":{\"start\":0,\"middle\":0,\"end\":1},\"update\":{\"start\":10,\"middle\":20,\"end\":1},\"delete\":{\"start\":0,\"middle\":0,\"end\":1}}},\"overallComparison\":{\"recommendation\":\"some recommendation\",\"averageTime\":{\"arrayList\":10,\"linkedList\":8}}}"));
    }

    @Test
    public void testMeasureOperationsResponseContainsDetailsForBothLists() throws Exception {
        Mockito.when(service.measureOperations(1000, 1)).thenReturn(createMockResponseForPerformanceTest());

        mvc.perform(MockMvcRequestBuilders
                        .get("/performance/measure")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.details.arrayList").exists())
                .andExpect(jsonPath("$.details.linkedList").exists())
        ;
    }

    @Test
    public void testMeasureOperationsContainsAnalysisForOperations() throws Exception {
        Mockito.when(service.measureOperations(1000, 1)).thenReturn(createMockResponseForPerformanceTest());

        mvc.perform(MockMvcRequestBuilders
                        .get("/performance/measure")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.details.arrayList.create").exists())
                .andExpect(jsonPath("$.details.arrayList.read").exists())
                .andExpect(jsonPath("$.details.arrayList.update").exists())
                .andExpect(jsonPath("$.details.arrayList.delete").exists());
    }

    @Test
    public void testGetPerformanceMeasurementsReturns200Ok() throws Exception {
        Mockito.when(service.getLatestMeasurements(2)).thenReturn(createMockResponseForMeasurements());

        mvc.perform(MockMvcRequestBuilders
                        .get("/performance/getMeasurements")
                        .param("size", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPerformanceMeasurementsResponseContainsResultsForBothDatastructures() throws Exception {
        Mockito.when(service.getLatestMeasurements(2)).thenReturn(createMockResponseForMeasurements());

        mvc.perform(MockMvcRequestBuilders
                        .get("/performance/getMeasurements")
                        .param("size", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$[0].dataStructure").value(DataStructure.ARRAYLIST.name()))
                .andExpect(jsonPath("$[1].dataStructure").value(DataStructure.LINKEDLIST.name()));
    }
}