package com.ankoma88.datastructures.performance.service;

import com.ankoma88.datastructures.performance.data.entities.MeasurementRecord;
import com.ankoma88.datastructures.performance.data.repository.MeasurementsRepository;
import com.ankoma88.datastructures.performance.domain.performance.PerformanceAnalysis;
import com.ankoma88.datastructures.performance.domain.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.ankoma88.datastructures.performance.domain.model.DataStructureEnum.ARRAYLIST;
import static com.ankoma88.datastructures.performance.domain.model.DataStructureEnum.LINKEDLIST;
import static com.ankoma88.datastructures.performance.domain.model.OperationEnum.*;
import static com.ankoma88.datastructures.performance.domain.model.ScenarioEnum.*;

@Service
public class PerformanceAnalysisService implements AnalysisService {

    private static final Logger LOGGER = Logger.getLogger(PerformanceAnalysisService.class.getName());

    @Autowired
    @Qualifier("measurementsTaskExecutor")
    private Executor measurementsThreadPool;

    @Autowired
    private MeasurementsRepository measurementsRepository;

    @Override
    public List<PerformanceMeasurementDto> getLatestMeasurements(int size){
        PageRequest pageRequest = PageRequest.of(0, size);
        Page<MeasurementRecord> records = measurementsRepository.getMeasurementRecords(pageRequest);
        List<MeasurementRecord> measurementRecords = records.getContent();
        return buildMeasurements(measurementRecords);
    }

