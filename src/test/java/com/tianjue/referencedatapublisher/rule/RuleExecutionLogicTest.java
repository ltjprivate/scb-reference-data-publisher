package com.tianjue.referencedatapublisher.rule;

import com.tianjue.referencedatapublisher.instrument.Field;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleExecutionLogicTest {
    private List<MatchCodeRule> matchCodeRules = new ArrayList<>();
    private List<AggregationRule> aggregationRules = new ArrayList<>();
    private Map<Field, String> dataMap = new HashMap<>();
    private Map<Field, String> aggrMap = new HashMap<>();

    @After
    public void after() {
        matchCodeRules.clear();
        aggregationRules.clear();
        dataMap.clear();
        aggrMap.clear();
    }

    @Test
    public void Given_first_match_code_rule_return_non_null_When_second_match_code_rule_return_non_null_Then_it_returns_first_rule_result() {
        String firstInstrument = "FIRST";
        String secondInstrument = "SECOND";
        matchCodeRules.add(map -> {return firstInstrument;});
        matchCodeRules.add(map -> {return secondInstrument;});
        Assert.assertEquals(firstInstrument, RuleExecutionLogic.executeMatchCodeRules(matchCodeRules, dataMap));
    }

    @Test
    public void Given_first_match_code_rule_return_null_When_second_match_code_rule_return_non_null_Then_it_returns_non_null_result() {
        String firstInstrument = null;
        String secondInstrument = "SECOND";
        matchCodeRules.add(map -> {return firstInstrument;});
        matchCodeRules.add(map -> {return secondInstrument;});
        Assert.assertEquals(secondInstrument, RuleExecutionLogic.executeMatchCodeRules(matchCodeRules, dataMap));
    }

    @Test
    public void Given_first_agg_rule_update_first_When_second_agg_rule_update_Then_it_contains_both_updates() {
        String first = "FIRST";
        String second = "SECOND";
        Field firstField = Field.CODE;
        Field secondField = Field.EXCHANGE_CODE;
        aggregationRules.add((instrumentMap, aggMap) -> {aggMap.put(firstField, first);});
        aggregationRules.add((instrumentMap, aggMap) -> {aggMap.put(secondField, second);});
        RuleExecutionLogic.executeAggregationRules(aggregationRules, dataMap, aggrMap);

        Assert.assertEquals(first, aggrMap.get(firstField));
        Assert.assertEquals(second, aggrMap.get(secondField));
    }
}
