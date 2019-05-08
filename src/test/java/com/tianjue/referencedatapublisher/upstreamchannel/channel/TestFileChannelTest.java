package com.tianjue.referencedatapublisher.upstreamchannel.channel;

import com.tianjue.referencedatapublisher.config.LMEConfig;
import com.tianjue.referencedatapublisher.downstreamapi.SubscriberManager;
import com.tianjue.referencedatapublisher.exception.ParseException;
import com.tianjue.referencedatapublisher.executor.AvailableExecutorMappingFunction;
import com.tianjue.referencedatapublisher.executor.ReferenceDataPublisherExecutor;
import com.tianjue.referencedatapublisher.instrument.Field;
import com.tianjue.referencedatapublisher.instrument.InstrumentUtils;
import com.tianjue.referencedatapublisher.upstreamchannel.listener.LMEDataProviderListener;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.tianjue.referencedatapublisher.TestUtils.TIME_OUT_SEC;

public class TestFileChannelTest {

    private String lmeDataFilePath = "data/lme_upstream_data.txt";
    AtomicBoolean assertTrue = new AtomicBoolean(true);

    @BeforeClass
    public static void beforeClass() {
        ReferenceDataPublisherExecutor.registerExecutorHashFunction(AvailableExecutorMappingFunction.STRING_HASH_CODE_FUNCTION);
        LMEConfig.setRules();
    }

    @Test
    public void When_filechannel_read_data_Then_it_should_publishes_same_data() throws InterruptedException{
        String code = "PB_03_2018";
        String lastTradeDate = "15-03-2018";
        String deliveryDate = "17-03-2018";
        String market = "PB";
        String label = "Lead 13 March 2018";

        CountDownLatch countDownLatch = new CountDownLatch(1);
        SubscriberManager.addSubscriber(str -> {
            try {
                // CODE=PB_03_2018|LAST_TRADING_DATE=15-03-2018|DELIVERY_DATE=17-03-2018|MARKET=PB|LABEL=Lead 13 March 2018|
                Map<Field, String> map = InstrumentUtils.toMap(str);
                Assert.assertEquals(code, map.get(Field.CODE));
                Assert.assertEquals(lastTradeDate, map.get(Field.LAST_TRADING_DATE));
                Assert.assertEquals(deliveryDate, map.get(Field.DELIVERY_DATE));
                Assert.assertEquals(market, map.get(Field.MARKET));
                Assert.assertEquals(label, map.get(Field.LABEL));

            } catch(ParseException|AssertionError e) {
                e.printStackTrace();
                assertTrue.set(false);
            } finally {
                countDownLatch.countDown();
            }
        });

        DataSourceChannel lmeTestFileChannel = new TestFileChannel(lmeDataFilePath);
        lmeTestFileChannel.setDataProviderListener(new LMEDataProviderListener());
        lmeTestFileChannel.start();

        if (!countDownLatch.await(TIME_OUT_SEC, TimeUnit.SECONDS)) {
            Assert.fail("unusual timeout");
        }

        if(!assertTrue.get()) {
            Assert.fail("Assert Fail");
        }
    }

}
