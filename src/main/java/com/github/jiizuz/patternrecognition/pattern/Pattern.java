package com.github.jiizuz.patternrecognition.pattern;

import com.google.common.base.MoreObjects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Arrays;

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
    protected double @NonNull [] vector;

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

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("class", className)
                .add("vector", Arrays.toString(vector))
                .toString();
    }
}
