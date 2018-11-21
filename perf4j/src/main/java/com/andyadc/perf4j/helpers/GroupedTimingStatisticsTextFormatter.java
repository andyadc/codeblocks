package com.andyadc.perf4j.helpers;

import com.andyadc.perf4j.GroupedTimingStatistics;

/**
 * GroupedTimingStatisticsFormatter that simply returns the toString() value of the GroupedTimingStatistics instance,
 * with a newline appended.
 *
 * @author Alex Devine
 */
public class GroupedTimingStatisticsTextFormatter implements GroupedTimingStatisticsFormatter {
    public String format(GroupedTimingStatistics stats) {
        return stats.toString() + MiscUtils.NEWLINE;
    }
}
