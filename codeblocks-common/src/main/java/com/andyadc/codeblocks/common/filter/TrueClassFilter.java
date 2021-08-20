package com.andyadc.codeblocks.common.filter;

public class TrueClassFilter implements ClassFilter {

	/**
	 * Singleton {@link TrueClassFilter} instance
	 */
	public static final TrueClassFilter INSTANCE = new TrueClassFilter();

	private TrueClassFilter() {
	}

	@Override
	public boolean accept(Class<?> filteredObject) {
		return true;
	}
}
