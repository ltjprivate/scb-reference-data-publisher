package com.tianjue.referencedatapublisher;

import com.tianjue.referencedatapublisher.config.LMEConfig;
import com.tianjue.referencedatapublisher.config.PRIMEConfig;
import com.tianjue.referencedatapublisher.downstreamapi.SubscriberManager;
import com.tianjue.referencedatapublisher.executor.AvailableExecutorMappingFunction;
import com.tianjue.referencedatapublisher.executor.ReferenceDataPublisherExecutor;
import com.tianjue.referencedatapublisher.upstreamchannel.channel.DataSourceChannel;
import com.tianjue.referencedatapublisher.upstreamchannel.channel.TestFileChannel;
import com.tianjue.referencedatapublisher.upstreamchannel.listener.LMEDataProviderListener;
import com.tianjue.referencedatapublisher.upstreamchannel.listener.PRIMEDataProviderListener;

public class ReferenceDataPublisher
{
    static {
        // process thread pool initialization
        ReferenceDataPublisherExecutor.registerExecutorHashFunction(AvailableExecutorMappingFunction.STRING_HASH_CODE_FUNCTION);
        LMEConfig.setRules();
        PRIMEConfig.setRules();
    }

    static void printUsage() {
        System.out.println("Usage: ReferenceDataPublisher LMEDataFilePath PrimeDataFilePath");
    }

    public static void main( String[] args )
    {
        if(args.length != 2) {
            printUsage();
            System.exit(-1);
        }

        try {
            // register downstream subscriber
            SubscriberManager.addSubscriber(str -> {
                System.out.println("======================= Subscriber Receive Data ===================================");
                System.out.println(str);
            });

            // initialize upstream channel
            String lmeDataFilePath = args[0];
            String primeDataFilePath = args[1];
            DataSourceChannel lmeTestFileChannel = new TestFileChannel(lmeDataFilePath);
            DataSourceChannel primeTestFileChannel = new TestFileChannel(primeDataFilePath);
            lmeTestFileChannel.setDataProviderListener(new LMEDataProviderListener());
            primeTestFileChannel.setDataProviderListener(new PRIMEDataProviderListener());

            lmeTestFileChannel.start();
            primeTestFileChannel.start();

            ReferenceDataPublisherExecutor.awaitTermination(1000000);
        } catch(InterruptedException e){
            System.out.println(e.getMessage());
        }
    }
}
