package com.andyadc.codeblocks.framework.profiler;

/**
 * profiler的开关这是，目前仅仅是一个静态单例，需要外部来触发修改
 */
public class ProfilerSwitch {

	private static ProfilerSwitch instance = new ProfilerSwitch();
	/**
	 * 是否打开打印日志的开关
	 */
	private boolean openProfilerTree = true;
	/**
	 * 超时时间
	 */
	private long invokeTimeout = 100;
	/**
	 * 是否打印纳秒
	 */
	private boolean openProfilerNanoTime = false;

	public static ProfilerSwitch getInstance() {
		return instance;
	}

	public boolean isOpenProfilerTree() {
		return openProfilerTree;
	}

	public void setOpenProfilerTree(boolean openProfilerTree) {
		this.openProfilerTree = openProfilerTree;
	}

	public long getInvokeTimeout() {
		return invokeTimeout;
	}

	public void setInvokeTimeout(long invokeTimeout) {
		this.invokeTimeout = invokeTimeout;
	}

	public boolean isOpenProfilerNanoTime() {
		return openProfilerNanoTime;
	}

	public void setOpenProfilerNanoTime(boolean openProfilerNanoTime) {
		this.openProfilerNanoTime = openProfilerNanoTime;
	}
}
