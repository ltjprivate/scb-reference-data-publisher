package com.tianjue.referencedatapublisher.rule;

import com.tianjue.referencedatapublisher.instrument.Field;

public class AvailableMatchCodeRule {
    public static final MatchCodeRule CODE_RULE = map -> map.get(Field.CODE);
    public static final MatchCodeRule EXCHANGE_CODE_RULE = map -> map.get(Field.EXCHANGE_CODE);
}
