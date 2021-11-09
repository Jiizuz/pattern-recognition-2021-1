package com.github.jiizuz.patternrecognition.pattern.util;

import java.util.function.Supplier;

/**
 * {@link Supplier} that acts as a supply factory of centroids.
 *
 * <p>The generated centroids must be new on each call.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @see java.util.function.Supplier
 * @since 1.7
 */
public interface CentroidFactory<C extends Centroid> extends Supplier<C> {
}
