package com.github.jiizuz.patternrecognition.color.util;

import lombok.experimental.UtilityClass;

/**
 * {@link UtilityClass} with utility methods related with a Color.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @since 1.7
 */
@UtilityClass
public class ColorUtils {

    /**
     * Checks the color integer components supplied for validity.
     * Throws an {@link IllegalArgumentException} if the value is out of
     * range.
     *
     * @param r the Red component
     * @param g the Green component
     * @param b the Blue component
     * @param a the Alpha component
     **/
    public void checkColorValueRange(final int r, final int g, final int b, final int a) {
        boolean rangeError = false;
        String badComponentString = "";

        if (a < 0 || a > 255) {
            rangeError = true;
            badComponentString = badComponentString + " Alpha";
        }
        if (r < 0 || r > 255) {
            rangeError = true;
            badComponentString = badComponentString + " Red";
        }
        if (g < 0 || g > 255) {
            rangeError = true;
            badComponentString = badComponentString + " Green";
        }
        if (b < 0 || b > 255) {
            rangeError = true;
            badComponentString = badComponentString + " Blue";
        }
        if (rangeError) {
            throw new IllegalArgumentException("Color parameter outside expected range:"
                    + badComponentString);
        }
    }

    /**
     * Checks the color <tt>float</tt> components supplied for validity.
     * Throws an {@link IllegalArgumentException} if the value is out
     * of range.
     *
     * @param r the Red component
     * @param g the Green component
     * @param b the Blue component
     * @param a the Alpha component
     **/
    public static void checkColorValueRange(final float r, final float g, final float b, final float a) {
        boolean rangeError = false;
        String badComponentString = "";

        if (a < 0.0 || a > 1.0) {
            rangeError = true;
            badComponentString = badComponentString + " Alpha";
        }
        if (r < 0.0 || r > 1.0) {
            rangeError = true;
            badComponentString = badComponentString + " Red";
        }
        if (g < 0.0 || g > 1.0) {
            rangeError = true;
            badComponentString = badComponentString + " Green";
        }
        if (b < 0.0 || b > 1.0) {
            rangeError = true;
            badComponentString = badComponentString + " Blue";
        }
        if (rangeError) {
            throw new IllegalArgumentException("Color parameter outside expected range:"
                    + badComponentString);
        }
    }
}
