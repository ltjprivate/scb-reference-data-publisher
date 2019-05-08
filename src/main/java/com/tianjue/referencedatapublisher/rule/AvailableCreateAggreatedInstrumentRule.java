package com.tianjue.referencedatapublisher.rule;

import com.tianjue.referencedatapublisher.instrument.AggregatedInstrument;
import com.tianjue.referencedatapublisher.instrument.Field;

import java.util.Map;

public class AvailableCreateAggreatedInstrumentRule {
    public static final CreateAggregatedInstrumentRule LME_CREATE_RULE = new CreateAggregatedInstrumentRule() {
        @Override
        public AggregatedInstrument create(Map<Field, String> fieldStringMap) {
            AggregatedInstrument aggregatedInstrument = new AggregatedInstrument();
            aggregatedInstrument.addAll(fieldStringMap);

            // put TRADABLE default value as true
            if(fieldStringMap.get(Field.TRADABLE) == null) {
                aggregatedInstrument.add(Field.TRADABLE, "TRUE");
            }
            return aggregatedInstrument;
        }
    };

    public static final CreateAggregatedInstrumentRule PRIME_CREATE_RULE = new CreateAggregatedInstrumentRule() {
        @Override
        public AggregatedInstrument create(Map<Field, String> fieldStringMap) {
            AggregatedInstrument aggregatedInstrument = new AggregatedInstrument();
            aggregatedInstrument.addAll(fieldStringMap);
            String exchangeCode = fieldStringMap.get(Field.EXCHANGE_CODE);
            if(exchangeCode != null) {
                aggregatedInstrument.add(Field.CODE, exchangeCode);
            }
            return aggregatedInstrument;
        }
    };
}
