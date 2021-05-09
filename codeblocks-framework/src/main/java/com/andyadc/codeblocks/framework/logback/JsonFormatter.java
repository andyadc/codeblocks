package com.andyadc.codeblocks.framework.logback;

import java.util.Map;

/**
 * A {@code JsonFormatter} formats a data {@link Map Map} into a JSON string.
 */
public interface JsonFormatter {

    /**
	 * Converts the specified map into a JSON string.
	 *
	 * @param m the map to be converted.
	 * @return a JSON String representation of the specified Map instance.
	 * @throws Exception if there is a problem converting the map to a String.
	 */
	String toJsonString(Map<String, Object> m) throws Exception;
}
