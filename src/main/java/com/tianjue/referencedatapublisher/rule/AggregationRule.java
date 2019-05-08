package com.tianjue.referencedatapublisher.rule;

import com.tianjue.referencedatapublisher.instrument.Field;

import java.util.Map;

@FunctionalInterface
public interface AggregationRule {
    void aggregate(Map<Field, String> instrumentData, Map<Field, String> aggregatedData);
}
