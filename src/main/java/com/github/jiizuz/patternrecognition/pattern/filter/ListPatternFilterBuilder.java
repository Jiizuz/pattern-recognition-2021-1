package com.github.jiizuz.patternrecognition.pattern.filter;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * {@link PatternFilterBuilder} that uses an internal {@link List}
 * to keep track of the added layers of filtering.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @see com.github.jiizuz.patternrecognition.pattern.filter.PatternFilterBuilder
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ListPatternFilterBuilder implements PatternFilterBuilder {

    /**
     * {@link List} with the added filter layers.
     */
    private final List<PatternFilter> filters = Lists.newArrayListWithCapacity(4);

    /**
     * Creates a new {@link PatternFilterBuilder} instance with this implementation.
     *
     * @return the created builder
     */
    @NonNull
    public static PatternFilterBuilder create() {
        return new ListPatternFilterBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public PatternFilterBuilder firstN(final int n) {
        checkArgument(n > 0, "n is <= 0", n);
        filters.add(new FirstNFilter(n));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public PatternFilterBuilder firstX(final float x) {
        checkArgument(x > 0.0F && x < 1.0F, "x <= 0 || x >= 1", x);
        filters.add(new FirstXFilter(x));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public PatternFilterBuilder lastN(final int n) {
        checkArgument(n > 0, "n is <= 0", n);
        filters.add(new LastNFilter(n));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public PatternFilterBuilder lastX(final float x) {
        checkArgument(x > 0.0F && x < 1.0F, "x <= 0 || x >= 1", x);
        filters.add(new LastXFilter(x));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public PatternFilterBuilder randomN(final int n, final @NonNull Random random) {
        checkArgument(n > 0, "n is <= 0", n);
        filters.add(new RandomNFilter(n, random));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public PatternFilterBuilder randomX(final float x, final @NonNull Random random) {
        checkArgument(x > 0.0F && x < 1.0F, "x <= 0 || x >= 1", x);
        filters.add(new RandomXFilter(x, random));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public PatternFilter build() {
        return new ComposedPatternFilter(filters);
    }
}
