package com.ankoma88.datastructures.Performance.domain.model;

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
