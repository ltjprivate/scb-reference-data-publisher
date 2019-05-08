package com.tianjue.referencedatapublisher.rule;

import com.tianjue.referencedatapublisher.instrument.Field;

import java.util.HashMap;
import java.util.Map;

public class AvailableAggregationRule {

    /**
     * @param instrumentData new instrument data update
     * @param aggregatedData aggregated data view
     * @param rules overwritten rule
     */
    private static void defaultAggregation(Map<Field, String> instrumentData, Map<Field, String> aggregatedData, Map<Field, Boolean> rules) {
        instrumentData.entrySet().stream().
                filter(instrumentDataEntry -> rules.get(instrumentDataEntry.getKey()) != null).
                forEach(instrumentDataEntry -> {
                    Field field = instrumentDataEntry.getKey();
                    String value = instrumentDataEntry.getValue();
                    if(rules.get(field)) {
                                // true: force update the field value
                                aggregatedData.put(field, value);
                            } else {
                                // false: only update when the field doesn't exist in the aggregated data
                                aggregatedData.putIfAbsent(field, value);
                            }
                        }
                );
    }

    public static AggregationRule LME_UPDATE_RULE = new AggregationRule() {
        private final Map<Field, Boolean> rules = new HashMap<>();
        {
            rules.put(Field.LAST_TRADING_DATE, true);
            rules.put(Field.DELIVERY_DATE, true);
            rules.put(Field.MARKET, false);
            rules.put(Field.LABEL, false);
            rules.put(Field.TRADABLE, false);
        }

        @Override
        public void aggregate(Map<Field, String> instrumentData, Map<Field, String> aggregatedData) {
            defaultAggregation(instrumentData, aggregatedData, rules);
        }
    };

    public static AggregationRule PRIME_UPDATE_RULE = new AggregationRule() {
        private final Map<Field, Boolean> rules = new HashMap<>();
        {
            rules.put(Field.LAST_TRADING_DATE, false);
            rules.put(Field.DELIVERY_DATE, false);
            rules.put(Field.MARKET, false);
            rules.put(Field.LABEL, false);
            rules.put(Field.TRADABLE, true);
        }

        @Override
        public void aggregate(Map<Field, String> instrumentData, Map<Field, String> aggregatedData) {
            defaultAggregation(instrumentData, aggregatedData, rules);
        }
    };
}
