package com.ankoma88.datastructures.performance.data.repository;

import com.ankoma88.datastructures.performance.data.entities.MeasurementRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface MeasurementsRepository extends JpaRepository<MeasurementRecord, Long> {

    @Query("SELECT m from measurements m")
    Page<MeasurementRecord> getMeasurementRecords(Pageable pageable);
}