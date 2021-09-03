package com.github.jiizuz.patternrecognition.pattern.util;

import lombok.experimental.UtilityClass;

/**
 * Utility class for math methods.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @since 1.0
 */
@UtilityClass
public class MathUtils {

    /**
     * Computes the Euclidean Distance of the specified points
     * and returns the resultant distance between the points.
     *
     * <p>It is important for the points to be in a Euclidean space.
     *
     * <pre>
     *     d<sub>E</sub>(P, Q) = √((p<sub>1</sub> - q<sub>1</sub>)<sup>2</sup> + (p<sub>2</sub> - q<sub>2</sub>)<sup>2</sup> + ... + (p<sub>n</sub> - q<sub>n</sub>)<sup>2</sup>)
     * </pre>
     *
     * @param p1 point one from the plane
     * @param p2 point two from the plane
     * @return the distance between both of the points
     * @throws IllegalArgumentException if the points aren't from the same size
     */
    public double computeEuclideanDistance(final double[] p1, final double[] p2) {
        ensureSameSize(p1, p2);

        double result = 0D;

        for (int i = 0; i < p1.length; ++i) {
            result += Math.pow(p1[i] - p2[i], 2);
        }
        result = Math.sqrt(result);

        return result;
    }

    /**
     * Ensures the specified {@param p1} and {@param p2} have the same sizes.
     *
     * @param p1 to check it has the same size
     * @param p2 to check it has the same size
     * @throws IllegalArgumentException if the vectors does not have the same size
     */
    private void ensureSameSize(double[] p1, double[] p2) throws IllegalArgumentException {
        if (p1.length != p2.length) {
            throw new IllegalArgumentException("p1 and p2 have different sizes");
        }
    }
}
