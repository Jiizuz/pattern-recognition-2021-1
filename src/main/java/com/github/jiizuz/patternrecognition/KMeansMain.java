package com.github.jiizuz.patternrecognition;

import com.github.jiizuz.patternrecognition.pattern.Pattern;
import com.github.jiizuz.patternrecognition.pattern.classification.KMeans;
import com.github.jiizuz.patternrecognition.pattern.parser.CsvParser;
import com.google.common.base.Joiner;
import lombok.NonNull;
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
     * Runs the main instance of the project.
     *
     * @param args passed in the command line
     */
    public void main(String[] args) throws IOException {
        final List<Pattern> sourcePatterns = CsvParser.parsePatternsFromCsv(FILE_NAME);

        final KMeans kMeans = new KMeans(16);

        // train phase

        kMeans.train(sourcePatterns);

        // classify phase

        final Joiner patternJoiner = Joiner.on('\n');
        kMeans.classify(sourcePatterns).asMap().forEach((centroid, patterns) -> {
            System.out.println("------------------------------ CLUSTER -----------------------------------");
            System.out.printf("Centroid %d {%s}, %d patterns%n", centroid.getId() + 1, toString(centroid.getVector()), patterns.size());
            System.out.println(patternJoiner.join(patterns));
            System.out.println();
        });
    }

    /**
     * Returns a string representation of the contents of the specified array.
     * The string representation consists of a list of the array's elements.
     * Adjacent elements are separated by the characters <tt>", "</tt> (a comma
     * followed by a space). Elements are converted to String as by
     * <tt>String.valueOf(double)</tt>.  Returns <tt>"null"</tt> if <tt>a</tt>
     * is <tt>null</tt>.
     *
     * @param a the array whose string representation to return
     * @return a string representation of <tt>a</tt>
     */
    @NonNull
    private String toString(final double[] a) {
        if (a == null) {
            return "null";
        }

        int iMax = a.length - 1;
        if (iMax == -1) {
            return "";
        }

        final StringBuilder output = new StringBuilder();
        for (int i = 0; ; i++) {
            output.append(a[i]);
            if (i == iMax) {
                return output.toString();
            }
            output.append(", ");
        }
    }
}
