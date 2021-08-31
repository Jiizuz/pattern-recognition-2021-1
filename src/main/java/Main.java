import lombok.experimental.UtilityClass;
import pattern.Pattern;
import pattern.parser.CsvParser;

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
        final List<Pattern> patterns = CsvParser.parsePatternFromCsv(FILE_NAME);

        System.out.println(patterns);
    }
}
