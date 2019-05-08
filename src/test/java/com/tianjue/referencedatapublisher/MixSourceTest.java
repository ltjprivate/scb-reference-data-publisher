package com.tianjue.referencedatapublisher;


import com.tianjue.referencedatapublisher.config.LMEConfig;
import com.tianjue.referencedatapublisher.config.PRIMEConfig;
import com.tianjue.referencedatapublisher.coreprocessor.ReferenceDataManager;
import com.tianjue.referencedatapublisher.downstreamapi.SubscriberManager;
import com.tianjue.referencedatapublisher.exception.ParseException;
import com.tianjue.referencedatapublisher.executor.AvailableExecutorMappingFunction;
import com.tianjue.referencedatapublisher.executor.ReferenceDataPublisherExecutor;
import com.tianjue.referencedatapublisher.instrument.Field;
import com.tianjue.referencedatapublisher.upstreamchannel.listener.DataProviderListener;
import com.tianjue.referencedatapublisher.upstreamchannel.listener.LMEDataProviderListener;
import com.tianjue.referencedatapublisher.upstreamchannel.listener.PRIMEDataProviderListener;
import org.junit.*;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.tianjue.referencedatapublisher.TestUtils.TIME_OUT_SEC;

public class MixSourceTest {
    private DataProviderListener lmeDataProviderListener = new LMEDataProviderListener();
    private DataProviderListener primeDataProviderListener = new PRIMEDataProviderListener();

    private AtomicBoolean assertTrue = new AtomicBoolean(true);

    @BeforeClass
    public static void beforeClass() {
        ReferenceDataPublisherExecutor.registerExecutorHashFunction(AvailableExecutorMappingFunction.STRING_HASH_CODE_FUNCTION);
        LMEConfig.setRules();
        PRIMEConfig.setRules();
    }

    @Before
    public void before() {
        assertTrue.set(true);
    }

    @After
    public void after() {
        if(!assertTrue.get()) {
            Assert.fail("assert fail in subscriber");
        }

        SubscriberManager.removeAllSubscribers();
        ReferenceDataManager.clear();
    }

    @Test
    public void Given_lme_does_not_publish_tradable_When_prime_publish_tradable_as_false_Then_subscriber_should_get_tradable_as_false() throws InterruptedException {
        // lme publish data
        String code = "PB_03_2018";
        String lastTradeDate = "15-03-2018";
        String deliveryDate = "17-03-2018";
        String market = "PB";
        String label = "Lead 13 March 18";
        String exchangeCode = "PB_03_2018";
        String tradable = "TRUE";

        CountDownLatch countDownLatch = new CountDownLatch(2);
        addAssertSubscriber(countDownLatch, assertTrue, code, lastTradeDate, deliveryDate, market, label, "FALSE");
        TestUtils.upstreamPublishInstrument(lmeDataProviderListener, code, lastTradeDate, deliveryDate, market, label, null, tradable, null);

        code = "PRIME_PB_03_2018";

        tradable = "FALSE";
        TestUtils.upstreamPublishInstrument(primeDataProviderListener, code, lastTradeDate, deliveryDate, market, label, exchangeCode, tradable, null);

        if (!countDownLatch.await(TIME_OUT_SEC, TimeUnit.SECONDS)) {
            Assert.fail("unusual timeout");
        }
    }

    @Test
    public void Given_lme_publishes_first_When_prime_publishes_different_trade_date_Then_subscriber_should_get_lme_trade_date() throws InterruptedException {
        String code = "PB_03_2018";
        String lastTradeDate = "15-03-2018";
        String deliveryDate = "17-03-2018";
        String market = "PB";
        String label = "Lead 13 March 18";
        String exchangeCode = "PB_03_2018";
        String tradable = null;

        CountDownLatch countDownLatch = new CountDownLatch(2);
        addAssertSubscriber(countDownLatch, assertTrue, code, lastTradeDate, deliveryDate, market, label, "TRUE");
        TestUtils.upstreamPublishInstrument(lmeDataProviderListener, code, lastTradeDate, deliveryDate, market, label, null, tradable, null);

        String primeLastTradeDate = "14-03-2018";
        String primeDeliveryDate = "18-03-2018";
        TestUtils.upstreamPublishInstrument(primeDataProviderListener, code, primeLastTradeDate, primeDeliveryDate, market, label, exchangeCode, tradable, null);

        if (!countDownLatch.await(TIME_OUT_SEC, TimeUnit.SECONDS)) {
            Assert.fail("unusual timeout");
        }
    }

    @Test
    public void Given_prime_publishes_first_When_lme_publishes_different_trade_date_Then_subscriber_should_get_lme_trade_date() throws InterruptedException {
        String code = "PB_03_2018";
        String lastTradeDate = "15-03-2018";
        String deliveryDate = "17-03-2018";
        String market = "PB";
        String label = "Lead 13 March 18";
        String exchangeCode = "PB_03_2018";
        String tradable = "FALSE";

        String lmeLastTradeDate = "14-03-2018";
        String lmeDeliveryDate = "18-03-2018";

        CountDownLatch countDownLatch = new CountDownLatch(2);
        addAssertSubscriber(countDownLatch, assertTrue, code, lmeLastTradeDate, lmeDeliveryDate, market, label, tradable);
        TestUtils.upstreamPublishInstrument(primeDataProviderListener, code, lastTradeDate, deliveryDate, market, label, exchangeCode, tradable, null);
        TestUtils.upstreamPublishInstrument(lmeDataProviderListener, code, lmeLastTradeDate, lmeDeliveryDate, market, label, null, null, null);

        if (!countDownLatch.await(TIME_OUT_SEC, TimeUnit.SECONDS)) {
            Assert.fail("unusual timeout");
        }
    }


    private void addAssertSubscriber(CountDownLatch countDownLatch, AtomicBoolean assertTrue, String code, String lastTradeDate, String deliveryDate, String market, String label, String tradable) throws InterruptedException {

        SubscriberManager.addSubscriber(str ->
        {
            try {
                if (countDownLatch.getCount() == 1) {
                    Map<Field, String> dataMap = TestUtils.decode(str);
                    Assert.assertEquals(code, dataMap.get(Field.CODE));
                    Assert.assertEquals(lastTradeDate, dataMap.get(Field.LAST_TRADING_DATE));
                    Assert.assertEquals(deliveryDate, dataMap.get(Field.DELIVERY_DATE));
                    Assert.assertEquals(market, dataMap.get(Field.MARKET));
                    Assert.assertEquals(label, dataMap.get(Field.LABEL));
                    Assert.assertEquals(tradable, dataMap.get(Field.TRADABLE));
                }
            } catch (ParseException|AssertionError e) {
                e.printStackTrace();
                assertTrue.set(false);
            } finally {
                countDownLatch.countDown();
            }
        });
    }
}
