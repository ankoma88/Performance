package com.ankoma88.datastructures.Performance.data.repository;

import com.ankoma88.datastructures.Performance.data.entities.MeasurementRecord;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MeasurementsRepository extends ListCrudRepository<MeasurementRecord, Long> {

    @Query("select * from measurements limit :size")
    List<MeasurementRecord> getMeasurementRecords(@Param("size") long size);
}
