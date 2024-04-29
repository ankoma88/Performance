package com.ankoma88.datastructures.Performance.domain.performance;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Analyzer {

    private static final Logger LOGGER = Logger.getLogger( PerformanceAnalysis.class.getName() );

    abstract int getRepeats();

    abstract String getName();

    abstract void setup();

    abstract void runMethod();

    public Long doPerformanceAnalysis() {
        int runs = getRepeats();
        long totalTime = 0;
        for (int i = 0; i < runs; i++) {
            setup();
            long startTime = System.nanoTime();
            runMethod();
            long endTime = System.nanoTime();
            totalTime += (endTime - startTime);
        }

        long averageTime = totalTime / runs;
        long millis = (long) (averageTime / 1e6);
        LOGGER.log(Level.INFO, (getName() + " took " + millis + " ms"));
        return millis;
    }
}
