package com.ankoma88.datastructures.performance.domain.model;

public record AverageTime(Long arrayList, Long linkedList) {

    @Override
    public String toString() {
        return "AverageTime: {" +
                "ArrayList: " + arrayList + " ms" +
                ", LinkedList: " + linkedList + " ms" +
                '}';
    }
}
