package com.github.jiizuz.patternrecognition.pattern.chart;

import com.github.jiizuz.patternrecognition.pattern.Pattern;
import com.github.jiizuz.patternrecognition.pattern.display.KMeansRenderer;
import com.github.jiizuz.patternrecognition.pattern.util.Centroid;
import com.google.common.base.Joiner;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.primitives.Doubles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;

import java.io.PrintStream;
import java.util.Comparator;

/**
 * {@link KMeansRenderer} that uses an {@link PrintStream} to redirect
 * the output of the results on the render phase being simple text.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @see com.github.jiizuz.patternrecognition.pattern.display.KMeansRenderer
 * @since 1.6
 */
@Builder
@AllArgsConstructor
public class PrintStreamKMeansRenderer implements KMeansRenderer {

    /**
     * Default appendable to redirect over the output.
     */
    private static final PrintStream DEFAULT_APPENDABLE = System.out;

    /**
     * {@link PrintStream} to redirect over the results.
     */
    @Builder.Default
    private final PrintStream appendable = DEFAULT_APPENDABLE;

    /**
     * {@link ListMultimap} that contains the patterns per centroid to render.
     */
    private final ListMultimap<Centroid, Pattern> centroidMap = MultimapBuilder.ListMultimapBuilder
            .treeKeys(Comparator.comparingInt(Centroid::getId))
            .arrayListValues(16)
            .build();

    /**
     * {@inheritDoc}
     */
    @Override
    public void accumulate(final @NonNull ListMultimap<? extends Centroid, ? extends Pattern> centroidMap) {
        this.centroidMap.putAll(centroidMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderResults() {
        final Joiner patternJoin = Joiner.on('\n');
        centroidMap.asMap().forEach((centroid, patterns) -> {
            appendable.println("------------------------------ CLUSTER -----------------------------------");
            appendable.printf("Centroid %d {%s}, %d patterns%n", centroid.getId() + 1, Doubles.join(", ", centroid.getVector()), patterns.size());
            appendable.println(patternJoin.join(patterns));
            appendable.println();
        });
    }
}
