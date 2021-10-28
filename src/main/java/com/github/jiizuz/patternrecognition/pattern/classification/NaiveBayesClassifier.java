package com.github.jiizuz.patternrecognition.pattern.classification;

import com.github.jiizuz.patternrecognition.pattern.Pattern;
import com.github.jiizuz.patternrecognition.pattern.util.MathUtils;
import com.github.jiizuz.patternrecognition.pattern.util.Naive;
import com.google.common.base.MoreObjects;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import lombok.NonNull;

import java.util.List;

import static com.google.common.base.Preconditions.checkState;

/**
 * In statistics, naive Bayes classifiers are a family of simple "probabilistic
 * classifiers" based on applying Bayes' theorem with strong (naïve) independence
 * assumptions between the features. They are among the simplest Bayesian network
 * models, but coupled with kernel density estimation, they can achieve higher
 * accuracy levels.
 * <p>
 * Naïve Bayes classifiers are highly scalable, requiring a number of parameters
 * linear in the number of variables (features/predictors) in a learning problem.
 * Maximum-likelihood training can be done by evaluating a closed-form expression,
 * which takes linear time, rather than by expensive iterative approximation as
 * used for many other types of classifiers.
 * <p>
 * In the statistics and computer science literature, naive Bayes models are known
 * under a variety of names, including simple Bayes and independence Bayes. All
 * these names reference the use of Bayes' theorem in the classifier's decision
 * rule, but naïve Bayes is not (necessarily) a Bayesian method.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @see com.github.jiizuz.patternrecognition.pattern.classification.Classifier
 * @since 1.4
 */
public class NaiveBayesClassifier implements Classifier {

    /**
     * {@link List} to store the naives trained.
     */
    private final ObjectList<Naive> naives = new ObjectArrayList<>(32);

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

        // Store the naives per class-name.
        final Object2ObjectMap<String, Naive> naivesMap = new Object2ObjectOpenHashMap<>(32, 0.85F);

        // accumulate patterns
        patterns.forEach(pattern -> naivesMap.computeIfAbsent(pattern.getClassName(), name -> new Naive(pattern))
                .getRepresentative().accumulate(pattern));

        // close naives and calculate and priors
        naivesMap.values().forEach(naive -> {
            naive.close();
            naive.calculatePrior(patterns.size()); // must be called after the close of the naive
        });

        // append variances
        patterns.forEach(pattern -> naivesMap.get(pattern.getClassName())
                .appendVariance(pattern));

        naives.addAll(naivesMap.values()); // the order actually does not matter, each naive is iterated anyway
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void classify(final @NonNull Pattern pattern) {
        final double[][] distribution = new double[naives.size()][pattern.getVector().length];

        // calculate a matrix normal distribution
        double evidence = 0.0D;

        for (int i = 0; i < naives.size(); ++i) {
            final Naive naive = naives.get(i);

            double prior = naive.getPrior(); // actual final product

            for (int j = 0; j < pattern.getVector().length; ++j) {
                prior *= distribution[i][j] =
                        calculateNormalDistribution(pattern.getVector()[j], naive.getRepresentative().getVector()[j], naive.getVariance()[j]);
            }

            evidence += prior;
        }

        double currDelta = -1;
        String foundClassName = null;

        for (int i = 0; i < naives.size(); ++i) {
            final Naive naive = naives.get(i);

            double prior = naive.getPrior(); // actual final product

            for (int j = 0; j < pattern.getVector().length; ++j) {
                prior *= distribution[i][j];
            }

            prior /= evidence;

            if (prior > currDelta) { // find max
                currDelta = prior;
                foundClassName = naive.getName();
            }
        }

        pattern.setClassName(foundClassName);
    }

    /**
     * In probability theory, a normal (or Gaussian or Gauss or Laplace–Gauss) distribution is a
     * type of continuous probability distribution for a real-valued random variable. The general
     * form of its probability density function is:
     *
     * <pre>
     *     P(<tt>X</tt> = <tt>v</tt> | C<sub><tt>k</tt></sub>) = 1 / &#8730;((2&#928;σ<sup>2</sup><sub><tt>k</tt></sub>)) · e<sup>-((<tt>v</tt> - <tt>u</tt><sub><tt>k</tt></sub>)<sup>2</sup>/(2σ<sup>2</sup><sub><tt>k</tt></sub>))</sup>
     * </pre>
     *
     * @param v        continuous attribute
     * @param u        mean of values in v
     * @param variance of the distribution
     * @see <a href="https://en.wikipedia.org/wiki/Normal_distribution">Normal distribution</a>
     */
    private double calculateNormalDistribution(final double v, final double u, final double variance) {
        final double exponent = Math.exp(-(MathUtils.square(v - u) / (2 * MathUtils.square(variance))));
        return (1 / (Math.sqrt(2 * Math.PI) * variance)) * exponent;
    }

    // util

    /**
     * Checks if this algorithm is closed.
     *
     * @throws IllegalStateException if this algorithm is closed
     */
    private void checkCloseStatus() throws IllegalStateException {
        checkState(!closed, "The algorithm is already closed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("naives", naives)
                .add("closed", closed)
                .toString();
    }
}
