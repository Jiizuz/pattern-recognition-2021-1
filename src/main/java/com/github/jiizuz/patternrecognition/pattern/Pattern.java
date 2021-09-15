package com.github.jiizuz.patternrecognition.pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Java Bean to store the data of a single pattern.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @since 1.0
 */
@Data
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
public class Pattern implements Cloneable {

    /**
     * Name of the class of this pattern.
     */
    protected String className;

    /**
     * Vector with the numbers of the pattern.
     */
    @NonNull
    protected double[] vector;

    /**
     * Constructs a {@link Pattern} based on the specified {@param pattern}.
     *
     * @param pattern to clone from the data
     */
    public Pattern(final @NonNull Pattern pattern) {
        this.className = pattern.getClassName();
        this.vector = pattern.getVector().clone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pattern clone() {
        try {
            return (Pattern) super.clone();
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(e);
        }
    }
}
