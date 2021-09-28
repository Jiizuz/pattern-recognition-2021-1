package com.github.jiizuz.patternrecognition.pattern.filter;

import com.github.jiizuz.patternrecognition.pattern.Pattern;
import com.google.common.collect.Lists;
import lombok.NonNull;

import java.util.List;

/**
 * {@link PatternFilter} implementation that overrides default
 * behavior of methods for common patterns data flow.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @see com.github.jiizuz.patternrecognition.pattern.filter.PatternFilter
 * @since 1.0
 */
public abstract class CommonPatternFilter implements PatternFilter {

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Pattern filterCopy(final @NonNull Pattern pattern) {
        final Pattern clone = pattern.clone();

        filter(clone);

        return clone;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void filter(final @NonNull List<Pattern> patterns) {
        patterns.forEach(this::filter);
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public List<Pattern> filterCopy(final @NonNull List<Pattern> patterns) {
        final List<Pattern> clone = Lists.newArrayList(patterns);

        clone.replaceAll(this::filterCopy);

        return clone;
    }
}
