package com.ankoma88.datastructures.performance.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DetailsDto {
    private AnalysisDto arrayList;
    private AnalysisDto linkedList;

    @Override
    public String toString() {
        return "Details: {" +
                "ArrayList: " + arrayList +
                ", LinkedList: " + linkedList +
                '}';
    }
}
