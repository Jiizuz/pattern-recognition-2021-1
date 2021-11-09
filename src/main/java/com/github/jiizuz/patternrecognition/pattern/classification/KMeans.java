package com.github.jiizuz.patternrecognition.pattern.classification;

import com.github.jiizuz.patternrecognition.pattern.Pattern;
import com.github.jiizuz.patternrecognition.pattern.util.Centroid;
import com.github.jiizuz.patternrecognition.pattern.util.CentroidFactory;
import com.github.jiizuz.patternrecognition.pattern.util.MathUtils;
import com.github.jiizuz.patternrecognition.pattern.util.RepresentativeCentroid;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.MultimapBuilder;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import lombok.NonNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * K-means is an unsupervised classification algorithm (clustering)
 * that groups objects into k groups based on their characteristics.
 * Clustering is done by minimizing the sum of distances between each
 * object and the centroid of its group or cluster.
 * The quadratic distance is usually used.
 *
 * <p>The algorithm consists of three steps:
 *
 * <ol>
 *     <li>Initialization: once the number of groups, k, has been chosen,
 *     k centroids are established in the data space, for example,
 *     choosing them randomly.</li>
 *     <li>Assigning objects to centroids: each object in the data is
 *     assigned to its closest centroid.</li>
 *     <li>Centroid update: the position of the centroid of each group
 *     is updated taking as a new centroid the position of the average
 *     of the objects belonging to that group.</li>
 * </ol>
 *
 * <p>Steps 2 and 3 are repeated until the centroids do not move, or move
 * below a threshold distance at each step.
 *
 * @param <C> Type of centroids to generate on the classifications.
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @apiNote This class is not Thread-Safe. One instance must be created
 * when using a multi-thread project.
 * @since 1.5
 */
public class KMeans<C extends Centroid> {

    /**
     * Maximum amount of centroids per classifier.
     */
    private static final int MAX_CENTROIDS = 150;

    /**
     * The amount of centroids to generate.
     */
    private final int amount;

    /**
     * {@link CentroidFactory} to create centroids on-demand.
     */
    private final CentroidFactory<C> centroidFactory;

    /**
     * Current centroids.
     */
    private List<C> centroids;

    /**
     * Constructs a new {@link KMeans} with the specified amount of centroids.
     *
     * <p>For the previous version constructor, the next code should be used:
     * <pre>
     *     KMeans<Centroid> kMeans = new KMeans<>(centroids, Centroid::new);
     * </pre>
     *
     * @param centroidsAmount amount of the centroids to generate
     */
    public KMeans(final int centroidsAmount, final @NonNull CentroidFactory<C> factory) {
        amount = Math.min(MAX_CENTROIDS, centroidsAmount);
        centroidFactory = factory;

        // initialize early for the train
        centroids = Lists.newArrayListWithCapacity(amount);
    }

    /**
     * Trains this {@link KMeans} in order to classify incoming patterns.
     *
     * <p>Once the classifier is trained, it can classify other patterns.
     *
     * @param patterns to train with this classifier
     * @param <P>      type of {@link Pattern} to handle
     * @throws NullPointerException if the specified list is <tt>null</tt>
     * @apiNote Multiple trains can be made, but previous train data will
     * be dumped.
     */
    public <P extends Pattern> void train(final @NonNull List<P> patterns) {
        // dump previous data

        centroids.clear();

        // train

        // tracks the patterns already added
        final IntSet added = new IntOpenHashSet();
        // generates pseudo-random indexes
        final Random random = new Random();

        for (int i = 0; i < amount; ++i) {
            double[] vector;

            do {
                vector = patterns.get(getNext(random, added, patterns.size())).getVector();
            } while (containsCentroid(vector));

            final C centroid = centroidFactory.get();
            centroid.setId(i);
            centroid.setVector(vector);

            // add pseudo-random centroids
            centroids.add(centroid);
        }
    }

    /**
     * Groups (classifies) the specified patterns and returns in a
     * {@link ListMultimap} the classified patterns per cluster
     * identified by the {@link Centroid}.
     *
     * @param patterns to classify
     * @param <P>      type of {@link Pattern} to handle
     * @return the classification result grouped by the cluster
     * @throws NullPointerException if the list is <tt>null</tt>
     * @apiNote The specified {@link List} neither the {@link Pattern}
     * are modified in any way, i.e. they can be <tt>immutable</tt>.
     */
    @NonNull
    public <P extends Pattern> ListMultimap<C, P> classify(final @NonNull List<P> patterns) {
        final ListMultimap<C, P> classification = getClassification(patterns);

        final List<C> relocated = relocateCentroids(classification);
        if (centroidDiscrepancy(relocated)) {
            // update the centroids for the next attempt
            centroids = relocated;

            // clear before enter the next recursion to avoid OutOfMemory
            classification.clear();

            return classify(patterns);
        }

        return classification;
    }

