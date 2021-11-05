package com.github.jiizuz.patternrecognition.pattern.util;

import com.github.jiizuz.patternrecognition.pattern.classification.KMeans;
import com.google.common.base.MoreObjects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Arrays;

/**
 * Represents a single centroid in a {@link KMeans} algorithm instance.
 *
 * <p>A centroid is the real or imaginary location that represents the
 * center of the cluster. All data point is assigned to each group by
 * reducing the sum of squares in the group.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @see java.lang.Cloneable
 * @since 1.5
 */
@Data
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
public class Centroid implements Cloneable {

    /**
     * Unique Id of this {@link Centroid}.
     */
    protected int id;

    /**
     * Vector with the location (coordinates) of the {@link Centroid}.
     */
    protected double @NonNull [] vector;

    /**
     * Constructs a {@link Centroid} based on the specified centroid.
     *
     * @param centroid to clone from the data
     * @apiNote This is known as a copy constructor.
     */
    public Centroid(final @NonNull Centroid centroid) {
        id = centroid.getId();
        vector = centroid.getVector().clone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Centroid clone() {
        try {
            return (Centroid) super.clone();
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
                .add("id", id)
                .add("coords", Arrays.toString(vector))
                .toString();
    }
}
