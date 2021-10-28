package com.github.jiizuz.patternrecognition.pattern;

import com.google.common.base.MoreObjects;
import lombok.Getter;
import lombok.NonNull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * Stores the average data of a pattern based on its <b>Class Name</b>.
 *
 * <p>Once a representation is closed, it cannot accept more patterns.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @see com.github.jiizuz.patternrecognition.pattern.Pattern
 * @see java.lang.AutoCloseable
 * @since 1.0
 */
@Getter
public class RepresentativePattern extends Pattern implements AutoCloseable, Cloneable {

    /**
     * Amount of patterns accumulated in this representation.
     */
    private int count = 0;

    /**
     * Status to allow or not new patterns.
     */
    private boolean closed = false;

    /**
     * Constructs a new {@link RepresentativePattern} with the specified
     * class name and an empty vector with the specified {@param vectorSize}.
     *
     * @param className  unique class name of this representation
     * @param vectorSize size of the vector of the patterns
     */
    public RepresentativePattern(final @NonNull String className, final int vectorSize) {
        super(className, new double[vectorSize]);
    }

    /**
     * Accumulates the specified {@param pattern} in this representation.
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
     * average data on the vector of this {@link Pattern}.
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
        checkArgument(getClassName().equals(pattern.getClassName()), "The specified pattern is not of the same class name");
        checkArgument(getVector().length == pattern.getVector().length, "The specified pattern has not the same size");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RepresentativePattern clone() {
        return (RepresentativePattern) super.clone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("className", className)
                .add("vector", vector)
                .add("count", count)
                .add("closed", closed)
                .toString();
    }
}