    @Override
    public PerformanceDto measureOperations(int listSize, int repeats) {

        final CompletableFuture<MeasurementDto> arrayListCreateCF = buildCompletableFuture(listSize, repeats, CREATE, ARRAYLIST);
        final CompletableFuture<MeasurementDto> arrayListReadCF = buildCompletableFuture(listSize, repeats, READ, ARRAYLIST);
        final CompletableFuture<MeasurementDto> arrayListUpdateCF = buildCompletableFuture(listSize, repeats, UPDATE, ARRAYLIST);
        final CompletableFuture<MeasurementDto> arrayListDeleteCF = buildCompletableFuture(listSize, repeats, DELETE, ARRAYLIST);

        final CompletableFuture<MeasurementDto> linkedListCreateCF = buildCompletableFuture(listSize, repeats, CREATE, LINKEDLIST);
        final CompletableFuture<MeasurementDto> linkedListReadCF = buildCompletableFuture(listSize, repeats, READ, LINKEDLIST);
        final CompletableFuture<MeasurementDto> linkedListUpdateCF = buildCompletableFuture(listSize, repeats, UPDATE, LINKEDLIST);
        final CompletableFuture<MeasurementDto> linkedListDeleteCF = buildCompletableFuture(listSize, repeats, DELETE, LINKEDLIST);

        AnalysisDto arrayListAnalysis = null;
        AnalysisDto linkedListAnalysis = null;
        PerformanceDto performance;

        try {
            arrayListAnalysis = new AnalysisDto(
                    arrayListCreateCF.get(),
                    arrayListReadCF.get(),
                    arrayListUpdateCF.get(),
                    arrayListDeleteCF.get()
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            LOGGER.log(Level.WARNING, e.getCause().getMessage(), e.getCause());
        }
        try {
            linkedListAnalysis = new AnalysisDto(
                    linkedListCreateCF.get(),
                    linkedListReadCF.get(),
                    linkedListUpdateCF.get(),
                    linkedListDeleteCF.get()
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            LOGGER.log(Level.WARNING, e.getCause().getMessage(), e.getCause());
        }

        performance = buildPerformanceAnalysis(arrayListAnalysis, linkedListAnalysis);

        return performance;
    }

    private CompletableFuture<MeasurementDto> buildCompletableFuture(int listSize, int repeats, OperationEnum operation, DataStructureEnum dataStructure) {
        final PerformanceAnalysis performanceAnalysis = new PerformanceAnalysis(listSize, repeats);

        AtomicLong start = new AtomicLong();
        AtomicLong middle = new AtomicLong();
        AtomicLong end = new AtomicLong();
        return CompletableFuture.supplyAsync(() -> {
            switch (operation) {
                case CREATE -> {
                    start.set(performanceAnalysis.operationCreate(START, dataStructure));
                    middle.set(performanceAnalysis.operationCreate(MIDDLE, dataStructure));
                    end.set(performanceAnalysis.operationCreate(END, dataStructure));
                }
                case READ -> {
                    start.set(performanceAnalysis.operationRead(START, dataStructure));
                    middle.set(performanceAnalysis.operationRead(MIDDLE, dataStructure));
                    end.set(performanceAnalysis.operationRead(END, dataStructure));
                }
                case UPDATE -> {
                    start.set(performanceAnalysis.operationUpdate(START, dataStructure));
                    middle.set(performanceAnalysis.operationUpdate(MIDDLE, dataStructure));
                    end.set(performanceAnalysis.operationUpdate(END, dataStructure));
                }
                case DELETE -> {
                    start.set(performanceAnalysis.operationDelete(START, dataStructure));
                    middle.set(performanceAnalysis.operationDelete(MIDDLE, dataStructure));
                    end.set(performanceAnalysis.operationDelete(END, dataStructure));
                }
            }
            final MeasurementDto measurement = new MeasurementDto(start.get(), middle.get(), end.get());
            persist(measurement, operation, dataStructure);
            return measurement;
        }, measurementsThreadPool);
    }

    private void persist(MeasurementDto measurement, OperationEnum operation, DataStructureEnum dataStructure) {
        measurementsRepository.save(buildMeasurementRecord(measurement, operation, dataStructure));
    }

    private PerformanceDto buildPerformanceAnalysis(AnalysisDto arrayListAnalysis, AnalysisDto linkedListAnalysis) {
        final Long averageArrayListOperationTime = getAverageOperationTime(arrayListAnalysis);
        final Long averageLinkedListOperationTime = getAverageOperationTime(linkedListAnalysis);

        final String recommendation = buildRecommendation();

        final AverageTimeDto averageTime = new AverageTimeDto(averageArrayListOperationTime, averageLinkedListOperationTime);

        return new PerformanceDto(
                new DetailsDto(
                        arrayListAnalysis,
                        linkedListAnalysis
                ),
                new OverallComparisonDto(
                        recommendation,
                        averageTime
                )
        );
    }

    private Long getAverageOperationTime(AnalysisDto arrayListAnalysis) {
        final Long arrayListCreateAverageTime = (arrayListAnalysis.getCreate().getStart()
                + arrayListAnalysis.getCreate().getMiddle() + arrayListAnalysis.getCreate().getEnd()) / 3;
        final Long arrayListReadAverageTime = (arrayListAnalysis.getRead().getStart()
                + arrayListAnalysis.getRead().getMiddle() + arrayListAnalysis.getRead().getEnd()) / 3;
        final Long arrayListUpdateAverageTime = (arrayListAnalysis.getUpdate().getStart()
                + arrayListAnalysis.getCreate().getMiddle() + arrayListAnalysis.getUpdate().getEnd()) / 3;
        final Long arrayListDeleteAverageTime = (arrayListAnalysis.getDelete().getStart()
                + arrayListAnalysis.getDelete().getMiddle() + arrayListAnalysis.getDelete().getEnd()) / 3;
        return (arrayListCreateAverageTime + arrayListReadAverageTime
                + arrayListUpdateAverageTime + arrayListDeleteAverageTime) / 4;
    }

    private String buildRecommendation() {
        return """
                Based on the performance metrics, ArrayList is recommended for scenarios involving frequent read
                operations, while LinkedList is better suited for scenarios with frequent insertions and deletions.
                """;
    }

    private MeasurementRecord buildMeasurementRecord(MeasurementDto measurement, OperationEnum operation, DataStructureEnum dataStructure) {
        return new MeasurementRecord(dataStructure.name(), operation.name(), measurement.getStart(), measurement.getMiddle(), measurement.getEnd(), Timestamp.from(Instant.now()));
    }

    private List<PerformanceMeasurementDto> buildMeasurements(List<MeasurementRecord> measurementRecords) {
        return measurementRecords.stream().map(it ->
                new PerformanceMeasurementDto(
                        it.getId(), it.getDataStructure(), it.getOperation(), it.getFirst(), it.getMiddle(), it.getLast(), it.getTimeStamp()
                )
        ).collect(Collectors.toList());
    }
}
