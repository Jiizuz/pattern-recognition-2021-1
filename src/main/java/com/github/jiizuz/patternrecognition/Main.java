package com.github.jiizuz.patternrecognition;

import com.github.jiizuz.patternrecognition.pattern.Pattern;
import com.github.jiizuz.patternrecognition.pattern.classification.Classifier;
import com.github.jiizuz.patternrecognition.pattern.classification.ClassifyResults;
import com.github.jiizuz.patternrecognition.pattern.classification.MinimalDistanceClassifier;
import com.github.jiizuz.patternrecognition.pattern.parser.CsvParser;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.util.List;

/**
 * Main class of the project to initialize and run.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @since 1.0
 */
@UtilityClass
public class Main {

    /**
     * Name of the file to parse from example data.
     */
    private final String FILE_NAME = "iris.csv";

    /**
     * Runs the main instance of the project.
     *
     * @param args passed in the command line
     */
    public void main(final String[] args) throws IOException {
        final List<Pattern> patterns = CsvParser.parsePatternsFromCsv(FILE_NAME);

        final Classifier minimalDistance = new MinimalDistanceClassifier();
        minimalDistance.train(patterns);

        // should be Iris-setosa
        final Pattern test = Pattern.builder()
                .vector(new double[] { 4.3D, 3.0D, 1.1D, 0.1D })
                .build();

        final ClassifyResults classifyResults = minimalDistance.classify(test);

        System.out.println(test);
        System.out.println();
        classifyResults.getCompatibilities().forEach((className, percentage) ->
                System.out.printf("%.2f%% compatible with %s%n", percentage, className));
    }
}
