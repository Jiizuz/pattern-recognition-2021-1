package com.github.jiizuz.patternrecognition.pattern;

import com.github.jiizuz.patternrecognition.pattern.classification.Classifier;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * {@link Pattern} to <b>Test</b> if a {@link Classifier} is
 * actually classifying successfully certain input {@link Pattern}.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @see com.github.jiizuz.patternrecognition.pattern.Pattern
 * @since 1.0
 */
@SuperBuilder
public class TestPattern extends Pattern {

    /**
     * Class-name expected to be assigned to this {@link Pattern}.
     */
    @Getter
    @NonNull
    private final String expectedClass;

    /**
     * Constructs a new {@link TestPattern} with the specified
     * <tt>vector</tt>> and the <tt>expected class-name</tt>.
     *
     * @param vector        of the {@link Pattern} being tested
     * @param expectedClass class-name that this {@link Pattern} must be assigned
     */
    public TestPattern(final double[] vector, final @NonNull String expectedClass) {
        super(null, vector.clone());
        this.expectedClass = expectedClass;
    }

    /**
     * Converts the specified {@link Pattern} into a {@link TestPattern}.
     *
     * <p>The {@link #getClassName()} of this {@link TestPattern} will be <tt>null</tt>.
     *
     * <p>The {@link #getExpectedClass()} of this {@link TestPattern} will be
     * the {@link Pattern#getClassName()} of the specified {@link Pattern}.
     *
     * @param pattern to map into a {@link TestPattern}
     * @apiNote this method does not save any reference from the {@link Pattern}
     */
    public TestPattern(final @NonNull Pattern pattern) {
        super(null, pattern.getVector().clone());
        this.expectedClass = pattern.getClassName();
    }

    /**
     * Returns if the <tt>Classification</tt> of this {@link TestPattern} was
     * successful.
     *
     * @return <tt>true</tt> if the classification was successful
     */
    public boolean isSuccessClassification() {
        return Objects.equal(expectedClass, className);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("className", className)
                .add("vector", vector)
                .add("expectedClass", expectedClass)
                .add("success", isSuccessClassification())
                .toString();
    }
}
