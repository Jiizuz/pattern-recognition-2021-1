package pattern;

import lombok.*;

/**
 * Java Bean to store the data of a single pattern.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @since 1.0
 */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Pattern {

    /**
     * Name of the class of this pattern.
     */
    private String className;

    /**
     * Vector with the numbers of the pattern.
     */
    @NonNull
    private double[] vector;

    /**
     * Constructs a {@link Pattern} based on the specified {@param pattern}.
     *
     * @param pattern to clone from the data
     */
    public Pattern(final @NonNull Pattern pattern) {
        this.className = pattern.getClassName();
        this.vector = pattern.getVector().clone();
    }
}
