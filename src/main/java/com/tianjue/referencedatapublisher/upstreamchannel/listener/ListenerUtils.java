package com.tianjue.referencedatapublisher.upstreamchannel.listener;

import com.tianjue.referencedatapublisher.coreprocessor.ReferenceDataProcessor;
import com.tianjue.referencedatapublisher.exception.ExceptionHandler;
import com.tianjue.referencedatapublisher.exception.ParseException;
import com.tianjue.referencedatapublisher.executor.ReferenceDataPublisherExecutor;
import com.tianjue.referencedatapublisher.instrument.InstrumentData;

public class ListenerUtils {
    static void onInstrument(InstrumentData instrument, ExceptionHandler exceptionHandler) {
        try {
            instrument.parse();

            // same instrument: same executor
            String matchingCode = instrument.getMatchingCode();
            if (matchingCode != null) {
                ReferenceDataPublisherExecutor.getProcessorExecutor(matchingCode).execute(new ReferenceDataProcessor(instrument));
            } else {
                throw new ParseException("ERROR: cannot get matching code. so cannot route to the executor.");
            }

        } catch (ParseException e) {
            System.out.println("Fail to Parse Instrument: " + e.getMessage() + ". str: " + instrument.toString());
            if (exceptionHandler != null) {
                exceptionHandler.handle(instrument.toString(), e);
            }
        }
    }
}
