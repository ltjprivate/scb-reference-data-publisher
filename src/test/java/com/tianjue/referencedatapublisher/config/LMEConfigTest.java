package com.tianjue.referencedatapublisher.config;

import com.tianjue.referencedatapublisher.rule.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class LMEConfigTest {
    @BeforeClass
    public static void beforeClass() {
        LMEConfig.setRules();
    }

    @Test
    public void assertMatchRules() {
        List<MatchCodeRule> matchCodeRuleList = LMEConfig.getMatchCodeRules();
        Assert.assertEquals(1, matchCodeRuleList.size());
        Assert.assertEquals(AvailableMatchCodeRule.CODE_RULE, matchCodeRuleList.get(0));
    }

    @Test
    public void assertCreateAggrInstrumentRule() {
        CreateAggregatedInstrumentRule rule = LMEConfig.getCreateAggregatedInstrumentRule();
        Assert.assertEquals(AvailableCreateAggreatedInstrumentRule.LME_CREATE_RULE, rule);
    }

    @Test
    public void assertAggregationRule() {
        List<AggregationRule> rules = LMEConfig.getAggregationRules();
        Assert.assertEquals(1, rules.size());
        Assert.assertEquals(AvailableAggregationRule.LME_UPDATE_RULE, rules.get(0));
    }
}
