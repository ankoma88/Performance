package com.ankoma88.datastructures.performance.domain.model;

public record Measurement(Long start, Long middle, Long end) {

    @Override
    public String toString() {
        return "{" +
                "Start: " + start + " ms, " +
                "Middle: " + middle + " ms, " +
                "End: " + end + " ms" +
                '}';
    }
}
