package com.tianjue.referencedatapublisher.upstreamchannel.channel;

import com.tianjue.referencedatapublisher.upstreamchannel.listener.DataProviderListener;

public abstract class DataSourceChannel {

    protected DataProviderListener dataProviderListener;

    public abstract void start();

    public void setDataProviderListener(DataProviderListener listener) {
        dataProviderListener = listener;
    }
}
