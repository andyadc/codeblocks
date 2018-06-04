package com.andyadc.codeblocks.util.concurrent;

import org.slf4j.MDC;

import java.util.Map;

/**
 * Slf4j 异步线程设置 MDC
 *
 * @author andy.an
 * @since 2018/6/4
 */
public class MDCRunnable implements Runnable {

    private final Runnable runnable;
    private final Map<String, String> map;

    public MDCRunnable(Runnable runnable) {
        this.runnable = runnable;
        // 保存当前线程的MDC值
        this.map = MDC.getCopyOfContextMap();
    }

    @Override
    public void run() {
        // 传入已保存的MDC值
        for (Map.Entry<String, String> entry : map.entrySet()) {
            MDC.put(entry.getKey(), entry.getValue());
        }
        // 装饰器模式，执行run方法
        runnable.run();
        // 移除已保存的MDC值
        for (Map.Entry<String, String> entry : map.entrySet()) {
            MDC.remove(entry.getKey());
        }
    }
}
