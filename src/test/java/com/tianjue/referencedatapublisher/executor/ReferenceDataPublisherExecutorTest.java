package com.tianjue.referencedatapublisher.executor;

import org.junit.*;

public class ReferenceDataPublisherExecutorTest {

    @After
    public void after() {
        ReferenceDataPublisherExecutor.unregisterExecutorHashFunction();
    }

    @Test
    public void When_same_code_Then_it_maps_to_same_thread_pool_executor() {
        ReferenceDataPublisherExecutor.registerExecutorHashFunction(AvailableExecutorMappingFunction.STRING_HASH_CODE_FUNCTION);
        String code1 = "CODE_1";
        String code2 = "CODE_1";
        Assert.assertEquals(ReferenceDataPublisherExecutor.getProcessorExecutor(code1), ReferenceDataPublisherExecutor.getProcessorExecutor(code2));
    }

    @Test
    public void When_different_code_Then_it_maps_to_different_thread_pool_executor() {
        ReferenceDataPublisherExecutor.registerExecutorHashFunction(AvailableExecutorMappingFunction.STRING_HASH_CODE_FUNCTION);
        String code1 = "CODE_1";
        String code2 = "CODE_2";
        Assert.assertNotEquals(ReferenceDataPublisherExecutor.getProcessorExecutor(code1), ReferenceDataPublisherExecutor.getProcessorExecutor(code2));
    }

    @Test
    public void Given_no_executor_hash_function_Then_it_has_executor_to_use() {
        String code1 = "CODE_1";
        Assert.assertNotNull(ReferenceDataPublisherExecutor.getProcessorExecutor(code1));
    }
}
