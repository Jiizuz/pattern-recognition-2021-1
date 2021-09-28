package com.github.jiizuz.patternrecognition.pattern.filter;

import com.github.jiizuz.patternrecognition.pattern.Pattern;
import lombok.NonNull;

import java.util.List;

/**
 * Filter that applies to a single {@link Pattern} or to a provided
 * {@link List} of {@link Pattern}s in order to select certain
 * characteristics of the Patterns and generate certain scenarios.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @implSpec this class is immutable and thread-safe
 * @since 1.0
 */
public interface PatternFilter {

    /**
     * Filters the specified {@link Pattern}'s vector of characteristics
     * and stores the results on the same object reference to be used.
     *
     * @param pattern to apply on this filter
     * @throws NullPointerException if the {@link Pattern} is <tt>null</tt>
     * @implSpec the {@link Pattern} will have <b>ALWAYS</b> another vector
     * object reference after this call.
     *
     * <p>The new vector of the {@link Pattern} will always have at least a
     * single element on it.
     */
    void filter(@NonNull Pattern pattern);

    /**
     * Filters the specified {@link Pattern}'s vector of characteristics
     * and returns an exact clone with the results of the filtering.
     *
     * @param pattern to clone and apply on this filter
     * @return the cloned {@link Pattern} filtered
     * @throws NullPointerException if the {@link Pattern} is <tt>null</tt>
     * @implSpec the {@link Pattern} will have <b>ALWAYS</b> another vector
     * object reference after this call.
     *
     * <p>The new vector of the {@link Pattern} will always have at least a
     * single element on it.
     */
    @NonNull
    Pattern filterCopy(@NonNull Pattern pattern);

    /**
     * Filters the {@link Pattern}'s vector of characteristics in the specified
     * {@link List} and stores the results on the same object reference to be used.
     *
     * @param patterns to apply on this filter
     * @throws NullPointerException if the list is <tt>null</tt>
     * @implSpec
     * @implSpec the specified list is a read-only reference but the patterns are modified.
     *
     * <p>The {@link Pattern}s will have <b>ALWAYS</b> another vector
     * object reference after this call.
     *
     * <p>The new vector of the {@link Pattern} will always have at least a
     * single element on it.
     */
    void filter(@NonNull List<Pattern> patterns);

    /**
     * Filters a deep-clone of the the {@link Pattern}'s vector of characteristics
     * in the specified {@link List} and stores the results on the returned clone.
     *
     * @param patterns to clone and apply on this filter
     * @return the cloned {@link Pattern}s filtered (mutable)
     * @throws NullPointerException if the list is <tt>null</tt>
     * @implSpec the specified list is a read-only reference.
     *
     * <p>The returned {@link List} has the exact same size as the provided one.
     *
     * <p>The {@link Pattern}s will have <b>ALWAYS</b> another vector
     * object reference after this call.
     *
     * <p>The new vector of the {@link Pattern} will always have at least a
     * single element on it.
     */
    @NonNull
    List<Pattern> filterCopy(@NonNull List<Pattern> patterns);
}
