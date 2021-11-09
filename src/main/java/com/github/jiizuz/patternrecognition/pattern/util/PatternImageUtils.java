package com.github.jiizuz.patternrecognition.pattern.util;

import com.github.jiizuz.patternrecognition.image.util.ImageUtils;
import com.github.jiizuz.patternrecognition.pattern.PixelPattern;
import com.google.common.collect.ImmutableList;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * {@link UtilityClass} with utility methods related with a pattern image.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @since 1.7
 */
@UtilityClass
public class PatternImageUtils {

    /**
     * Parses all the pixels from the specified image and generates a
     * list with the pixels based on a set of {@link PixelPattern}.
     *
     * @param image to retrieve from the pixels
     * @return all the pixels in the specified image as a pattern
     * @apiNote The returned {@link List} is <tt>immutable</tt>.
     */
    @NonNull
    public List<PixelPattern> getPatterns(final @NonNull Image image) {
        final BufferedImage bufferedImage = ImageUtils.toBufferedImage(image);

        final ImmutableList.Builder<PixelPattern> list = new ImmutableList.Builder<>();

        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                final int rgb = bufferedImage.getRGB(x, y);

                // do not consider alpha
                list.add(new PixelPattern(x, y, rgb, false));
            }
        }

        return list.build();
    }
}
