package com.github.jiizuz.patternrecognition;

import com.github.jiizuz.patternrecognition.pattern.Pattern;
import com.github.jiizuz.patternrecognition.pattern.TestPattern;
import com.github.jiizuz.patternrecognition.pattern.classification.Classifier;
import com.github.jiizuz.patternrecognition.pattern.classification.KNearestNeighborsClassifier;
import com.github.jiizuz.patternrecognition.pattern.classification.MinimalDistanceClassifier;
import com.github.jiizuz.patternrecognition.pattern.classification.NaiveBayesClassifier;
import com.github.jiizuz.patternrecognition.pattern.filter.ListPatternFilterBuilder;
import com.github.jiizuz.patternrecognition.pattern.filter.PatternFilter;
import com.github.jiizuz.patternrecognition.pattern.parser.CsvParser;
import com.github.jiizuz.patternrecognition.pattern.util.ConfusionMatrix;
import com.google.common.collect.ImmutableList;
import lombok.NonNull;
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
     * Value of <tt>K</tt> to use on the {@link KNearestNeighborsClassifier}.
     */
    private final int K_VALUE = 4;

    /**
     * Runs the main instance of the project.
     *
     * @param args passed in the command line
     */
    public void main(final String[] args) throws IOException {
        final List<Pattern> patterns = CsvParser.parsePatternsFromCsv(FILE_NAME);

        final PatternFilter filter = ListPatternFilterBuilder.create()
                .firstX(0.50F)
                .build();

        final List<Pattern> filteredPatterns = filter.filterCopy(patterns);

        for (int i = 0; i < patterns.size(); ++i) {
            System.out.println(patterns.get(i) + " -> " + filteredPatterns.get(i));
        }

        final ImmutableList<Classifier> classifiers = ImmutableList.of(
                new MinimalDistanceClassifier(),
                new KNearestNeighborsClassifier(K_VALUE),
                new NaiveBayesClassifier());

        classifiers.forEach(classifier -> testClassifier(classifier, patterns));
    }

    /**
     * Tests the specified {@link Classifier} with the specified {@link Pattern}s
     * as the <tt>train</tt> patterns for the {@link Classifier}.
     *
     * @param classifier that is going to be tested
     * @param patterns   to train the {@link Classifier}
     * @apiNote the results are sent to the {@link System#out} stream
     */
    private void testClassifier(final @NonNull Classifier classifier, final @NonNull List<Pattern> patterns) {
        System.out.printf("About to test classifier: %s%n", classifier);

        // train phase

        classifier.train(patterns);

        // single test

        // should be Iris-setosa
        final TestPattern test = TestPattern.builder()
                .vector(new double[] { 4.3D, 3.0D, 1.1D, 0.1D })
                .expectedClass("Iris-setosa")
                .build();

        classifier.classify(test);

        System.out.printf("Successful classify test? %s, %s%n", test.isSuccessClassification(), test);

        // bulk test

        final List<TestPattern> testPatterns = patterns.stream()
                .map(TestPattern::new)
                .collect(ImmutableList.toImmutableList());

        final IntSummaryStatistics classifyStatistics = testPatterns.stream()
                .peek(classifier::classify)
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
