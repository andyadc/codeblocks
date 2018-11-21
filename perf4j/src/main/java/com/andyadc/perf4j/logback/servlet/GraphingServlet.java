package com.andyadc.perf4j.logback.servlet;

import com.andyadc.perf4j.chart.StatisticsChartGenerator;
import com.andyadc.perf4j.logback.GraphingStatisticsAppender;
import com.andyadc.perf4j.servlet.AbstractGraphingServlet;

import java.util.ArrayList;
import java.util.List;

/**
 * This graphing servlet implementation looks for graphs from {@link com.andyadc.perf4j.logback.GraphingStatisticsAppender}s that
 * have been created by the logback framework. Thus, in order to add live performance graphs to a web application, you
 * should first create the necessary GraphingStatisticsAppenders in your logback.xml config file. Then, you should create
 * an instance of this servlet in your web.xml file and set a "graphNames" init parameter to be a comma-separated list
 * of the appender names whose graphs you wish to display.
 *
 * @author Alex Devine
 * @author Xu Huisheng
 */
public class GraphingServlet extends AbstractGraphingServlet {

    private static final long serialVersionUID = 6378412967662499268L;

    /**
     * Finds the specified graph by using the
     * {@link com.andyadc.perf4j.logback.GraphingStatisticsAppender#getAppenderByName(String)} method to find the appender with
     * the specified name.
     *
     * @param name the name of the GraphingStatisticsAppender whose chart generator should be returned.
     * @return The specified chart generator, or null if no appender with the specified name was found.
     */
    protected StatisticsChartGenerator getGraphByName(String name) {
        GraphingStatisticsAppender appender = GraphingStatisticsAppender.getAppenderByName(name);

        return (appender == null) ? null : appender.getChartGenerator();
    }

    /**
     * This method looks for all known GraphingStatisticsAppenders and returns their names.
     *
     * @return The list of known GraphingStatisticsAppender names.
     */
    protected List<String> getAllKnownGraphNames() {
        List<String> retVal = new ArrayList<>();

        for (GraphingStatisticsAppender appender : GraphingStatisticsAppender.getAllGraphingStatisticsAppenders()) {
            retVal.add(appender.getName());
        }

        return retVal;
    }
}
