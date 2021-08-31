package pattern.parser;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import pattern.Pattern;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class to parse a file into multiple objects separated by
 * commas.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @since 1.0
 */
@UtilityClass
public class CsvParser {

    /**
     * Parses the patterns found in the file at the specified {@param fileName}.
     *
     * <p>The format to parse patterns is that all elements are split by
     * commands and the last element in the row is the name of the class.
     *
     * <p>Example file:<pre>
     *     5.1,3.5,1.4,0.2,Iris-setosa
     *     4.9,3.0,1.4,0.2,Iris-setosa
     *     7.0,3.2,4.7,1.4,Iris-versicolor
     *     6.4,3.2,4.5,1.5,Iris-versicolor
     *     6.3,3.3,6.0,2.5,Iris-virginica
     *     5.8,2.7,5.1,1.9,Iris-virginica
     * </pre>
     *
     * <p>This method resolves the {@link Path} of the specified file name and
     * wraps the call on the method: {@link #parsePatternsFromCsv(Path)}.
     *
     * @param fileName of the file to parse from the patterns
     * @return a {@link List} with the found patterns in the file
     * @throws IOException if there is an exception reading the file
     * @see #parsePatternsFromCsv(Path)
     */
    @NonNull
    public List<Pattern> parsePatternsFromCsv(final @NonNull String fileName) throws IOException {
        final Path path = Paths.get(fileName);

        return parsePatternsFromCsv(path);
    }

    /**
     * Parses the patterns found in the file at the specified {@param path}.
     *
     * <p>The format to parse patterns is that all elements are split by
     * commands and the last element in the row is the name of the class.
     *
     * <p>Example file:<pre>
     *     5.1,3.5,1.4,0.2,Iris-setosa
     *     4.9,3.0,1.4,0.2,Iris-setosa
     *     7.0,3.2,4.7,1.4,Iris-versicolor
     *     6.4,3.2,4.5,1.5,Iris-versicolor
     *     6.3,3.3,6.0,2.5,Iris-virginica
     *     5.8,2.7,5.1,1.9,Iris-virginica
     * </pre>
     *
     * @param path to parse from the patterns
     * @return a {@link List} with the found patterns in the file
     * @throws IOException if there is an exception reading the file
     */
    @NonNull
    public List<Pattern> parsePatternsFromCsv(final @NonNull Path path) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            return reader.lines()
                    .map(line -> line.split(","))
                    .map(CsvParser::parsePatternElements)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Parses the vector from the specified {@param elements} considering
     * the class name as the final element in the specified array.
     *
     * @param elements to parse from the elements of a pattern
     * @return the parsed {@link Pattern}
     * @throws NumberFormatException    if one of the elements of the array is
     *                                  not a valid number
     * @throws IllegalArgumentException if the specified array is empty
     */
    @NonNull
    private Pattern parsePatternElements(final @NonNull String[] elements) {
        if (elements.length == 0) {
            throw new IllegalArgumentException("elements are empty");
        }

        final double[] vector = Arrays.stream(elements, 0, elements.length - 1)
                .map(Double::parseDouble)
                .mapToDouble(value -> value)
                .toArray();

        return Pattern.builder()
                .className(elements[elements.length - 1])
                .vector(vector)
                .build();
    }
}
