package com.github.jiizuz.patternrecognition.pattern.util;

import com.github.jiizuz.patternrecognition.pattern.Pattern;
import com.github.jiizuz.patternrecognition.pattern.RepresentativePattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * Represents a single element in a {@code Naive Bayes} classify.
 *
 * <p>Once a {@link Naive} is {@code closed} it will not allow
 * more accumulations on its {@link RepresentativePattern} average
 * and will start to allow variances.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @see com.github.jiizuz.patternrecognition.pattern.classification.NaiveBayesClassifier
 * @see java.lang.AutoCloseable
 * @since 1.4
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE) // copy constructor
public final class Naive implements AutoCloseable, Cloneable {

    /**
     * Name of this {@link Naive} instance.
     */
    @NonNull
    private final String name;

    /**
     * {@link RepresentativePattern} to accumulate an average pattern to represent this {@link Naive}.
     *
     * <p>a.k.a. average
     */
    @NonNull
    private final RepresentativePattern representative;

    /**
     * Vector to track the variance of this {@link Naive}.
     */
    private final double @NonNull [] variance;

    /**
     * Probability that shows the likelihood of an outcome in a given dataset.
     */
    private double prior = 0;

    /**
     * Status to allow or not incoming variances.
     */
    private boolean closed = false;

    /**
     * Constructs a new {@link Naive} to wrap over patterns with
     * the same class name as the specified {@link Pattern}.
     *
     * @param pattern to wrap around this {@link Naive}
     * @throws NullPointerException if the pattern is {@code null}
     */
    public Naive(final @NonNull Pattern pattern) {
        final int length = pattern.getVector().length;

        name = pattern.getClassName();
        representative = new RepresentativePattern(name, length);
        variance = new double[length];
    }

    // --- AutoCloseable ---

    /**
     * Closes this {@link Naive} and will also close the representative pattern.
     *
     * @throws IllegalStateException if this {@link Naive} is already {@code closed}
     */
    @Override
    public void close() {
        checkIsNotClosed();
        closed = true;

        representative.close();
    }

    // --- Closeable ---

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public Naive clone() { // we use a copy constructor since the RepresentativePattern is final
        return new Naive(name, representative.clone(), variance.clone(), prior, closed);
    }

    /**
     * Prior probability shows the likelihood of an outcome in a given dataset.
     *
     * @param total total amount of patterns in the dataset
     * @throws IllegalArgumentException if the specified total is negative or zero
     * @throws IllegalStateException    if this {@link Naive} is not {@code closed} yet
     */
    public void calculatePrior(final int total) {
        checkIsClosed();
        checkArgument(total > 0, "total cannot be negative nor zero", total);

        prior = (double) representative.getCount() / total;
    }

    /**
     * Appends the specified pattern to the variance vector.
     *
     * @param pattern to append to the variance of this {@link Naive}
     * @throws NullPointerException     if the pattern is {@code null}
     * @throws IllegalStateException    if this {@link Naive} is not {@code closed} yet
     * @throws IllegalArgumentException if the pattern has an invalid data to this {@link Naive}
     */
    public void appendVariance(final @NonNull Pattern pattern) {
        checkIsClosed();
        validatePattern(pattern);

        final double[] vector = pattern.getVector();

        for (int i = 0; i < variance.length; ++i) {
            final double delta = vector[i] - representative.getVector()[i];

            variance[i] += MathUtils.square(delta) / (representative.getCount() - 1);
        }
    }

    // check methods

    /**
     * Checks if this {@link Naive} is closed.
     *
     * @throws IllegalStateException if this {@link Naive} is not {@code closed}
     */
    private void checkIsClosed() throws IllegalStateException {
        checkState(closed, "The naive is not closed yet");
    }

    /**
     * Checks if this {@link Naive} is closed.
     *
     * @throws IllegalStateException if this {@link Naive} is {@code closed}
     */
    private void checkIsNotClosed() throws IllegalStateException {
        checkState(!closed, "The naive is already closed");
    }

    /**
     * Validates that the specified pattern has valid data to store.
     *
     * @param pattern to check
     * @throws IllegalArgumentException if the pattern has no valid data
     * @throws NullPointerException     if the specified pattern is {@code null}
     */
    private void validatePattern(final @NonNull Pattern pattern) throws IllegalArgumentException {
        checkArgument(name.equals(pattern.getClassName()), "The specified pattern is not of the same class name");
        checkArgument(variance.length == pattern.getVector().length, "The specified pattern has not the same size");
    }
}
