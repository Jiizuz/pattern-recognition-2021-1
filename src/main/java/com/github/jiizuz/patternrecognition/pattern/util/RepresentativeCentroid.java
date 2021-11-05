package com.github.jiizuz.patternrecognition.pattern.util;

import com.github.jiizuz.patternrecognition.pattern.Pattern;
import com.google.common.base.MoreObjects;
import lombok.Getter;
import lombok.NonNull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * {@link Centroid} that works as an accumulator in order to
 * accumulate multiple {@link Pattern} and generate a 'mean'.
 *
 * <p>A {@link RepresentativeCentroid} must be closed in order
 * to determine the actual mean in the {@link #getVector()}.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @see com.github.jiizuz.patternrecognition.pattern.util.Centroid
 * @see java.lang.AutoCloseable
 * @since 1.5
 */
@Getter
public class RepresentativeCentroid extends Centroid implements AutoCloseable, Cloneable {

    /**
     * Amount of patterns accumulated in this centroid.
     */
    private int count = 0;

    /**
     * Status to allow or not new patterns.
     */
    private boolean closed = false;

    /**
     * Constructs a new {@link RepresentativeCentroid} with the
     * specified Id and an empty vector with the specified size.
     *
     * @param id     unique-Id of this representation
     * @param length size of the vector
     */
    public RepresentativeCentroid(final int id, final int length) {
        super(id, new double[length]);
    }

    /**
     * Accumulates the specified {@link Pattern} in this representation.
     *
     * @param pattern to accumulate in this representation
     * @throws NullPointerException if the specified pattern is <tt>null</tt>
     */
    public void accumulate(final @NonNull Pattern pattern) {
        checkCloseStatus();
        validatePattern(pattern);

        final double[] vector = getVector();
        final double[] patternVector = pattern.getVector();

        for (int i = 0; i < vector.length; i++) {
            vector[i] += patternVector[i];
        }
        count++;
    }

    /**
     * Closes this representation and will update the
     * average data on the vector of this {@link Centroid}.
     *
     * @throws IllegalStateException if the representation is already closed
     */
    @Override
    public void close() throws IllegalStateException {
        checkCloseStatus();
        closed = true;

        final double[] vector = getVector();

        for (int i = 0; i < vector.length; i++) {
            vector[i] /= count;
        }
    }

    /**
     * Checks if this representation is closed.
     *
     * @throws IllegalStateException if this representation is closed
     */
    private void checkCloseStatus() throws IllegalStateException {
        checkState(!closed, "The representation is already closed");
    }

    /**
     * Validates that the specified pattern has valid data to store.
     *
     * @param pattern to check
     * @throws IllegalArgumentException if the pattern has no valid data
     * @throws NullPointerException     if the specified pattern is <tt>null</tt>
     */
    private void validatePattern(final @NonNull Pattern pattern) throws IllegalArgumentException {
        checkArgument(getVector().length == pattern.getVector().length, "The specified pattern has not the same size");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RepresentativeCentroid clone() {
        return (RepresentativeCentroid) super.clone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("vector", vector)
                .add("count", count)
                .add("closed", closed)
                .toString();
    }
}
