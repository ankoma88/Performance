package com.ankoma88.datastructures.performance.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AverageTimeDto {
    private Long arrayList;
    private Long linkedList;

    @Override
    public String toString() {
        return "AverageTime: {" +
                "ArrayList: " + arrayList + " ms" +
                ", LinkedList: " + linkedList + " ms" +
                '}';
    }
}
