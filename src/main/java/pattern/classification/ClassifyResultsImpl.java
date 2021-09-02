package pattern.classification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;

import java.util.Map;
import java.util.StringJoiner;

/**
 * {@link ClassifyResults} that wraps the results in a basic Java Bean.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @see pattern.classification.ClassifyResults
 * @since 1.0
 */
@Builder
@AllArgsConstructor
public final class ClassifyResultsImpl implements ClassifyResults {

    /**
     * Compatibilities of the Pattern.
     */
    @NonNull
    private final Map<String, Double> compatibilities;

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Map<String, Double> getCompatibilities() {
        return compatibilities;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", ClassifyResultsImpl.class.getSimpleName() + "[", "]")
                .add("compatibilities=" + compatibilities)
                .toString();
    }
}
