package com.tianjue.referencedatapublisher.rule;

import com.tianjue.referencedatapublisher.instrument.Field;

import java.util.Map;

@FunctionalInterface
public interface MatchCodeRule {
    String getCode(Map<Field, String> map);
}
