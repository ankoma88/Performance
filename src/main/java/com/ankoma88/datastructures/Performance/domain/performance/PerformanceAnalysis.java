package com.ankoma88.datastructures.performance.domain.performance;

import com.ankoma88.datastructures.performance.domain.model.DataStructure;
import com.ankoma88.datastructures.performance.domain.model.Scenario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.ankoma88.datastructures.performance.domain.model.Operation.*;

public class PerformanceAnalysis {

    private final Config config;
    private final int repeats;
    private List<String> testList;
    private List<String> stringList;
    private String firstElement;
    private String middleElement;
    private String lastElement;
    private int max;

    public PerformanceAnalysis(int listSize, int repeats) {
        this.repeats = repeats;
        config = new Config(listSize);
    }

    public void initialize() {
        stringList = Arrays.asList(config.elements);
        max = config.getListSize();
        firstElement = config.getElement(true, 0);
        middleElement = config.getElement(true, max / 2);
        lastElement = config.getElement(true, max - 1);
    }

    public Long operationCreate(Scenario scenario, DataStructure dataStructure) {
        initialize();
        Analyzer operationAdd = new Analyzer() {
            @Override
            int getRepeats() {
                return repeats;
            }

            @Override
            String getName() {
                return dataStructure.name() + " " + CREATE + " " + scenario.name();
            }

            @Override
            void setup() {
                testList = dataStructure == DataStructure.ARRAYLIST ? new ArrayList<>() : new LinkedList<>();
                testList.addAll(stringList);
            }

            @Override
            void runMethod() {
                List<String> currentList = testList;
                switch (scenario) {
                    case START -> currentList.addFirst(firstElement);
                    case MIDDLE -> currentList.add(max / 2, middleElement);
                    case END -> currentList.addLast(lastElement);
                }
            }
        };
        return operationAdd.doPerformanceAnalysis();
    }

    public Long operationRead(Scenario scenario, DataStructure dataStructure) {
        initialize();
        Analyzer findInList = new Analyzer() {
            @Override
            int getRepeats() {
                return repeats;
            }

            @Override
            String getName() {
                return dataStructure.name() + " " + READ + " " + scenario.name();
            }

            @Override
            void setup() {
                testList = dataStructure == DataStructure.ARRAYLIST ? new ArrayList<>() : new LinkedList<>();
                testList.addAll(stringList);
            }

            @Override
            void runMethod() {
                List<String> currentList = testList;
                switch (scenario) {
                    case START -> currentList.getFirst();
                    case MIDDLE -> currentList.get(max / 2);
                    case END -> currentList.getLast();
                }
            }
        };
        return findInList.doPerformanceAnalysis();
    }

    public Long operationUpdate(Scenario scenario, DataStructure dataStructure) {
        initialize();
        Analyzer updateInList = new Analyzer() {
            @Override
            int getRepeats() {
                return repeats;
            }

            @Override
            String getName() {
                return dataStructure.name() + " " + UPDATE + " " + scenario.name();
            }

            @Override
            void setup() {
                testList = dataStructure == DataStructure.ARRAYLIST ? new ArrayList<>() : new LinkedList<>();
                testList.addAll(stringList);
            }

            @Override
            void runMethod() {
                List<String> currentList = testList;
                switch (scenario) {
                    case START -> {
                        String found = currentList.getFirst();
                        if (found != null) {
                            currentList.set(0, found + "_");
                        }
                    }
                    case MIDDLE -> {
                        String found = currentList.get(max / 2);
                        if (found != null) {
                            currentList.set(max / 2, found + "_");
                        }
                    }
                    case END -> {
                        String found = currentList.getLast();
                        if (found != null) {
                            currentList.set(max - 1, found + "_");
                        }
                    }
                }
            }
        };
        return updateInList.doPerformanceAnalysis();
    }

    public Long operationDelete(Scenario scenario, DataStructure dataStructure) {
        initialize();
        Analyzer deleteInList = new Analyzer() {
            @Override
            int getRepeats() {
                return repeats;
            }

            @Override
            String getName() {
                return dataStructure.name() + " " + DELETE + " " + scenario.name();
            }

            @Override
            void setup() {
                testList = dataStructure == DataStructure.ARRAYLIST ? new ArrayList<>() : new LinkedList<>();
                testList.addAll(stringList);
            }

            @Override
            void runMethod() {
                List<String> currentList = testList;
                switch (scenario) {
                    case START -> currentList.remove(firstElement);
                    case MIDDLE -> currentList.remove(middleElement);
                    case END -> currentList.remove(lastElement);
                }
            }
        };
        return deleteInList.doPerformanceAnalysis();
    }
}
        
