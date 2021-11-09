package com.github.jiizuz.patternrecognition.image.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * {@link UtilityClass} with utility methods related with images.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @since 1.7
 */
@UtilityClass
public class ImageUtils {

    /**
     * Converts a given Image into a BufferedImage.
     *
     * @param img the Image to be converted
     * @return the converted BufferedImage
     * @see <a href="https://stackoverflow.com/questions/13605248/java-converting-image-to-bufferedimage">Taken from</a>
     */
    @NonNull
    public BufferedImage toBufferedImage(final @NonNull Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        final BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        final Graphics2D bGr = bufferedImage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        return bufferedImage;
    }
}
