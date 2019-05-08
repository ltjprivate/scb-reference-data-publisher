package com.tianjue.referencedatapublisher.rule;

import com.tianjue.referencedatapublisher.instrument.AggregatedInstrument;
import com.tianjue.referencedatapublisher.instrument.Field;

import java.util.Map;

public interface CreateAggregatedInstrumentRule {
    AggregatedInstrument create(Map<Field, String> fieldStringMap);
}
