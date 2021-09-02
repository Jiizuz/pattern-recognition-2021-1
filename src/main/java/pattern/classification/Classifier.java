package pattern.classification;

import lombok.NonNull;
import pattern.Pattern;

import java.util.List;

/**
 * Classifies patterns into their respective class based on the
 * vector of the pattern, normally the classifiers requires to
 * be trained in order to classify another patterns.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @since 1.0
 */
public interface Classifier {

    /**
     * Trains this {@link Classifier} in order to classify incoming patterns.
     *
     * <p>Once a classifier is trained, it can classify other patterns.
     *
     * @param patterns to train with this classifier
     * @throws NullPointerException if the specified list is <tt>null</tt>
     * @implSpec the classifiers may not allow multiple train
     */
    void train(@NonNull List<Pattern> patterns);

    /**
     * Classifies the class-name of the specified {@param pattern} based
     * on the trained data, if no train was made, this method keeps the
     * class-name of the patter in <tt>null</tt>.
     *
     * @param pattern to classify
     * @throws NullPointerException if the specified pattern is <tt>null</tt>
     */
    @NonNull
    ClassifyResults classify(@NonNull Pattern pattern);
}
