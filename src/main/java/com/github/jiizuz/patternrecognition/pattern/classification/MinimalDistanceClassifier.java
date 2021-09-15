package com.github.jiizuz.patternrecognition.pattern.classification;

import com.github.jiizuz.patternrecognition.pattern.Pattern;
import com.github.jiizuz.patternrecognition.pattern.RepresentativePattern;
import com.github.jiizuz.patternrecognition.pattern.util.MathUtils;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import lombok.NonNull;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * {@link Classifier} that uses the average of the data and finds
 * the minimal distance to classify new incoming patterns.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @see com.github.jiizuz.patternrecognition.pattern.classification.Classifier
 * @since 1.0
 */
public class MinimalDistanceClassifier implements Classifier {

    /**
     * Storage of the available representations.
     */
    private final Map<String, RepresentativePattern> representations = Maps.newHashMap();

    /**
     * Status to allow or not new trains.
     */
    private boolean closed = false;

    /**
     * {@inheritDoc}
     */
    @Override
    public void train(final @NonNull List<Pattern> patterns) {
        checkCloseStatus();
        closed = true;

        patterns.forEach(pattern -> getRepresentation(pattern).accumulate(pattern));

        representations.values().forEach(RepresentativePattern::close);
    }

    /**
     * {@inheritDoc}
     */
    public void classify(final @NonNull Pattern pattern) {
        representations.values().stream()
                .min(Comparator.comparingDouble(value ->
                        MathUtils.computeEuclideanDistance(pattern.getVector(), value.getVector())))
                .map(Pattern::getClassName)
                .ifPresent(pattern::setClassName);
    }

    /**
     * Returns the representative pattern of the specified {@param pattern}.
     *
     * <p>If the representation does not exist, a new will be created.
     *
     * @param pattern to retrieve the representation of
     * @return the representation of the pattern
     * @throws NullPointerException if the specified {@param pattern} is <tt>null</tt>
     */
    @NonNull
    private RepresentativePattern getRepresentation(final @NonNull Pattern pattern) throws NullPointerException {
        final String className = pattern.getClassName();
        checkNotNull(className, "name of pattern");

        return representations.computeIfAbsent(className, name ->
                new RepresentativePattern(name, pattern.getVector().length));
    }

    /**
     * Checks if this algorithm is closed.
     *
     * @throws IllegalStateException if this algorithm is closed
     */
    private void checkCloseStatus() throws IllegalStateException {
        checkState(!closed, "The algorithm is already closed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("representations", representations)
                .add("closed", closed)
                .toString();
    }
}
