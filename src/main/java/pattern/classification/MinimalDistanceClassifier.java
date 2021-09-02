package pattern.classification;

import lombok.NonNull;
import pattern.Pattern;
import pattern.RepresentativePattern;

import java.util.*;

/**
 * {@link Classifier} that uses the average of the data and finds
 * the minimal distance to classify new incoming patterns.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @see pattern.classification.Classifier
 * @since 1.0
 */
public class MinimalDistanceClassifier implements Classifier {

    /**
     * Storage of the available representations.
     */
    private final Map<String, RepresentativePattern> representations = new HashMap<>();

    /**
     * Status to allow or not new trains.
     */
    private boolean closed = false;

    /**
     * {@inheritDoc}
     */
    @Override
    public void train(final @NonNull List<Pattern> patterns) {
        checkCloseStatus();
        closed = true;

        for (final Pattern pattern : patterns) {
            final RepresentativePattern representation = getRepresentation(pattern);

            representation.accumulate(pattern);
        }

        representations.values().forEach(RepresentativePattern::close);
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    public ClassifyResults classify(final @NonNull Pattern pattern) {
        final double[] vector = pattern.getVector();
        final double vectorAverage = getAverage(vector);

        final Map<String, Double> compatibilities = new HashMap<>(4);

        String foundClassName = null;
        double currentDistance = Integer.MAX_VALUE;

        for (final RepresentativePattern representation : representations.values()) {
            final double[] representationVector = representation.getVector();

            final double average = getAverage(representationVector);

            final double distance = Math.abs(average - vectorAverage);

            final double min = Math.min(average, vectorAverage);
            final double max = Math.max(average, vectorAverage);
            compatibilities.put(representation.getClassName(), min / max * 100D);

            if (distance <= currentDistance) {
                foundClassName = representation.getClassName();
                currentDistance = distance;
            }
        }

        pattern.setClassName(foundClassName);

        return ClassifyResultsImpl.builder()
                .compatibilities(sortByValue(compatibilities))
                .build();
    }

    /**
     * Returns the representative pattern of the specified {@param pattern}.
     *
     * <p>If the representation does not exist, a new will be created.
     *
     * @param pattern to retrieve the representation of
     * @return the representation of the pattern
     * @throws NullPointerException if the specified {@param pattern} is <tt>null</tt>
     */
    @NonNull
    private RepresentativePattern getRepresentation(final @NonNull Pattern pattern) throws NullPointerException {
        final String className = pattern.getClassName();
        if (Objects.isNull(className)) {
            throw new NullPointerException("name of pattern");
        }

        RepresentativePattern representation = representations.get(className);

        if (Objects.isNull(representation)) {
            representation = new RepresentativePattern(className, pattern.getVector().length);
            representations.put(className, representation);
        }

        return representation;
    }

    /**
     * Checks if this algorithm is closed.
     *
     * @throws IllegalStateException if this algorithm is closed
     */
    private void checkCloseStatus() throws IllegalStateException {
        if (closed) {
            throw new IllegalStateException("The algorithm is already closed");
        }
    }

    /**
     * Sorts the specified {@param map} and returns a copy sorted.
     *
     * @param <K> type of the key of the map
     * @param <V> type of value of the map to sort with, must be Comparable
     * @param map to sort by value
     * @return the sorted copy of the map
     */
    @NonNull
    private static <K, V extends Comparable<V>> Map<K, V> sortByValue(final @NonNull Map<K, V> map) {
        final LinkedHashMap<K, V> sortedMap = new LinkedHashMap<>();

        map.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

        return sortedMap;
    }

    /**
     * Returns the average of the specified {@param elements}.
     *
     * @param elements to derive from the average
     * @return the average of the elements
     */
    private double getAverage(final double[] elements) {
        double sum = 0D;
        for (final double element : elements) {
            sum += element;
        }
        return sum / elements.length;
    }
}
