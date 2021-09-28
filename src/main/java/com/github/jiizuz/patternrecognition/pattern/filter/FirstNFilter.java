package com.github.jiizuz.patternrecognition.pattern.filter;

import com.github.jiizuz.patternrecognition.pattern.Pattern;
import it.unimi.dsi.fastutil.doubles.DoubleArrays;
import lombok.NonNull;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * {@link PatternFilter} that takes only the first <tt>n</tt>
 * characteristics of the Patterns.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @see com.github.jiizuz.patternrecognition.pattern.filter.PatternFilter
 * @since 1.0
 */
public class FirstNFilter extends CommonPatternFilter implements PatternFilter {

    /**
     * N amount of characteristics to consider.
     */
    private final int n;

    /**
     * Constructs a new {@link FirstNFilter} with the specified <tt>n</tt>.
     *
     * @param n amount of characteristics to consider
     * @throws IllegalArgumentException if n is <= 0
     */
    public FirstNFilter(final int n) {
        checkArgument(n > 0, "n is <= 0", n);
        this.n = n;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void filter(final @NonNull Pattern pattern) {
        final double[] vector = pattern.getVector();

        if (n >= vector.length) {
            // ensure clone
            pattern.setVector(vector.clone());
            return;
        }

        final double[] trimmed = DoubleArrays.trim(vector, n);

        pattern.setVector(trimmed);
    }
}
