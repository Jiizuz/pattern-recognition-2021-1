package com.github.jiizuz.patternrecognition.pattern.classification;

import com.github.jiizuz.patternrecognition.pattern.Pattern;
import com.github.jiizuz.patternrecognition.pattern.util.MathUtils;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkState;

/**
 * In statistics, the <b>k-nearest neighbors algorithm</b> (<b>k-NN</b>) is a
 * non-parametric classification method first developed by Evelyn Fix and
 * Joseph Hodges in 1951, and later expanded by Thomas Cover. It is used for
 * classification and regression. In both cases, the input consists of the
 * k-closest training examples in data set. The output depends on whether
 * k-NN is used for classification or regression:
 *
 * <ul>
 * <li>In <i>k-NN classification</i>, the output is a class membership. An
 * object is classified by a plurality vote of its neighbors, with the object
 * being assigned to the class most common among its <tt>k</tt> nearest neighbors
 * (<tt>k</tt> is a positive integer, typically small). If <tt>k = 1</tt>, then
 * the object is simply assigned to the class of that single nearest neighbor.</li>
 * <li>In <tt>k-NN</tt> regression, the output is the property value for the object.
 * This value is the average of the values of <tt>k</tt> nearest neighbors.</li>
 * </ul>
 *
 * <p><tt>k</tt>-NN is a type of classification where the function is only approximated
 * locally and all computation is deferred until function evaluation. Since this
 * algorithm relies on distance for classification, if the features represent different
 * physical units or come in vastly different scales then normalizing the training data
 * can improve its accuracy dramatically.
 *
 * <p>The neighbors are taken from a set of objects for which the class (for <tt>k</tt>-NN
 * classification) or the object property value (for <tt>k</tt>-NN regression) is known.
 * This can be thought of as the training set for the algorithm, though no explicit training
 * step is required.
 *
 * <p>A peculiarity of the k-NN algorithm is that it is sensitive to the local structure of the data.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @see com.github.jiizuz.patternrecognition.pattern.classification.Classifier
 * @since 1.0
 */
@RequiredArgsConstructor
public class KNearestNeighborsClassifier implements Classifier {

    /**
     * <tt>K</tt> value to use on the classification.
     */
    private final int k;

    /**
     * {@link Lock} to <tt>lock</tt> the classifier.
     */
    private final Lock lock = new ReentrantLock(true);

    /**
     * {@link Map} with the ordinal of the classes found in the train.
     */
    private final Map<String, Integer> ordinals = Maps.newHashMapWithExpectedSize(4);

    /**
     * Index to count progressively the classes index.
     */
    private int classIndex = 0;

    /**
     * Status to allow or not new trains.
     */
    private boolean closed = false;

    /**
     * {@link List} of the train {@link Pattern}s.
     *
     * <p>This list is a <tt>deep copy</tt> of the <tt>train</tt>.
     *
     * <p>This list is <tt>mutable</tt> in order to allow <tt>sorts</tt>.
     */
    private List<Pattern> trainPatterns = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public void train(final @NonNull List<Pattern> patterns) {
        checkCloseStatus();
        closed = true;

        trainPatterns = patterns.stream()
                .map(Pattern::clone)
                // take advantage of the iteration to save the classes
                .peek(pattern -> ordinals.computeIfAbsent(pattern.getClassName(), s -> classIndex++))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void classify(final @NonNull Pattern pattern) {
        lock.lock();
        try {
            checkState(closed, "the algorithm was not trained yet");

            trainPatterns.sort(Comparator.comparingDouble(value ->
                    MathUtils.computeEuclideanDistance(pattern.getVector(), value.getVector())));

            final int[] counters = new int[classIndex];

            for (Pattern trainPattern : trainPatterns) {
                final int ordinal = ordinals.get(trainPattern.getClassName());
                if (++counters[ordinal] == k) {
                    pattern.setClassName(trainPattern.getClassName());
                    return;
                }
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Checks if this classifier is closed.
     *
     * @throws IllegalStateException if this classifier is closed
     */
    private void checkCloseStatus() throws IllegalStateException {
        checkState(!closed, "the classifier is already closed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("k", k)
                .add("ordinals", ordinals)
                .add("classIndex", classIndex)
                .add("closed", closed)
                .add("trainPatterns", trainPatterns)
                .toString();
    }
}
