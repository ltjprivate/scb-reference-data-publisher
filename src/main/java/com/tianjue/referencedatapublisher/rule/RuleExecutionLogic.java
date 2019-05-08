package com.tianjue.referencedatapublisher.rule;

import com.tianjue.referencedatapublisher.instrument.Field;

import java.util.List;
import java.util.Map;

public class RuleExecutionLogic {
    public static String executeMatchCodeRules(List<MatchCodeRule> matchCodeRules, Map<Field, String> dataMap) {
        for(MatchCodeRule matchCodeRule: matchCodeRules) {
            String code = matchCodeRule.getCode(dataMap);
            if(code != null && code.length() > 0) {
                return code;
            }
        }
        return null;
    }

    public static void executeAggregationRules(List<AggregationRule> aggregationRules,
                                               Map<Field, String> dataMap,
                                               Map<Field, String> aggregatedDataMap) {
        aggregationRules.forEach(
                aggregationRule -> aggregationRule.aggregate(dataMap, aggregatedDataMap));
    }
}
