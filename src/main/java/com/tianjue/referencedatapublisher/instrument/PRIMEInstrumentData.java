package com.tianjue.referencedatapublisher.instrument;

import com.tianjue.referencedatapublisher.config.PRIMEConfig;
import com.tianjue.referencedatapublisher.exception.ParseException;
import com.tianjue.referencedatapublisher.rule.RuleExecutionLogic;

public class PRIMEInstrumentData extends InstrumentData {

    public PRIMEInstrumentData(String str) {
        super(str);
    }

    @Override
    public void parse() throws ParseException {
        super.parse();
    }

    @Override
    public String getMatchingCode() {
        return RuleExecutionLogic.executeMatchCodeRules(PRIMEConfig.getMatchCodeRules(), dataMap);
    }

    @Override
    public void aggregate(AggregatedInstrument aggregatedInstrument) {
        RuleExecutionLogic.executeAggregationRules(PRIMEConfig.getAggregationRules(), dataMap, aggregatedInstrument.getDataMap());
    }

    @Override
    public AggregatedInstrument createAggregatedInstrument() {
        return PRIMEConfig.getCreateAggregatedInstrumentRule().create(dataMap);
    }
}
