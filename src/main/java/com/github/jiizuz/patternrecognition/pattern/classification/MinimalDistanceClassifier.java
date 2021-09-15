package com.github.jiizuz.patternrecognition.pattern.classification;

import com.github.jiizuz.patternrecognition.pattern.Pattern;
import com.github.jiizuz.patternrecognition.pattern.RepresentativePattern;
import com.github.jiizuz.patternrecognition.pattern.util.MathUtils;
import lombok.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private final Map<String, RepresentativePattern> representations = new HashMap<>();

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

        for (final Pattern pattern : patterns) {
            final RepresentativePattern representation = getRepresentation(pattern);

            representation.accumulate(pattern);
        }

        representations.values().forEach(RepresentativePattern::close);
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    public ClassifyResults classify(final @NonNull Pattern pattern) {
        final double[] vector = pattern.getVector();

        String foundClassName = null;
        double currentDistance = Integer.MAX_VALUE;

        for (final RepresentativePattern representation : representations.values()) {
            final double[] representationVector = representation.getVector();

            final double distance = MathUtils.computeEuclideanDistance(vector, representationVector);

            if (distance <= currentDistance) {
                foundClassName = representation.getClassName();
                currentDistance = distance;
            }
        }

        pattern.setClassName(foundClassName);

        return ClassifyResultsImpl.builder()
                .build();
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
        if (Objects.isNull(className)) {
            throw new NullPointerException("name of pattern");
        }

        RepresentativePattern representation = representations.get(className);

        if (Objects.isNull(representation)) {
            representation = new RepresentativePattern(className, pattern.getVector().length);
            representations.put(className, representation);
        }

        return representation;
    }

    /**
     * Checks if this algorithm is closed.
     *
     * @throws IllegalStateException if this algorithm is closed
     */
    private void checkCloseStatus() throws IllegalStateException {
        if (closed) {
            throw new IllegalStateException("The algorithm is already closed");
        }
    }
}