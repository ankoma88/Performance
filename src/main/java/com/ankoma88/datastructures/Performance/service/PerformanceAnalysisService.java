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

import static com.ankoma88.datastructures.performance.domain.model.DataStructure.ARRAYLIST;
import static com.ankoma88.datastructures.performance.domain.model.DataStructure.LINKEDLIST;
import static com.ankoma88.datastructures.performance.domain.model.Operation.*;
import static com.ankoma88.datastructures.performance.domain.model.Scenario.*;

@Service
public class PerformanceAnalysisService implements AnalysisService {

    private static final Logger LOGGER = Logger.getLogger(PerformanceAnalysisService.class.getName());

    @Autowired
    @Qualifier("measurementsTaskExecutor")
    private Executor measurementsThreadPool;

    @Autowired
    private MeasurementsRepository measurementsRepository;

    @Override
    public List<PerformanceMeasurement> getLatestMeasurements(int size){
        PageRequest pageRequest = PageRequest.of(0, size);
        Page<MeasurementRecord> records = measurementsRepository.getMeasurementRecords(pageRequest);
        List<MeasurementRecord> measurementRecords = records.getContent();
        return buildMeasurements(measurementRecords);
    }

    @Override
    public Performance measureOperations(int listSize, int repeats) {

        final CompletableFuture<Measurement> arrayListCreateCF = buildCompletableFuture(listSize, repeats, CREATE, ARRAYLIST);
        final CompletableFuture<Measurement> arrayListReadCF = buildCompletableFuture(listSize, repeats, READ, ARRAYLIST);
        final CompletableFuture<Measurement> arrayListUpdateCF = buildCompletableFuture(listSize, repeats, UPDATE, ARRAYLIST);
        final CompletableFuture<Measurement> arrayListDeleteCF = buildCompletableFuture(listSize, repeats, DELETE, ARRAYLIST);

        final CompletableFuture<Measurement> linkedListCreateCF = buildCompletableFuture(listSize, repeats, CREATE, LINKEDLIST);
        final CompletableFuture<Measurement> linkedListReadCF = buildCompletableFuture(listSize, repeats, READ, LINKEDLIST);
        final CompletableFuture<Measurement> linkedListUpdateCF = buildCompletableFuture(listSize, repeats, UPDATE, LINKEDLIST);
        final CompletableFuture<Measurement> linkedListDeleteCF = buildCompletableFuture(listSize, repeats, DELETE, LINKEDLIST);

        Analysis arrayListAnalysis = null;
        Analysis linkedListAnalysis = null;
        Performance performance;

        try {
            arrayListAnalysis = new Analysis(
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
            linkedListAnalysis = new Analysis(
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

    private CompletableFuture<Measurement> buildCompletableFuture(int listSize, int repeats, Operation operation, DataStructure dataStructure) {
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
            final Measurement measurement = new Measurement(start.get(), middle.get(), end.get());
            persist(measurement, operation, dataStructure);
            return measurement;
        }, measurementsThreadPool);
    }

    private void persist(Measurement measurement, Operation operation, DataStructure dataStructure) {
        measurementsRepository.save(buildMeasurementRecord(measurement, operation, dataStructure));
    }

    private Performance buildPerformanceAnalysis(Analysis arrayListAnalysis, Analysis linkedListAnalysis) {
        final Long averageArrayListOperationTime = getAverageOperationTime(arrayListAnalysis);
        final Long averageLinkedListOperationTime = getAverageOperationTime(linkedListAnalysis);

        final String recommendation = buildRecommendation();

        final AverageTime averageTime = new AverageTime(averageArrayListOperationTime, averageLinkedListOperationTime);

        return new Performance(
                new Details(
                        arrayListAnalysis,
                        linkedListAnalysis
                ),
                new OverallComparison(
                        recommendation,
                        averageTime
                )
        );
    }

    private Long getAverageOperationTime(Analysis arrayListAnalysis) {
        final Long arrayListCreateAverageTime = (arrayListAnalysis.create().start()
                + arrayListAnalysis.create().middle() + arrayListAnalysis.create().end()) / 3;
        final Long arrayListReadAverageTime = (arrayListAnalysis.read().start()
                + arrayListAnalysis.read().middle() + arrayListAnalysis.read().end()) / 3;
        final Long arrayListUpdateAverageTime = (arrayListAnalysis.update().start()
                + arrayListAnalysis.update().middle() + arrayListAnalysis.update().end()) / 3;
        final Long arrayListDeleteAverageTime = (arrayListAnalysis.delete().start()
                + arrayListAnalysis.delete().middle() + arrayListAnalysis.delete().end()) / 3;
        return (arrayListCreateAverageTime + arrayListReadAverageTime
                + arrayListUpdateAverageTime + arrayListDeleteAverageTime) / 4;
    }

    private String buildRecommendation() {
        return """
                Based on the performance metrics, ArrayList is recommended for scenarios involving frequent read
                operations, while LinkedList is better suited for scenarios with frequent insertions and deletions.
                """;
    }

    private MeasurementRecord buildMeasurementRecord(Measurement measurement, Operation operation, DataStructure dataStructure) {
        return new MeasurementRecord(dataStructure.name(), operation.name(), measurement.start(), measurement.middle(), measurement.end(), Timestamp.from(Instant.now()));
    }

    private List<PerformanceMeasurement> buildMeasurements(List<MeasurementRecord> measurementRecords) {
        return measurementRecords.stream().map(it ->
                new PerformanceMeasurement(
                        it.getId(), it.getDataStructure(), it.getOperation(), it.getFirst(), it.getMiddle(), it.getLast(), it.getTimeStamp()
                )
        ).collect(Collectors.toList());
    }
}
