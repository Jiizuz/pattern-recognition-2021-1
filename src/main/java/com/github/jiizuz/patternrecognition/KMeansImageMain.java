package com.github.jiizuz.patternrecognition;

import com.github.jiizuz.patternrecognition.image.component.ImageComponent;
import com.github.jiizuz.patternrecognition.pattern.PixelPattern;
import com.github.jiizuz.patternrecognition.pattern.classification.KMeans;
import com.github.jiizuz.patternrecognition.pattern.util.PatternImageUtils;
import com.github.jiizuz.patternrecognition.pattern.util.PixelCentroid;
import com.google.common.collect.ListMultimap;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Main class of the project to initialize and run.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @since 1.7
 */
@UtilityClass
public class KMeansImageMain {

    // TODO: 9/11/21 Make a File chooser to select the image to read

    /**
     * Start location of the rendered images in the X axis.
     */
    private final int PAD_X = 69;

    /**
     * Name of the image to read.
     */
    private final String FILE_NAME = "anime-girl.jpg";

    /**
     * Amount of centroids to use in a K-Means.
     */
    private final int CENTROIDS = 3;

    /**
     * Runs the main instance of the project.
     *
     * @param args passed in the command line
     */
    public void main(final String[] args) throws IOException {
        final BufferedImage sourceImage = ImageIO.read(new File(FILE_NAME));

        final List<PixelPattern> pixels = PatternImageUtils.getPatterns(sourceImage);

        // K-Means

        final KMeans<PixelCentroid> kMeans = new KMeans<>(CENTROIDS, PixelCentroid::new);

        // train phase

        kMeans.train(pixels);

        // classification phase

        final ListMultimap<PixelCentroid, PixelPattern> classification = kMeans.classify(pixels);

        final BufferedImage resultImage = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        PatternImageUtils.writeCentroidPixels(classification, resultImage);

        // render comparison

        renderImage(sourceImage, 0);
        renderImage(resultImage, 1);
    }

    /**
     * Renders the specified image into a separately {@link JFrame}.
     *
     * @param image to render
     * @param index to locate over the frame on the X axis
     */
    public void renderImage(final @NonNull Image image, final int index) {
        final JFrame frame = new JFrame();

        final int height = image.getHeight(frame);
        final int width = image.getWidth(frame);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(PAD_X + width * index, 0);
        frame.setSize(width, height);
        frame.setResizable(false);

        frame.add(new ImageComponent(image));

        frame.setVisible(true);
    }

    // uhh... funny stuff i guess

    /**
     * Renders in a chart the distribution of the centroids per pixel.
     *
     * @param pixelMap with the pixels to render in the distribution
     * @throws NullPointerException     if the map is <tt>null</tt>
     * @throws IllegalArgumentException if the map is empty
     */
    public void renderCentroidsDistribution(final @NonNull ListMultimap<PixelCentroid, PixelPattern> pixelMap) {
        checkArgument(!pixelMap.isEmpty(), "empty pixel map");

        final XYSeriesCollection seriesCollection = new XYSeriesCollection();

        // get height

        final int height = pixelMap.values().stream().map(PixelPattern::getY)
                .max(Comparator.comparingInt(value -> value))
                .orElseThrow(() -> new Error("This should never happen")) + 1; // +1 for the index

        // pixelMap -> Series

        pixelMap.asMap().forEach((centroid, pixels) -> {
            final XYSeries series = new XYSeries(centroid.getId() + 1);

            pixels.forEach(pixel -> series.add(pixel.getX(), height - pixel.getY()));

            seriesCollection.addSeries(series);
        });

        // render series in chart

        final JFreeChart chart = ChartFactory.createScatterPlot("Centroids distribution", "X", "Y", seriesCollection);
        final ChartFrame frame = new ChartFrame("Distribution", chart);
        frame.pack();
        frame.setVisible(true);
    }
}
