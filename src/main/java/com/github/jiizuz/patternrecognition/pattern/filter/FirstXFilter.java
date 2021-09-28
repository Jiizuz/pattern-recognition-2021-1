package com.github.jiizuz.patternrecognition.pattern.filter;

import com.github.jiizuz.patternrecognition.pattern.Pattern;
import com.google.common.math.DoubleMath;
import it.unimi.dsi.fastutil.doubles.DoubleArrays;
import lombok.NonNull;

import java.math.RoundingMode;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * {@link PatternFilter} that takes only the first <tt>X%</tt>
 * characteristics of the Patterns.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @see com.github.jiizuz.patternrecognition.pattern.filter.PatternFilter
 * @since 1.0
 */
public class FirstXFilter extends CommonPatternFilter implements PatternFilter {

    /**
     * X% amount of characteristics to consider.
     */
    private final float x;

    /**
     * Constructs a new {@link FirstXFilter} with the specified <tt>x</tt>.
     *
     * @param x percentage of characteristics to consider
     * @throws IllegalArgumentException if x <= 0 || x >= 1
     */
    public FirstXFilter(final float x) {
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

        pattern.setVector(DoubleArrays.trim(vector, amount));
    }
}
