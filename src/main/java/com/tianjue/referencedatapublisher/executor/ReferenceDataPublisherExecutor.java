package com.tianjue.referencedatapublisher.executor;

import com.tianjue.referencedatapublisher.config.SystemConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ReferenceDataPublisherExecutor {

    private static final List<ThreadPoolExecutor> processorThreadPoolExecutor = new CopyOnWriteArrayList<>();

    static {
        createExecutors();
    }

    static void createExecutors() {
        for (int i = 0; i < SystemConfig.PROCESSOR_THREAD_COUNT; i++) {
            processorThreadPoolExecutor.add(createSingleThreadExecutor());
        }
    }

    private static ThreadPoolExecutor createSingleThreadExecutor() {
        // one thread used for IO
        return new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    static private ExecutorMappingFunction executorMappingFunction;

    // for test
    static void unregisterExecutorHashFunction() {
        executorMappingFunction = null;
    }

    public static void registerExecutorHashFunction(ExecutorMappingFunction hashFunction) {
        executorMappingFunction = hashFunction;
    }

    public static ThreadPoolExecutor getProcessorExecutor(String str) {
        if (executorMappingFunction != null) {
            return getProcessorExecutor(executorMappingFunction.getExecutor(str));
        }
        // default return the first executor
        return getProcessorExecutor(0);
    }

    public static void shutdownNow() {
        for(ThreadPoolExecutor executor: processorThreadPoolExecutor) {
            executor.shutdownNow();
        }
        processorThreadPoolExecutor.clear();
    }

    public static void awaitTermination(long sec) throws InterruptedException {
        for (ThreadPoolExecutor executor : processorThreadPoolExecutor) {
            executor.awaitTermination(sec, TimeUnit.SECONDS);
        }
    }

    static ThreadPoolExecutor getProcessorExecutor(int index) {
        return processorThreadPoolExecutor.get(index % (processorThreadPoolExecutor.size()));
    }
}
