package com.andyadc.codeblocks.framework.kafka.logback.delivery;

@FunctionalInterface
public interface FailedDeliveryCallback<E> {
    void onFailedDelivery(E evt, Throwable throwable);
}
