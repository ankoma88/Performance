package com.ankoma88.datastructures.Performance.domain.performance;

public class Config {
    private final int listSize;

    protected final String[] elements;

    public Config(int listSize) {
        this.listSize = listSize;
        elements = initList();
    }

    public int getListSize() {
        return listSize;
    }

    public String[] initList() {
        String[] elements = new String[listSize];
        Boolean result = Boolean.TRUE;
        for (int i = 0; i < listSize; i++) {
            elements[i] = getElement(result, i);
            result = !result;
        }
        return elements;
    }
    protected String getElement(Boolean result, int i) {
        return String.valueOf(result) + i + !result;
    }
}
