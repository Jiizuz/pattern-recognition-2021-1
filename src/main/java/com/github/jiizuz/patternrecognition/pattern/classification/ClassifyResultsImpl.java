package com.github.jiizuz.patternrecognition.pattern.classification;

import com.google.common.base.MoreObjects;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * {@link ClassifyResults} that wraps the results in a basic Java Bean.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @see com.github.jiizuz.patternrecognition.pattern.classification.ClassifyResults
 * @since 1.0
 */
@Builder
@AllArgsConstructor
public final class ClassifyResultsImpl implements ClassifyResults {

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .toString();
    }
}
