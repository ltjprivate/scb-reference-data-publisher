package com.tianjue.referencedatapublisher.coreprocessor;

import com.tianjue.referencedatapublisher.instrument.AggregatedInstrument;
import com.tianjue.referencedatapublisher.instrument.InstrumentData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReferenceDataManager {
    private static Map<String, AggregatedInstrument> map = new ConcurrentHashMap<>();

    public static AggregatedInstrument updateInstrument(InstrumentData instrumentData) {
        String matchingCode = instrumentData.getMatchingCode();
        AggregatedInstrument aggregatedInstrument = map.get(matchingCode);
        if(aggregatedInstrument == null) {
            aggregatedInstrument = instrumentData.createAggregatedInstrument();
            map.put(matchingCode, aggregatedInstrument);
        } else {
            instrumentData.aggregate(aggregatedInstrument);
        }
        return aggregatedInstrument;
    }

    /**
     * use for test cases
     */
    public static void clear() {
        map.clear();
    }
}
