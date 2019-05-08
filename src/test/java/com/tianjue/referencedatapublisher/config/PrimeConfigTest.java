package com.tianjue.referencedatapublisher.config;

import com.tianjue.referencedatapublisher.rule.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class PrimeConfigTest {
    @BeforeClass
    public static void beforeClass() {
        PRIMEConfig.setRules();
    }

    @Test
    public void assertMatchRules() {
        List<MatchCodeRule> matchCodeRuleList = PRIMEConfig.getMatchCodeRules();
        Assert.assertEquals(1, matchCodeRuleList.size());
        Assert.assertEquals(AvailableMatchCodeRule.EXCHANGE_CODE_RULE, matchCodeRuleList.get(0));
    }

    @Test
    public void assertCreateAggrInstrumentRules() {
        CreateAggregatedInstrumentRule rule = PRIMEConfig.getCreateAggregatedInstrumentRule();
        Assert.assertEquals(AvailableCreateAggreatedInstrumentRule.PRIME_CREATE_RULE, rule);
    }

    @Test
    public void assertAggregationRule() {
        List<AggregationRule> rules = PRIMEConfig.getAggregationRules();
        Assert.assertEquals(1, rules.size());
        Assert.assertEquals(AvailableAggregationRule.PRIME_UPDATE_RULE, rules.get(0));
    }
}
