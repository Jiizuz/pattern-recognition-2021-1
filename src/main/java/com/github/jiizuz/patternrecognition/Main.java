package com.github.jiizuz.patternrecognition;

import com.github.jiizuz.patternrecognition.pattern.Pattern;
import com.github.jiizuz.patternrecognition.pattern.TestPattern;
import com.github.jiizuz.patternrecognition.pattern.classification.Classifier;
import com.github.jiizuz.patternrecognition.pattern.classification.MinimalDistanceClassifier;
import com.github.jiizuz.patternrecognition.pattern.parser.CsvParser;
import com.github.jiizuz.patternrecognition.pattern.util.ConfusionMatrix;
import com.google.common.collect.ImmutableList;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.util.IntSummaryStatistics;
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

        // train phase

        final Classifier minimalDistance = new MinimalDistanceClassifier();
        minimalDistance.train(patterns);

        // single test

        // should be Iris-setosa
        final TestPattern test = TestPattern.builder()
                .vector(new double[] { 4.3D, 3.0D, 1.1D, 0.1D })
                .expectedClass("Iris-setosa")
                .build();

        minimalDistance.classify(test);

        System.out.printf("Successful classify test? %s, %s%n", test.isSuccessClassification(), test);

        // bulk test

        final List<TestPattern> testPatterns = patterns.stream()
                .map(TestPattern::new)
                .collect(ImmutableList.toImmutableList());

        final IntSummaryStatistics classifyStatistics = testPatterns.stream()
                .peek(minimalDistance::classify)
                .mapToInt(testPattern -> testPattern.isSuccessClassification() ? 1 : 0)
                .summaryStatistics();

        final ConfusionMatrix confusionMatrix = new ConfusionMatrix(testPatterns);
        confusionMatrix.computeMatrix();

        System.out.println();
        System.out.printf("Train patterns: %,d%n", patterns.size());
        System.out.printf("Classified patterns: %,d%n", classifyStatistics.getCount());
        System.out.printf("Patterns classified successfully: %,d (%.3f%%)%n",
                classifyStatistics.getSum(), classifyStatistics.getAverage() * 100D);
        System.out.printf("%nConfusion matrix:%n%s%n", confusionMatrix.getDisplayMatrix());
    }
}
