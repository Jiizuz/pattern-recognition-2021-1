package com.github.jiizuz.patternrecognition.pattern.util;

import com.github.jiizuz.patternrecognition.image.util.ImageUtils;
import com.github.jiizuz.patternrecognition.pattern.PixelPattern;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ListMultimap;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

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

    /**
     * Draws the {@link PixelPattern} contained in the specified map but with
     * the actual {@code sRGB} value of the {@link PixelCentroid} owner of it.
     *
     * @param pixelMap map with the pixels per centroid to draw on the image
     * @param image    to set over the sRGB pixels
     * @param xOffset  to set over the image in the X axis
     * @param yOffset  to set over the image in the Y axis
     * @throws IllegalArgumentException if the pixel map is empty
     * @throws NullPointerException     if either the map or image are <tt>null</tt>
     */
    public void writeCentroidPixels(final @NonNull ListMultimap<PixelCentroid, PixelPattern> pixelMap, final @NonNull BufferedImage image, final int xOffset, final int yOffset) {
        checkArgument(!pixelMap.isEmpty(), "empty pixel map");

        // set centroid rgb on pixels
        pixelMap.asMap().forEach((centroid, pixels) -> {
            final int rgb = centroid.getRGB(); // this method computes the result, stored for performance

            pixels.forEach(pixel -> image.setRGB(xOffset + pixel.getX(), yOffset + pixel.getY(), rgb));
        });
    }

    /**
     * Draws the {@link PixelPattern} contained in the specified map but with
     * the actual {@code sRGB} value of the {@link PixelCentroid} owner of it.
     *
     * @param pixelMap map with the pixels per centroid to draw on the image
     * @param image    to set over the sRGB pixels
     * @throws IllegalArgumentException if the pixel map is empty
     * @throws NullPointerException     if either the map or image are <tt>null</tt>
     * @apiNote This method sets offsets at <tt>0</tt>
     * @see #writeCentroidPixels(ListMultimap, BufferedImage, int, int)
     */
    public void writeCentroidPixels(final @NonNull ListMultimap<PixelCentroid, PixelPattern> pixelMap, final @NonNull BufferedImage image) {
        writeCentroidPixels(pixelMap, image, 0, 0);
    }
}
