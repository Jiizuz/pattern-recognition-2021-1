package com.github.jiizuz.patternrecognition.pattern.filter;

import lombok.NonNull;

import java.util.Random;

/**
 * Builds a single {@link PatternFilter} by adding layers of
 * filter to apply over a List of Patterns.
 *
 * <p>The layers are stacked and are applied one behind another
 * using the resultant Pattern of the previous Layer, being a
 * multi-layer filter.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @implSpec this class is not thread-safe
 * @since 1.0
 */
public interface PatternFilterBuilder {

    /**
     * Adds a layer to the {@link PatternFilter} that will get only the
     * first found <tt>n</tt> characteristics on the vector of the pattern.
     *
     * <p>If the length of the vector is lower than <tt>n</tt>, then the
     * vector remains in the same state failing silently this layer.
     *
     * @param n amount of first characteristics to get
     * @return this <tt>Builder</tt> reference for chain calls
     * @throws IllegalArgumentException if n is negative or zero
     */
    @NonNull
    PatternFilterBuilder firstN(int n);

    /**
     * Adds a layer to the {@link PatternFilter} that will get only the
     * first found <tt>x%</tt> amount of characteristics of the vector.
     *
     * <p>The percentage must be in the next range:
     * <pre>
     * Pseudocode Notation
     *     x > 0 && x < 1
     * Inequality Notation
     *     0 < x < 1
     * Interval Notation
     *     (0, 1)
     * </pre>
     *
     * @param x percentage of the first characteristics to consider
     * @return this <tt>Builder</tt> reference for chain calls
     * @throws IllegalArgumentException if x is <= 0 or >= 1
     */
    @NonNull
    PatternFilterBuilder firstX(float x);

    /**
     * Adds a layer to the {@link PatternFilter} that will get only the
     * last found <tt>n</tt> characteristics on the vector of the pattern.
     *
     * <p>If the length of the vector is lower than <tt>n</tt>, then the
     * vector remains in the same state failing silently this layer.
     *
     * @param n amount of last characteristics to get
     * @return this <tt>Builder</tt> reference for chain calls
     * @throws IllegalArgumentException if n is negative or zero
     */
    @NonNull
    PatternFilterBuilder lastN(int n);

    /**
     * Adds a layer to the {@link PatternFilter} that will get only the
     * last found <tt>x%</tt> amount of characteristics of the vector.
     *
     * <p>The percentage must be in the next range:
     * <pre>
     * Pseudocode Notation
     *     x > 0 && x < 1
     * Inequality Notation
     *     0 < x < 1
     * Interval Notation
     *     (0, 1)
     * </pre>
     *
     * @param x percentage of the last characteristics to consider
     * @return this <tt>Builder</tt> reference for chain calls
     * @throws IllegalArgumentException if x is <= 0 or >= 1
     */
    @NonNull
    PatternFilterBuilder lastX(float x);

    /**
     * Adds a layer to the {@link PatternFilter} that will get only the
     * specified amount of <tt>n</tt> characteristics on the vector of
     * the Pattern by selecting random characteristics.
     *
     * <p>IMPORTANT: The characteristics will not be duplicated.
     *
     * <p>If the length of the vector is lower than <tt>n</tt>, then the
     * vector remains in the same state failing silently this layer.
     *
     * <p>Note: when a List of Patterns is filtered, the characteristics
     * will remain as the same in each Pattern in the List.
     *
     * @param n      amount of random characteristics to get
     * @param random to use on the generation of pseudo-random numbers
     * @return this <tt>Builder</tt> reference for chain calls
     * @throws IllegalArgumentException if n is negative or zero
     */
    @NonNull
    PatternFilterBuilder randomN(int n, @NonNull Random random);

    /**
     * Adds a layer to the {@link PatternFilter} that will get only the
     * specified found <tt>x%</tt> amount of characteristics on the vector
     * of the Pattern by selecting random characteristics.
     *
     * <p>IMPORTANT: The characteristics will not be duplicated.
     *
     * <p>The percentage must be in the next range:
     * <pre>
     * Pseudocode Notation
     *     x > 0 && x < 1
     * Inequality Notation
     *     0 < x < 1
     * Interval Notation
     *     (0, 1)
     * </pre>
     *
     * <p>Note: when a List of Patterns is filtered, the characteristics
     * will remain as the same in each Pattern in the List.
     *
     * @param x      percentage of random characteristics to consider
     * @param random to use on the generation of pseudo-random numbers
     * @return this <tt>Builder</tt> reference for chain calls
     * @throws IllegalArgumentException if x is <= 0 or >= 1
     */
    @NonNull
    PatternFilterBuilder randomX(float x, @NonNull Random random);

    /**
     * Builds and returns the {@link PatternFilter}.
     *
     * @return the {@link PatternFilter} built
     */
    @NonNull
    PatternFilter build();
}
