package com.tianjue.referencedatapublisher;

import com.tianjue.referencedatapublisher.exception.ExceptionHandler;
import com.tianjue.referencedatapublisher.exception.ParseException;
import com.tianjue.referencedatapublisher.instrument.Field;
import com.tianjue.referencedatapublisher.upstreamchannel.listener.DataProviderListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TestUtils {
    public static long TIME_OUT_SEC = 10;

    public static Map<Field, String> decode(String string) throws ParseException {
        Map<Field, String> dataMap = new HashMap<>();
        String[] fieldValues = string.split("\\|");
        Arrays.stream(fieldValues).forEach(str -> {
            String[] fieldValue = str.split("=");
            dataMap.put(Field.valueOf(fieldValue[0]), fieldValue[1]);
        });
        return dataMap;
    }


    public static void upstreamPublishInstrument(DataProviderListener dataProviderListener, String code, String lastTradeDate, String deliveryDate, String market, String label, String exchangeCode, String tradable,
                                                 ExceptionHandler exceptionHandler) {
        Map<Field, String> data = new LinkedHashMap<>();
        if(code != null) {
            data.put(Field.CODE, code);
        }
        data.put(Field.LAST_TRADING_DATE, lastTradeDate);
        data.put(Field.DELIVERY_DATE, deliveryDate);
        data.put(Field.MARKET, market);
        data.put(Field.LABEL, label);
        if(exchangeCode != null) {
            data.put(Field.EXCHANGE_CODE, exchangeCode);
        }
        if(tradable != null) {
            data.put(Field.TRADABLE, tradable);
        }
        upstreamPublishInstrument(dataProviderListener, data, exceptionHandler);
    }

    private static void upstreamPublishInstrument(DataProviderListener dataProviderListener, Map<Field, String> data, ExceptionHandler exceptionHandler) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<Field, String> entry : data.entrySet()) {
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("|");
        }
        dataProviderListener.onData(stringBuilder.toString(), exceptionHandler);
    }
}
