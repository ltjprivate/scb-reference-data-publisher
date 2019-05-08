package com.tianjue.referencedatapublisher.config;

import com.tianjue.referencedatapublisher.rule.*;

import java.util.ArrayList;
import java.util.List;


public class LMEConfig {
    private static List<MatchCodeRule> matchCodeRules = new ArrayList<>();
    private static CreateAggregatedInstrumentRule createAggregatedInstrumentRule;
    private static List<AggregationRule> aggregationRules = new ArrayList<>();

    public static void addMatchingCodeRule(MatchCodeRule func) {
        matchCodeRules.add(func);
    }

    public static List<MatchCodeRule> getMatchCodeRules() {
        return matchCodeRules;
    }

    public static void addAggregationRules(AggregationRule rule) {
        aggregationRules.add(rule);
    }

    public static List<AggregationRule> getAggregationRules() {
        return aggregationRules;
    }

    public static void setCreateAggregatedInstrumentRule(CreateAggregatedInstrumentRule rule) {
        createAggregatedInstrumentRule = rule;
    }

    public static CreateAggregatedInstrumentRule getCreateAggregatedInstrumentRule() {
        return createAggregatedInstrumentRule;
    }

    public static void setRules() {
        matchCodeRules.clear();
        aggregationRules.clear();

        LMEConfig.addMatchingCodeRule(AvailableMatchCodeRule.CODE_RULE);
        LMEConfig.setCreateAggregatedInstrumentRule(AvailableCreateAggreatedInstrumentRule.LME_CREATE_RULE);
        LMEConfig.addAggregationRules(AvailableAggregationRule.LME_UPDATE_RULE);
    }
}
