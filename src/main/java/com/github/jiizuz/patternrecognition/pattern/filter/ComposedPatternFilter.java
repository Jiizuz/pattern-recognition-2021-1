package com.github.jiizuz.patternrecognition.pattern.filter;

import com.github.jiizuz.patternrecognition.pattern.Pattern;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.NonNull;

import java.util.List;

/**
 * {@link PatternFilter} that uses a multi-layer filter.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @see com.github.jiizuz.patternrecognition.pattern.filter.PatternFilter
 * @since 1.0
 */
public class ComposedPatternFilter implements PatternFilter {

    /**
     * {@link List} of filters to apply to Patterns.
     */
    @NonNull
    private final List<PatternFilter> filters;

    /**
     * Constructs a new {@link ComposedPatternFilter} with the specified
     * {@link List} of layers filters to apply on the Patterns.
     *
     * @param filterList layers to apply on this filter
     * @throws NullPointerException if the list is <tt>null</tt>
     */
    @Builder
    public ComposedPatternFilter(final @NonNull List<PatternFilter> filterList) {
        filters = ImmutableList.copyOf(filterList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void filter(final @NonNull Pattern pattern) {
        filters.forEach(filter -> filter.filter(pattern));
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Pattern filterCopy(final @NonNull Pattern pattern) {
        Pattern result = pattern.clone();

        for (PatternFilter filter : filters) {
            result = filter.filterCopy(result);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void filter(final @NonNull List<Pattern> patterns) {
        filters.forEach(filter -> filter.filter(patterns));
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public List<Pattern> filterCopy(final @NonNull List<Pattern> patterns) {
        List<Pattern> result = Lists.newArrayList(patterns);

        for (PatternFilter filter : filters) {
            result = filter.filterCopy(result);
        }

        return result;
    }
}
