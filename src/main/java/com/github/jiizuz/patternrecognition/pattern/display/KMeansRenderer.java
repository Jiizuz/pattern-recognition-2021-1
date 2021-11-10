package com.github.jiizuz.patternrecognition.pattern.display;

import com.github.jiizuz.patternrecognition.pattern.Pattern;
import com.github.jiizuz.patternrecognition.pattern.classification.KMeans;
import com.github.jiizuz.patternrecognition.pattern.util.Centroid;
import com.google.common.collect.ListMultimap;
import lombok.NonNull;

/**
 * Manages the rendering of the results of a single {@link KMeans}
 * result by allowing accumulation of data in order to render after
 * multiple centroids.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @implSpec This class is not Thread-Safe.
 * @since 1.6
 */
public interface KMeansRenderer {

    /**
     * Accumulates to the current renderer the specified {@link ListMultimap} of
     * patterns per centroid.
     *
     * @param centroidMap to accumulate to the results
     * @throws NullPointerException if the map is <tt>null</tt>
     * @implSpec The specified {@link ListMultimap} is not modified in any way.
     */
    void accumulate(@NonNull ListMultimap<? extends Centroid, ? extends Pattern> centroidMap);

    /**
     * Closes this renderer and generates the final results of the accumulation
     * by generating a visual display of the data for the user.
     */
    void renderResults();
}
