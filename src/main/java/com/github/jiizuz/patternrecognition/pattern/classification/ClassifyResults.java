package com.github.jiizuz.patternrecognition.pattern.classification;

import lombok.NonNull;

import java.util.Map;

/**
 * Results of a classification of a Pattern in a {@link Classifier}.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @since 1.0
 */
public interface ClassifyResults {

    /**
     * Returns the compatibility percentage of the Pattern with
     * all the available classes in the {@link Classifier}.
     *
     * <p>The key is the class-name compared.
     *
     * <p>The value is the approximation percentage.
     *
     * @return the compatibility percentage
     * @implSpec the Map may be ordered by the value
     */
    @NonNull
    Map<String, Double> getCompatibilities();
}
