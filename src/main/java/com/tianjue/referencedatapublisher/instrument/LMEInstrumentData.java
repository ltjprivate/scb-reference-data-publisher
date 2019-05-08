package com.tianjue.referencedatapublisher.instrument;

import com.tianjue.referencedatapublisher.config.LMEConfig;
import com.tianjue.referencedatapublisher.exception.ParseException;
import com.tianjue.referencedatapublisher.rule.AggregationRule;
import com.tianjue.referencedatapublisher.rule.MatchCodeRule;
import com.tianjue.referencedatapublisher.rule.RuleExecutionLogic;

public class LMEInstrumentData extends InstrumentData {

    public LMEInstrumentData(String str) {
        super(str);
    }

    @Override
    public void parse() throws ParseException {
        super.parse();
    }

    @Override
    public String getMatchingCode() {
        return RuleExecutionLogic.executeMatchCodeRules(LMEConfig.getMatchCodeRules(), dataMap);
    }

    @Override
    public void aggregate(AggregatedInstrument aggregatedInstrument) {
        RuleExecutionLogic.executeAggregationRules(LMEConfig.getAggregationRules(), dataMap, aggregatedInstrument.getDataMap());
    }

    @Override
    public AggregatedInstrument createAggregatedInstrument() {
        return LMEConfig.getCreateAggregatedInstrumentRule().create(dataMap);
    }
}
