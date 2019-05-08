package com.tianjue.referencedatapublisher.instrument;

import java.util.HashMap;
import java.util.Map;

public class AggregatedInstrument {
    private Map<Field, String> dataMap = new HashMap<>();

    Map<Field, String> getDataMap() {
        return dataMap;
    }

    public void add(Field field, String value) {
        dataMap.put(field, value);
    }

    public void addAll(Map<Field, String> map) {
        dataMap.putAll(map);
    }

    @Override
    public String toString() {
        return InstrumentUtils.toString(dataMap);
    }
}
