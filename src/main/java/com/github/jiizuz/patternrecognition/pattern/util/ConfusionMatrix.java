package com.github.jiizuz.patternrecognition.pattern.util;

import com.github.jiizuz.patternrecognition.pattern.TestPattern;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import static com.google.common.base.Preconditions.checkState;

/**
 * Confusion matrices are extremely powerful shorthand mechanisms
 * for what I call “analytic triage.”
 *
 * <p>Confusion matrices illustrate how samples belonging to a single
 * topic, cluster, or class (rows in the matrix) are assigned to the
 * plurality of possible topics, clusters, or classes.
 *
 * <p>The current implementation is very simple displaying the total
 * count as a columns/rows table using a dimensional int array.
 *
 * <p>The columns are the <b>Actual Class</b> of the assignation and
 * the rows are the <tt>Assigned Class</tt> counter.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @since 1.0
 */
@RequiredArgsConstructor
public class ConfusionMatrix {

    // TODO Jiizuz: 14/9/21 Create a version that allows dynamical matrices

    /**
     * {@link TestPattern}s to be computed in the resultant <tt>Matrix</tt>.
     *
     * @implSpec this {@link List} is read-only
     */
    private final List<TestPattern> patterns;

    /**
     * {@link Map} with the current found <i>classes</i> and their
     * <i>index</i> in the <tt>matrix</tt> that will be computed.
     */
    private final Map<String, Integer> classesIndex = Maps.newHashMap();

    /**
     * Counter to keep track of the amount of <i>classes</i> in the matrix.
     */
    private int count = 0;

    /**
     * Whether the current {@link ConfusionMatrix} was computed and is ready
     * to generate a visual representation of the <tt>Confusion Matrix</tt>.
     */
    private boolean computed = false;

    /**
     * Cache of the matrix being computed.
     */
    private int[][] matrix = null;

    /**
     * Computes this {@link ConfusionMatrix} and maps the data into a bi-dimensional
     * int array in order to compute the visual display of the <tt>Confusion Matrix</tt>.
     *
     * @throws IllegalStateException if this matrix is already computed
     * @see #getDisplayMatrix()
     */
    public void computeMatrix() {
        checkIsNotComputed();

        // map available classes
        for (final TestPattern pattern : patterns) {
            classesIndex.computeIfAbsent(pattern.getClassName(), s -> count++);
        }

        // quadratic matrix based on the amount of classes
        matrix = new int[classesIndex.size()][classesIndex.size()];

        // count classifications
        for (final TestPattern pattern : patterns) {
            final int r = classesIndex.get(pattern.getExpectedClass());
            final int c = classesIndex.get(pattern.getClassName());
            matrix[r][c]++;
        }
        computed = true;

        // clear to let the GC do its work
        classesIndex.clear();
    }

    /**
     * Generates a simple display of the <tt>Confusion Matrix</tt>.
     *
     * @return the generated display
     * @throws IllegalStateException if the matrix is not computed yet
     */
    @NonNull
    public String getDisplayMatrix() {
        checkIsComputed();

        StringBuilder aux = new StringBuilder();
        for (int[] counts : matrix) {
            StringJoiner joiner = new StringJoiner(", ", "| ", "\n");
            for (int operand : counts) {
                joiner.add(String.format("%0,2d", operand));
            }
            aux.append(joiner);
        }
        return aux.toString();
    }

    /**
     * Throws a {@link IllegalStateException} if this matrix is not computed.
     *
     * @throws IllegalStateException if this matrix is not computed
     */
    private void checkIsComputed() {
        checkState(computed, "the Matrix is not computed yet");
    }

    /**
     * Throws a {@link IllegalStateException} if this matrix is already computed.
     *
     * @throws IllegalStateException if this matrix is already computed
     */
    private void checkIsNotComputed() {
        checkState(!computed, "the Matrix was computed already");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("patterns", patterns)
                .add("classesIndex", classesIndex)
                .add("count", count)
                .add("computed", computed)
                .add("matrix", matrix)
                .toString();
    }
}
