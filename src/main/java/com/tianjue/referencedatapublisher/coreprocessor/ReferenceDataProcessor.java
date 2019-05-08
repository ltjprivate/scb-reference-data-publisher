package com.tianjue.referencedatapublisher.coreprocessor;

import com.tianjue.referencedatapublisher.downstreamapi.SubscriberManager;
import com.tianjue.referencedatapublisher.instrument.AggregatedInstrument;
import com.tianjue.referencedatapublisher.instrument.InstrumentData;

public class ReferenceDataProcessor implements Runnable {

    private InstrumentData instrumentData;

    public ReferenceDataProcessor(InstrumentData instrumentData) {
        this.instrumentData = instrumentData;
    }

    @Override
    public void run() {
        AggregatedInstrument aggregatedInstrument = ReferenceDataManager.updateInstrument(this.instrumentData);
        SubscriberManager.publish(aggregatedInstrument.toString());
    }
}
