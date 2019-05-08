package com.tianjue.referencedatapublisher.upstreamchannel.listener;

import com.tianjue.referencedatapublisher.exception.ExceptionHandler;
import com.tianjue.referencedatapublisher.instrument.LMEInstrumentData;

public class LMEDataProviderListener implements DataProviderListener {
    @Override
    public void onData(String str, ExceptionHandler exceptionHandler) {
        ListenerUtils.onInstrument(new LMEInstrumentData(str), exceptionHandler);
    }
}
