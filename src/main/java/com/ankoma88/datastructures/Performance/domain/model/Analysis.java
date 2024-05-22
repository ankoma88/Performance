package com.ankoma88.datastructures.performance.domain.model;

public record Analysis(
        Measurement create,
        Measurement read,
        Measurement update,
        Measurement delete) {

    @Override
    public String toString() {
        return "{" + create + ", " + read + ", " + update + ", " + delete + "}";
    }
}
