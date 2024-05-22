package com.ankoma88.datastructures.performance.domain.model;

public record Details(Analysis arrayList, Analysis linkedList) {

    @Override
    public String toString() {
        return "Details: {" +
                "ArrayList: " + arrayList +
                ", LinkedList: " + linkedList +
                '}';
    }
}
