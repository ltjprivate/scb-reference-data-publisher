package com.tianjue.referencedatapublisher.instrument;

import com.tianjue.referencedatapublisher.exception.ParseException;

import java.util.HashMap;
import java.util.Map;

public class InstrumentUtils {
    public static String toString(Map<Field, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        map.entrySet().forEach(entry -> {
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("|");
        });
        return stringBuilder.toString();
    }

    public static Map<Field, String> toMap(String str) throws ParseException{
        Map<Field, String> dataMap = new HashMap<>();
        String[] fieldValues = str.split("\\|");
        for (String fieldValue : fieldValues) {
            String[] pair = fieldValue.split("=");
            if (pair.length != 2) {
                throw new ParseException("wrong data: " + str);
            }
            dataMap.put(Field.valueOf(pair[0]), pair[1]);
        }
        return dataMap;
    }
}
