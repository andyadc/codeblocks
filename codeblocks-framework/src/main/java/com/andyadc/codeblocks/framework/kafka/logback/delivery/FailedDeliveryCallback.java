package com.andyadc.codeblocks.framework.kafka.logback.delivery;

/**
 * @author andy.an
 * @since 2018/12/6
 */
@FunctionalInterface
public interface FailedDeliveryCallback<E> {
    void onFailedDelivery(E evt, Throwable throwable);
}
