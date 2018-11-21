package com.andyadc.perf4j.helpers;

import com.andyadc.perf4j.GroupedTimingStatistics;

/**
 * Can format a {@link com.andyadc.perf4j.GroupedTimingStatistics} instance to a String.
 *
 * @author Alex Devine
 */
public interface GroupedTimingStatisticsFormatter {
    /**
     * Outputs the specified GroupedTimingStatistics instance as a String
     *
     * @param stats The GroupedTimingStatistics instance to format
     * @return The stringified GroupedTimingStatistics
     */
    String format(GroupedTimingStatistics stats);
}
