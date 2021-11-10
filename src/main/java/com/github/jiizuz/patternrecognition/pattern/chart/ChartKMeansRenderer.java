package com.github.jiizuz.patternrecognition.pattern.chart;

import com.github.jiizuz.patternrecognition.pattern.Pattern;
import com.github.jiizuz.patternrecognition.pattern.display.KMeansRenderer;
import com.github.jiizuz.patternrecognition.pattern.util.Centroid;
import com.google.common.collect.ListMultimap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * {@link KMeansRenderer} that uses a {@link JFreeChart} in order
 * to display the results by generating a scatter chart based on
 * the centroid of the patterns.
 *
 * <p>A {@link ChartFrame} is used to pack and display the chart.
 *
 * <p><b>IMPORTANT:</b> Only the first two characteristics of the
 * patterns will be considered, this means, the patterns passed
 * in the accumulation must have at least 2 characteristics, and
 * it is recommended to only have 2.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @see com.github.jiizuz.patternrecognition.pattern.display.KMeansRenderer
 * @since 1.6
 */
@Builder
@AllArgsConstructor
public class ChartKMeansRenderer implements KMeansRenderer {

    /**
     * Default title to set on the <tt>Chart</tt>.
     */
    private static final String CHART_TITLE = "KMeans clusters";

    /**
     * Default label for the <tt>X axis</tt> on the chart.
     */
    private static final String X_AXIS_LABEL = "X";

    /**
     * Default label for the <tt>Y axis</tt> on the chart.
     */
    private static final String Y_AXIS_LABEL = "Y";

    /**
     * Default title to set on the <tt>Frame</tt>.
     */
    private static final String FRAME_TITLE = "KMeans results";

    /**
     * Title to set on the <tt>Chart</tt>.
     */
    @Builder.Default
    private final String chartTitle = CHART_TITLE;

    /**
     * Label for the <tt>X axis</tt> on the chart.
     */
    @Builder.Default
    private final String xAxisLabel = X_AXIS_LABEL;

    /**
     * Label for the <tt>Y axis</tt> on the chart.
     */
    @Builder.Default
    private final String yAxisLabel = Y_AXIS_LABEL;

    /**
     * Title to set on the <tt>Frame</tt>.
     */
    @Builder.Default
    private final String frameTitle = FRAME_TITLE;

    /**
     * {@link XYSeriesCollection} to accumulate on the series of the centroids per pattern.
     */
    private final XYSeriesCollection seriesCollection = new XYSeriesCollection();

    /**
     * {@inheritDoc}
     */
    @Override
    public void accumulate(final @NonNull ListMultimap<? extends Centroid, ? extends Pattern> centroidMap) {
        checkArgument(!centroidMap.isEmpty(), "Empty centroid map");

        centroidMap.asMap().forEach((centroid, patterns) -> {
            final XYSeries series = new XYSeries(centroid.getId() + 1);

            patterns.stream().map(Pattern::getVector).forEach(vector ->
                    series.add(vector[0], vector[1])); // throw exception if not at least 2 elements

            seriesCollection.addSeries(series);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderResults() {
        final JFreeChart chart = ChartFactory.createScatterPlot(chartTitle, xAxisLabel, yAxisLabel, seriesCollection);
        final ChartFrame frame = new ChartFrame(frameTitle, chart);
        frame.pack();
        frame.setVisible(true);
    }
}
