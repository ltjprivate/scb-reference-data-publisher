package com.tianjue.referencedatapublisher.downstreamapi;

import com.tianjue.referencedatapublisher.config.SystemConfig;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static com.tianjue.referencedatapublisher.TestUtils.TIME_OUT_SEC;

public class SubscriberManagerTest {

    class TestSubscriber implements Subscriber {
        AtomicLong threadId;
        CountDownLatch countDownLatch;

        TestSubscriber(AtomicLong threadId, CountDownLatch countDownLatch) {
            this.threadId = threadId;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void onData(String str) {
            threadId.set(Thread.currentThread().getId());
            countDownLatch.countDown();
        }
    }
}
