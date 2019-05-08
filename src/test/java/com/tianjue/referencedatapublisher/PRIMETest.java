package com.tianjue.referencedatapublisher;


import com.tianjue.referencedatapublisher.config.PRIMEConfig;
import com.tianjue.referencedatapublisher.coreprocessor.ReferenceDataManager;
import com.tianjue.referencedatapublisher.downstreamapi.SubscriberManager;
import com.tianjue.referencedatapublisher.exception.ParseException;
import com.tianjue.referencedatapublisher.executor.AvailableExecutorMappingFunction;
import com.tianjue.referencedatapublisher.executor.ReferenceDataPublisherExecutor;
import com.tianjue.referencedatapublisher.instrument.Field;
import com.tianjue.referencedatapublisher.upstreamchannel.listener.DataProviderListener;
import com.tianjue.referencedatapublisher.upstreamchannel.listener.PRIMEDataProviderListener;
import org.junit.*;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.tianjue.referencedatapublisher.TestUtils.TIME_OUT_SEC;

/**
 * Unit test for simple ReferenceDataPublisher.
 */
public class PRIMETest {
    private DataProviderListener primeDataProviderListener = new PRIMEDataProviderListener();

    // the reason to have assertTrue is to pass the assert status from subscriber thread to TestSuite Thread, so that it becomes obvious for test case status
    AtomicBoolean assertTrue = new AtomicBoolean(true);

    @BeforeClass
    public static void beforeClass() {
        ReferenceDataPublisherExecutor.registerExecutorHashFunction(AvailableExecutorMappingFunction.STRING_HASH_CODE_FUNCTION);
        PRIMEConfig.setRules();
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
    public void When_prime_does_not_publish_exchange_code_Then_it_should_throw_exception_with_cannot_get_matching_code() throws InterruptedException {
        SubscriberManager.addSubscriber(str -> {
        });
        CountDownLatch countDownLatch = new CountDownLatch(1);

        String code = "PRIME_PB_03_2018";
        String lastTradeDate = "15-03-2018";
        String deliveryDate = "17-03-2018";
        String market = "PB";
        String label = "Lead 13 March 18";
        String exchangeCode = null;
        String tradable = "FALSE";

        TestUtils.upstreamPublishInstrument(primeDataProviderListener, code, lastTradeDate, deliveryDate, market, label, exchangeCode, tradable, (str, e) -> {
                    try {
                        Assert.assertTrue(e.getMessage().contains("cannot get matching code"));
                    } catch (AssertionError error) {
                        assertTrue.set(false);
                    } finally {
                        countDownLatch.countDown();
                    }
                }
        );
        if (!countDownLatch.await(TIME_OUT_SEC, TimeUnit.SECONDS)) {
            Assert.fail("unusual timeout");
        }
        if (!assertTrue.get()) {
            Assert.fail("Assert Error in Subscriber");
        }
    }

    @Test
    public void When_prime_publishes_normally_Then_subscriber_get_code_from_exchange_code() throws InterruptedException {
        String code = "PRIME_PB_03_2018";
        String lastTradeDate = "15-03-2018";
        String deliveryDate = "17-03-2018";
        String market = "PB";
        String label = "Lead 13 March 18";
        String exchangeCode = "PB_03_2018";
        String tradable = "FALSE";

        When_publish_Then_assert_subscription(code, lastTradeDate, deliveryDate, market, label, exchangeCode, tradable);
    }

    @Test
    public void When_prime_publish_PB_03_2018_And_publish_PB_04_2018_Then_subscriber_gets_both_correctly() throws InterruptedException {
        String code1 = "PRIME_PB_03_2018";
        String lastTradeDate1 = "15-03-2018";
        String deliveryDate1 = "17-03-2018";
        String market1 = "PB";
        String label1 = "Lead 13 March 18";
        String exchangeCode1 = "PB_03_2018";
        String tradable1 = "FALSE";

        When_publish_Then_assert_subscription(code1, lastTradeDate1, deliveryDate1, market1, label1, exchangeCode1, tradable1);

        String code2 = "PRIME_PB_04_2018";
        String lastTradeDate2 = "15-04-2018";
        String deliveryDate2 = "17-04-2018";
        String market2 = "PB2";
        String label2 = "Lead 13 Apr 18";
        String exchangeCode2 = "PB_04_2018";
        String tradable2 = "FALSE";
        When_publish_Then_assert_subscription(code2, lastTradeDate2, deliveryDate2, market2, label2, exchangeCode2, tradable2);
    }

    @Test
    public void When_prime_publish_tradable_as_false_And_publish_tradable_as_true_Then_subscriber_gets_tradable_finally() throws InterruptedException {
        String code1 = "PRIME_PB_03_2018";
        String lastTradeDate1 = "15-03-2018";
        String deliveryDate1 = "17-03-2018";
        String market1 = "PB";
        String label1 = "Lead 13 March 18";
        String exchangeCode1 = "PB_03_2018";
        String tradable1 = "FALSE";

        When_publish_Then_assert_subscription(code1, lastTradeDate1, deliveryDate1, market1, label1, exchangeCode1, tradable1);

        String tradable2 = "TRUE";
        When_publish_Then_assert_subscription(code1, lastTradeDate1, deliveryDate1, market1, label1, exchangeCode1, tradable2);
    }

    private void When_publish_Then_assert_subscription(String code, String lastTradeDate, String deliveryDate, String market, String label, String exchangeCode, String tradable1) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        SubscriberManager.addSubscriber(str ->
        {
            try {
                assertSubscriptionString(lastTradeDate, deliveryDate, market, label, exchangeCode, tradable1, str);
            } catch (ParseException | AssertionError e) {
                System.out.println(e.getMessage());
                assertTrue.set(false);
            } finally {
                countDownLatch.countDown();
            }
        });
        TestUtils.upstreamPublishInstrument(primeDataProviderListener, code, lastTradeDate, deliveryDate, market, label, exchangeCode, tradable1, null);
        if (!countDownLatch.await(TIME_OUT_SEC, TimeUnit.SECONDS)) {
            Assert.fail("unusual timeout");
        }
        if (!assertTrue.get()) {
            Assert.fail("Assert Error in Subscriber");
        }
        SubscriberManager.removeAllSubscribers();
    }

    private void assertSubscriptionString(String lastTradeDate, String deliveryDate, String market, String label, String exchangeCode, String tradable, String str) throws ParseException {
        Map<Field, String> dataMap = TestUtils.decode(str);
        Assert.assertEquals(exchangeCode, dataMap.get(Field.CODE)); // exchange code rather than code
        Assert.assertEquals(lastTradeDate, dataMap.get(Field.LAST_TRADING_DATE));
        Assert.assertEquals(deliveryDate, dataMap.get(Field.DELIVERY_DATE));
        Assert.assertEquals(market, dataMap.get(Field.MARKET));
        Assert.assertEquals(label, dataMap.get(Field.LABEL));
        Assert.assertEquals(exchangeCode, dataMap.get(Field.EXCHANGE_CODE));
        Assert.assertEquals(tradable, dataMap.get(Field.TRADABLE));
    }
}
