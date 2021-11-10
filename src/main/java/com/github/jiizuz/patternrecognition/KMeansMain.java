package com.github.jiizuz.patternrecognition;

import com.github.jiizuz.patternrecognition.pattern.Pattern;
import com.github.jiizuz.patternrecognition.pattern.chart.ChartKMeansRenderer;
import com.github.jiizuz.patternrecognition.pattern.chart.PrintStreamKMeansRenderer;
import com.github.jiizuz.patternrecognition.pattern.classification.KMeans;
import com.github.jiizuz.patternrecognition.pattern.display.KMeansRenderer;
import com.github.jiizuz.patternrecognition.pattern.filter.ListPatternFilterBuilder;
import com.github.jiizuz.patternrecognition.pattern.filter.PatternFilter;
import com.github.jiizuz.patternrecognition.pattern.parser.CsvParser;
import com.github.jiizuz.patternrecognition.pattern.util.Centroid;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ListMultimap;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.util.List;

/**
 * Main class of the project to initialize and run.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @since 1.5
 */
@UtilityClass
public class KMeansMain {

    /**
     * Name of the file to parse from example data.
     */
    private final String FILE_NAME = "iris.csv";

    /**
     * Amount of centroids to use in a K-Means.
     */
    private final int CENTROIDS = 3;

    /**
     * Runs the main instance of the project.
     *
     * @param args passed in the command line
     */
    public void main(String[] args) throws IOException {
        final List<Pattern> sourcePatterns = CsvParser.parsePatternsFromCsv(FILE_NAME);

        final PatternFilter filter = ListPatternFilterBuilder.create()
                .lastN(2)
                .build();

        final List<Pattern> filteredPatterns = filter.filterCopy(sourcePatterns);

        final KMeans<Centroid> kMeans = new KMeans<>(CENTROIDS, Centroid::new);

        // train phase

        kMeans.train(filteredPatterns);

        // classify phase

        final ListMultimap<Centroid, Pattern> classify = kMeans.classify(filteredPatterns);

        // render phase

        final List<KMeansRenderer> renderers = ImmutableList.of(
                PrintStreamKMeansRenderer.builder().build(),
                ChartKMeansRenderer.builder().build());

        renderers.forEach(renderer -> {
            renderer.accumulate(classify);
            renderer.renderResults();
        });
    }
}
