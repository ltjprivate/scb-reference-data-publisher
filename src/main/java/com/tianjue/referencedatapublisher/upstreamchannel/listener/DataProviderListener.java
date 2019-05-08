package com.tianjue.referencedatapublisher.upstreamchannel.listener;

import com.tianjue.referencedatapublisher.exception.ExceptionHandler;

public interface DataProviderListener {
    void onData(String str, ExceptionHandler exceptionHandler);
}
