package com.ankoma88.datastructures.performance.data.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "measurements")
public class MeasurementRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "datastructure")
    private String dataStructure;
    @Column(name = "operation")
    private String operation;
    @Column(name = "first")
    private Long first;
    @Column(name = "middle")
    private Long middle;
    @Column(name = "last")
    private Long last;
    @Column(name = "timestamp")
    private Timestamp timeStamp;

    public MeasurementRecord(String dataStructure, String operation, Long first, Long middle, Long last, Timestamp timeStamp) {
        this.dataStructure = dataStructure;
        this.operation = operation;
        this.first = first;
        this.middle = middle;
        this.last = last;
        this.timeStamp = timeStamp;
    }
}