    /**
     * Classifies the specified patterns by grouping them based on the minimal
     * euclidean distance from the pattern to the centroid position.
     *
     * @param patterns to classify
     * @param <P>      type of {@link Pattern} to handle
     * @return a {@link ListMultimap} with the patterns identified by their closest centroid
     * @apiNote The pattern's vector size must be the same as centroids size.
     * @see MathUtils#computeEuclideanDistance(double[], double[])
     */
    @NonNull
    private <P extends Pattern> ListMultimap<C, P> getClassification(final @NonNull List<P> patterns) {
        // this map will have exactly centroids#size() centroids with at least 1 pattern
        final ListMultimap<C, P> multimap = MultimapBuilder.ListMultimapBuilder
                .treeKeys(Comparator.comparingInt(Centroid::getId))
                .arrayListValues(16)
                .build();

        for (final P pattern : patterns) {
            final C nearest = centroids.stream()
                    .min(Comparator.comparingDouble(centroid ->
                            MathUtils.computeEuclideanDistance(pattern.getVector(), centroid.getVector())))
                    .orElseThrow(() -> new Error("this should never happen"));

            multimap.put(nearest, pattern);
        }

        return multimap;
    }

    /**
     * Relocates the {@link Centroid} in the specified map by generating the
     * mean value of the {@link Pattern} per centroid and returns those new
     * centroids relocated.
     *
     * <p>The returned {@link List} will have the same amount of centroids
     * as the specified in the construction, i.e.:
     * <pre>
     *     (relocateCentroids(centroidMap).size() == amount) = true;
     * </pre>
     *
     * @param centroidMap classified patterns per centroid to relocate
     * @param <P>         type of {@link Pattern} to handle
     * @return a {@link List} with the new relocated {@link Centroid}
     */
    @NonNull
    private <P extends Pattern> List<C> relocateCentroids(final @NonNull ListMultimap<C, P> centroidMap) {
        final Int2ObjectMap<RepresentativeCentroid> representativeCentroids = new Int2ObjectOpenHashMap<>(amount + 1, 0.99F);

        centroidMap.forEach((centroid, pattern) -> representativeCentroids.computeIfAbsent(centroid.getId(), id ->
                new RepresentativeCentroid(id, centroid.getVector().length)).accumulate(pattern));

        return representativeCentroids.values()
                .stream()
                .peek(RepresentativeCentroid::close)
                .map(this::cloneCentroid)
                .collect(ImmutableList.toImmutableList());
    }

    /**
     * Returns whether the specified {@link List} of centroids have
     * a discrepancy with the current {@link #centroids} list or not.
     *
     * <p>This is simple a deep equals of the centroids.
     *
     * @param other to check whether there is a discrepancy
     * @return <tt>true</tt> if the specified list have a discrepancy
     * TODO Jiizuz: 5/11/21 Implement a threshold
     */
    private boolean centroidDiscrepancy(final @NonNull List<C> other) {
        checkArgument(other.size() == centroids.size(), "other centroids have different size", other.size());

        for (int i = 0; i < amount; i++) {
            if (!centroids.get(i).equals(other.get(i))) {
                return true;
            }
        }

        return false;
    }

    // util

    /**
     * Returns whether a {@link Centroid} already has the specified vector.
     *
     * @param vector to check if it is already a centroid with
     * @return <tt>true</tt> if a centroid already has the specified vector
     */
    private boolean containsCentroid(final double @NonNull [] vector) {
        for (int i = 0, n = centroids.size(); i < n; i++) {
            if (Arrays.equals(centroids.get(i).getVector(), vector)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Searches for the next pseudo-random integer using the specified
     * {@link Random} by ignoring values already in the specified set,
     * limiting the maximum possible value.
     *
     * @param random to generate the next value
     * @param set    that contains already selected values
     * @param max    possible value of the result (exclusive)
     * @return the next found value
     * @apiNote The value will be added to the set once is found.
     */
    private int getNext(final @NonNull Random random, final @NonNull IntSet set, final int max) {
        int next;
        do {
            next = random.nextInt(max);
        } while (!set.add(next));
        return next;
    }

    /**
     * Clones the specified {@link Centroid} into a {@link C} centroid type.
     *
     * @param centroid to clone
     * @return the cloned {@link Centroid} in a {@link C} form
     */
    private C cloneCentroid(final @NonNull Centroid centroid) {
        final C clone = centroidFactory.get();
        clone.setId(centroid.getId());
        clone.setVector(centroid.getVector().clone());
        return clone;
    }
}
