package com.tianjue.referencedatapublisher.upstreamchannel.listener;

import com.tianjue.referencedatapublisher.exception.ExceptionHandler;
import com.tianjue.referencedatapublisher.instrument.PRIMEInstrumentData;

public class PRIMEDataProviderListener implements DataProviderListener {
    @Override
    public void onData(String str, ExceptionHandler exceptionHandler) {
        ListenerUtils.onInstrument(new PRIMEInstrumentData(str), exceptionHandler);
    }
}
