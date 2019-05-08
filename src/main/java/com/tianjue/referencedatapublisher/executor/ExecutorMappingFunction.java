package com.tianjue.referencedatapublisher.executor;

@FunctionalInterface
public interface ExecutorMappingFunction {
    int getExecutor(String str);
}
