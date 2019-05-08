package com.tianjue.referencedatapublisher.instrument;

import com.tianjue.referencedatapublisher.exception.ParseException;

import java.util.HashMap;
import java.util.Map;

public abstract class InstrumentData{
    String str;
    Map<Field, String> dataMap = new HashMap<>();

    Map<Field, String> getDataMap() {
        return dataMap;
    }

    protected InstrumentData(String str) {
        this.str = str;
    }

    /**
     * the default parse way
     */
    public void parse() throws ParseException {
        String[] fieldValues = str.split("\\|");
        for (String fieldValue : fieldValues) {
            String[] pair = fieldValue.split("=");
            if (pair.length != 2) {
                throw new ParseException("wrong data: " + str);
            }
            dataMap.put(Field.valueOf(pair[0]), pair[1]);
        }
        if(dataMap.get(Field.CODE) == null) {
            throw new ParseException("Invalid reference data. Cannot find CODE field: str: " + str);
        }
    }

    public abstract String getMatchingCode();

    public abstract AggregatedInstrument createAggregatedInstrument();
    public abstract void aggregate(AggregatedInstrument aggregatedInstrument);


    @Override
    public String toString() {
        return InstrumentUtils.toString(dataMap);
    }


}
