package com.github.jiizuz.patternrecognition.pattern.filter;

import com.github.jiizuz.patternrecognition.pattern.Pattern;
import com.google.common.math.DoubleMath;
import lombok.NonNull;

import java.math.RoundingMode;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * {@link PatternFilter} that takes only the last <tt>X%</tt>
 * characteristics of the Patterns.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @see com.github.jiizuz.patternrecognition.pattern.filter.PatternFilter
 * @since 1.0
 */
public class LastXFilter extends CommonPatternFilter implements PatternFilter {

    /**
     * X% amount of characteristics to consider.
     */
    private final float x;

    /**
     * Constructs a new {@link LastXFilter} with the specified <tt>x</tt>.
     *
     * @param x percentage of characteristics to consider
     * @throws IllegalArgumentException if x <= 0 || x >= 1
     */
    public LastXFilter(final float x) {
        checkArgument(x > 0.0F && x < 1.0F, "x <= 0 || x >= 1", x);
        this.x = x;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void filter(final @NonNull Pattern pattern) {
        final double[] vector = pattern.getVector();

        final int amount = Math.max(DoubleMath.roundToInt(vector.length * x, RoundingMode.FLOOR), 1);

        pattern.setVector(Arrays.copyOfRange(vector, vector.length - amount, vector.length));
    }
}
