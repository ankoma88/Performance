package com.ankoma88.datastructures.Performance.data.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Table("measurements")
public class MeasurementRecord {
    @Id
    @Column("id")
    private Long id;
    @Column("datastructure")
    private String dataStructure;
    @Column("operation")
    private String operation;
    @Column("first")
    private Long first;
    @Column("middle")
    private Long middle;
    @Column("last")
    private Long last;
    @Column("timestamp")
    private Timestamp timeStamp;

    public MeasurementRecord(String dataStructure, String operation, Long first, Long middle, Long last, Timestamp timeStamp) {
        this.dataStructure = dataStructure;
        this.operation = operation;
        this.first = first;
        this.middle = middle;
        this.last = last;
        this.timeStamp = timeStamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDataStructure() {
        return dataStructure;
    }

    public void setDataStructure(String dataStructure) {
        this.dataStructure = dataStructure;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Long getFirst() {
        return first;
    }

    public void setFirst(Long first) {
        this.first = first;
    }

    public Long getMiddle() {
        return middle;
    }

    public void setMiddle(Long middle) {
        this.middle = middle;
    }

    public Long getLast() {
        return last;
    }

    public void setLast(Long last) {
        this.last = last;
    }

    public Timestamp getTimestamp() {
        return timeStamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timeStamp = timestamp;
    }
}
