package com.tianjue.referencedatapublisher.downstreamapi;

import com.tianjue.referencedatapublisher.executor.ReferenceDataPublisherExecutor;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SubscriberManager {
    // subscriber manager; add/delete subscriber is rare so it is efficient to use here
    private static CopyOnWriteArrayList<Subscriber> subscribers = new CopyOnWriteArrayList<>();

    public static void publish(String data) {
        // we can define customized forkjoinpool to execute if want more customization
        subscribers.parallelStream().forEach(subscriber->subscriber.onData(data));
    }

    public static void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public static void removeAllSubscribers() {
        subscribers.clear();
    }
}
