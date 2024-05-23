package com.ankoma88.datastructures.performance.domain.performance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class AnalyzerTests {

    private TestAnalyzer testAnalyzer;

    @BeforeEach
    public void setup() {
        testAnalyzer = new TestAnalyzer();
    }

    @Test
    public void testDoPerformanceAnalysis() {
        long result = testAnalyzer.doPerformanceAnalysis();
        assert (result >= 10);
    }

    private static class TestAnalyzer extends Analyzer {

        @Override
        int getRepeats() {
            return 1;
        }

        @Override
        String getName() {
            return "TestAnalyzer";
        }

        @Override
        void setup() {
            // setup for test
        }

        @Override
        void runMethod() {
            try {
                Thread.sleep(10); // simulate some work
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}