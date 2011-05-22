package org.warblr.tools;

import net.bluecow.spectro.Clip;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.warblr.core.analysis.IndexedClip;
import org.warblr.core.analysis.SearchClip;
import org.warblr.core.matching.ClipMatch;
import org.warblr.core.matching.MatcherService;
import org.warblr.core.util.TestClipUtils;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ian
 * Date: 21/05/11
 * Time: 17:24
 * To change this template use File | Settings | File Templates.
 */
public class ViewPeakCharts {

    public static void main(String[] args) throws Throwable, IOException, LineUnavailableException {
        Clip testClip = TestClipUtils.getTestSearchClip("gardenbirds", "lesser-spotted-woodpecker-drum.wav");
        List<ClipMatch> matches = MatcherService.getNearestPeakMatches(testClip, false);

        for(ClipMatch clipMatch: matches) {
            XYSeriesCollection dataset = new XYSeriesCollection();

            SearchClip searchClip = clipMatch.getSearchClip();
            XYSeries searchClipSeries = new XYSeries(searchClip.getClip().getName() + "(Search)");
            dataset.addSeries(searchClipSeries);


            for(int searchFrameIndex = 0; searchFrameIndex < searchClip.getNumOfFrames(); searchFrameIndex++) {
                for(int freqIndex = searchClip.getLowPassFrequencyThreshold(); freqIndex < searchClip.getHighPassFrequencyThreshold(); freqIndex++) {
                    if(searchClip.isPeak(searchFrameIndex, freqIndex)) {
                        searchClipSeries.add(searchFrameIndex, freqIndex);
                    }
                }
            }

            IndexedClip indexedClip = clipMatch.getIndexedClip();
            XYSeries indexedClipSeries = new XYSeries(indexedClip.getClip().getName() + "(Indexed Result)");
            dataset.addSeries(indexedClipSeries);


            for(IndexedClip.IndexedClipPeak indexedClipPeak: indexedClip.getPeaks()) {
                indexedClipSeries.add(clipMatch.getIndexFrame() + indexedClipPeak.getFrame(), indexedClipPeak.getFrequency());
            }

            JFreeChart chart = createChart(dataset, "Match Result (" + clipMatch.getMatchPercentage() + "%)");
            ChartPanel chartPanel = new ChartPanel(chart);

            JFrame frame = new JFrame();
            frame.getContentPane().add(chartPanel);
            frame.setSize(1000,700);
            frame.setVisible(true);

            if(clipMatch.getIndexedClip().getClip().getName().startsWith(testClip.getName().split("-")[0])) {
                //Only show results that are better than or are the correct bird
                break;
            }
        }
    }

    private static JFreeChart createChart(XYDataset dataset, String title) {
        JFreeChart chart = ChartFactory.createScatterPlot(title,
                "X", "Y", dataset, PlotOrientation.VERTICAL, true, false, false);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setNoDataMessage("NO DATA");
        plot.setDomainZeroBaselineVisible(true);
        plot.setRangeZeroBaselineVisible(true);

        XYLineAndShapeRenderer renderer
                = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesOutlinePaint(0, Color.black);
        renderer.setUseOutlinePaint(true);
//        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
//        domainAxis.setAutoRangeIncludesZero(false);
//        domainAxis.setTickMarkInsideLength(2.0f);
//        domainAxis.setTickMarkOutsideLength(0.0f);
//
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(0, rangeAxis.getUpperBound());
//        rangeAxis.setTickMarkInsideLength(2.0f);
//        rangeAxis.setTickMarkOutsideLength(0.0f);

        return chart;
    }
}
