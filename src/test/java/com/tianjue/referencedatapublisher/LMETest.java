package com.tianjue.referencedatapublisher;


import com.tianjue.referencedatapublisher.config.LMEConfig;
import com.tianjue.referencedatapublisher.coreprocessor.ReferenceDataManager;
import com.tianjue.referencedatapublisher.downstreamapi.SubscriberManager;
import com.tianjue.referencedatapublisher.exception.ParseException;
import com.tianjue.referencedatapublisher.executor.AvailableExecutorMappingFunction;
import com.tianjue.referencedatapublisher.upstreamchannel.listener.DataProviderListener;
import com.tianjue.referencedatapublisher.upstreamchannel.listener.LMEDataProviderListener;
import com.tianjue.referencedatapublisher.executor.ReferenceDataPublisherExecutor;
import com.tianjue.referencedatapublisher.instrument.Field;
import org.junit.*;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.tianjue.referencedatapublisher.TestUtils.TIME_OUT_SEC;

public class LMETest {
    private DataProviderListener listener = new LMEDataProviderListener();

    // the reason to have assertTrue is to pass the assert status from subscriber thread to TestSuite Thread, so that it becomes obvious for test case status
    AtomicBoolean assertTrue = new AtomicBoolean(true);

    @BeforeClass
    public static void beforeClass() {
        ReferenceDataPublisherExecutor.registerExecutorHashFunction(AvailableExecutorMappingFunction.STRING_HASH_CODE_FUNCTION);
        LMEConfig.setRules();
    }

    @Before
    public void before() {
        assertTrue.set(true);
    }

    @After
    public void after() {
        SubscriberManager.removeAllSubscribers();
        ReferenceDataManager.clear();
    }

    @Test
    public void When_lme_does_not_publish_code_Then_it_throws_exception() throws InterruptedException {
        SubscriberManager.addSubscriber(str -> {
        });
        CountDownLatch countDownLatch = new CountDownLatch(1);

        String code = null;
        String lastTradeDate = "15-03-2018";
        String deliveryDate = "17-03-2018";
        String market = "PB";
        String label = "Lead 13 March 18";
        String exchangeCode = null;
        String tradable = "FALSE";

        TestUtils.upstreamPublishInstrument(listener, code, lastTradeDate, deliveryDate, market, label, exchangeCode, tradable, (str, e) -> {
                    Assert.assertTrue(e.getMessage().contains("Cannot find CODE field"));
                    countDownLatch.countDown();
                }
        );
        if (!countDownLatch.await(TIME_OUT_SEC, TimeUnit.SECONDS)) {
            Assert.fail("unusual timeout");
        }
    }

    @Test
    public void When_lme_does_not_publish_tradable_Then_subscriber_should_get_tradable_as_true() throws InterruptedException {
        String code = "PB_03_2018";
        String lastTradeDate = "15-03-2018";
        String deliveryDate = "17-03-2018";
        String market = "PB";
        String label = "Lead 13 March 18";
        String tradable = null;

        CountDownLatch countDownLatch = new CountDownLatch(1);
        assertPublishInstrument(countDownLatch, code, lastTradeDate, deliveryDate, market, label, "TRUE");
        TestUtils.upstreamPublishInstrument(listener, code, lastTradeDate, deliveryDate, market, label, null, tradable, null);

        if (!countDownLatch.await(TIME_OUT_SEC, TimeUnit.SECONDS)) {
            Assert.fail("unusual timeout");
        }
    }

    @Test
    public void When_lme_publish_PB_03_2018_And_publish_PB_04_2018_Then_subscriber_gets_both_correctly() throws InterruptedException {
        String code1 = "PB_03_2018";
        String lastTradeDate1 = "15-03-2018";
        String deliveryDate1 = "17-03-2018";
        String market1 = "PB";
        String label1 = "Lead 13 March 18";
        String exchangeCode1 = null;
        String tradable1 = null;

        When_publish_Then_assert_subscription(code1, lastTradeDate1, deliveryDate1, market1, label1, exchangeCode1, tradable1, "TRUE");

        String code2 = "PB_04_2018";
        String lastTradeDate2 = "15-04-2018";
        String deliveryDate2 = "17-04-2018";
        String market2 = "PB";
        String label2 = "Lead 13 Apr 18";
        String exchangeCode2 = null;
        String tradable2 = null;

        When_publish_Then_assert_subscription(code2, lastTradeDate2, deliveryDate2, market2, label2, exchangeCode2, tradable2, "TRUE");
    }

    private void When_publish_Then_assert_subscription(String code, String lastTradeDate, String deliveryDate, String market, String label,
                                                       String exchangeCode, String inputTradable, String subscriptionTradableValue) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        SubscriberManager.addSubscriber(str ->
        {
            try {
                assertSubscriberString(code, lastTradeDate, deliveryDate, market, label, subscriptionTradableValue, str);
            } catch (ParseException | AssertionError e) {
                System.out.println(e.getMessage());
                assertTrue.set(false);
            } finally {
                countDownLatch.countDown();
            }
        });
        TestUtils.upstreamPublishInstrument(listener, code, lastTradeDate, deliveryDate, market, label, exchangeCode, inputTradable, null);
        if (!countDownLatch.await(TIME_OUT_SEC, TimeUnit.SECONDS)) {
            Assert.fail("unusual timeout");
        }
        if (!assertTrue.get()) {
            Assert.fail("Assert Error in Subscriber");
        }
        SubscriberManager.removeAllSubscribers();
    }

    private void assertSubscriberString(String code, String lastTradeDate, String deliveryDate, String market, String label, String tradable, String str) throws ParseException {
        Map<Field, String> dataMap = TestUtils.decode(str);
        Assert.assertEquals(code, dataMap.get(Field.CODE));
        Assert.assertEquals(lastTradeDate, dataMap.get(Field.LAST_TRADING_DATE));
        Assert.assertEquals(deliveryDate, dataMap.get(Field.DELIVERY_DATE));
        Assert.assertEquals(market, dataMap.get(Field.MARKET));
        Assert.assertEquals(label, dataMap.get(Field.LABEL));
        Assert.assertEquals(tradable, dataMap.get(Field.TRADABLE));
    }


    private void assertPublishInstrument(CountDownLatch countDownLatch, String code, String lastTradeDate, String deliveryDate, String market, String label, String tradable) throws InterruptedException {

        SubscriberManager.addSubscriber(str ->
        {
            try {
                assertSubscriberString(code, lastTradeDate, deliveryDate, market, label, tradable, str);
            } catch (ParseException e) {
                Assert.fail("Catch exception " + e.getMessage());
            } finally {
                countDownLatch.countDown();
            }
        });
    }
}
