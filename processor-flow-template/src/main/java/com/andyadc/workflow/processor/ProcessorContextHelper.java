package com.andyadc.workflow.processor;

public final class ProcessorContextHelper {

	private static final ThreadLocal<ProcessorContext> processorContextThreadLocal = new ThreadLocal<>();

	public static void addProcessorContext(ProcessorContext context) {
		processorContextThreadLocal.set(context);
	}

	public static ProcessorContext getProcessorContext() {
		ProcessorContext context = processorContextThreadLocal.get();
		if (context == null) {
			context = new ProcessorContext();
			processorContextThreadLocal.set(context);
		}
		return context;
	}

	public static void remove() {
		processorContextThreadLocal.remove();
	}
}
