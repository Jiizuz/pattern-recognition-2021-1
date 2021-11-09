package com.github.jiizuz.patternrecognition.pattern;

import com.github.jiizuz.patternrecognition.color.util.ColorUtils;
import com.google.common.base.MoreObjects;
import lombok.Getter;

import java.awt.image.ColorModel;

/**
 * {@link Pattern} that holds the Red, Green and Blue values
 * of a single <tt>Pixel</tt> in an image, this pattern holds
 * also the X and Y coordinates of the pixel.
 *
 * <p>This class is based on {@link java.awt.Color}.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @see com.github.jiizuz.patternrecognition.pattern.Pattern
 * @since 1.7
 */
@Getter
public class PixelPattern extends Pattern {

    /**
     * Coordinate in the X axis.
     */
    private final int x;

    /**
     * Coordinate in the Y axis.
     */
    private final int y;

    /**
     * Creates a new {@link PixelPattern} at the specified coords with
     * an opaque <tt>sRGB</tt> color with the specified red, green, and
     * blue values in the range [0 - 255].
     * The actual color used in rendering depends on finding the best
     * match given the color space available for a given output device.
     * Alpha is defaulted to 255.
     *
     * @param x coordinate in the X axis of the pixel
     * @param y coordinate in the Y axis of the pixel
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @throws IllegalArgumentException if <tt>r</tt>, <tt>g</tt> or <tt>b</tt>
     *                                  are outside the range 0 to 255, inclusive
     * @see #getX
     * @see #getY
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     * @see #getRGB
     * @see #getVector
     */
    public PixelPattern(final int x, final int y, final int r, final int g, final int b) {
        this(x, y, r, g, b, 255);
    }

    /**
     * Creates a new {@link PixelPattern} at the specified coords with
     * an <tt>sRGB</tt> color with the specified red, green, blue and
     * alpha values in the range [0 - 255].
     *
     * @param x coordinate in the X axis of the pixel
     * @param y coordinate in the Y axis of the pixel
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @param a the alpha component
     * @throws IllegalArgumentException if <tt>r</tt>, <tt>g</tt>, <tt>b</tt> or
     *                                  <tt>a</tt> are outside the range 0 to 255,
     *                                  inclusive
     * @see #getX
     * @see #getY
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     * @see #getRGB
     * @see #getVector
     */
    public PixelPattern(final int x, final int y, final int r, final int g, final int b, final int a) {
        super("", new double[] { r, g, b, a });

        this.x = x;
        this.y = y;

        ColorUtils.checkColorValueRange(r, g, b, a);
    }

    /**
     * Creates a new {@link PixelPattern} at the specified coords with
     * an opaque <tt>sRGB</tt> color with the specified combined RGB value
     * consisting of the red component in bits 16-23, the green component
     * in bits 8-15, and the blue component in bits 0-7. The actual color
     * used in rendering depends on finding the best match given the
     * color space available for a particular output device.  Alpha is
     * defaulted to 255.
     *
     * @param x   coordinate in the X axis of the pixel
     * @param y   coordinate in the Y axis of the pixel
     * @param rgb the combined RGB components
     * @see #getX
     * @see #getY
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     * @see #getRGB
     * @see #getVector
     */
    public PixelPattern(final int x, final int y, final int rgb) {
        this(x, y, (rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF, ((rgb >> 24) & 0xFF) | 0xFF);
    }

    /**
     * Creates a new {@link PixelPattern} at the specified coords with an
     * <tt>sRGB</tt> color with the specified combined RGBA value consisting
     * of the alpha component in bits 24-31, the red component in bits 16-23,
     * the green component in bits 8-15, and the blue component in bits 0-7.
     * If the <tt>hasAlpha</tt> argument is <tt>false</tt>, alpha is defaulted
     * to 255.
     *
     * @param x        coordinate in the X axis of the pixel
     * @param y        coordinate in the Y axis of the pixel
     * @param rgba     the combined RGBA components
     * @param hasAlpha <tt>true</tt> if the alpha bits are valid;
     *                 <tt>false</tt> otherwise
     * @see #getX
     * @see #getY
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     * @see #getRGB
     * @see #getVector
     */
    public PixelPattern(final int x, final int y, final int rgba, final boolean hasAlpha) {
        this(x, y, (rgba >> 16) & 0xFF, (rgba >> 8) & 0xFF, rgba & 0xFF, hasAlpha ? ((rgba >> 24) & 0xFF) : 0xFF);
    }

    /**
     * Creates a new {@link PixelPattern} at the specified coords with an
     * opaque <tt>sRGB</tt> color with the specified red, green, and blue
     * values in the range [0.0 - 1.0].  Alpha is defaulted to 1.0.  The
     * actual color used in rendering depends on finding the best match
     * given the color space available for a particular output device.
     *
     * @param x coordinate in the X axis of the pixel
     * @param y coordinate in the Y axis of the pixel
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @throws IllegalArgumentException if <tt>r</tt>, <tt>g</tt> or
     *                                  <tt>b</tt> are outside the
     *                                  range 0.0 to 1.0, inclusive
     * @see #getX
     * @see #getY
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     * @see #getRGB
     * @see #getVector
     */
    public PixelPattern(final int x, final int y, final float r, final float g, final float b) {
        this(x, y, (int) (r * 255 + 0.5), (int) (g * 255 + 0.5), (int) (b * 255 + 0.5), 255);

        ColorUtils.checkColorValueRange(r, g, b, 1.0F);
    }

    /**
     * Creates a new {@link PixelPattern} at the specified coords with an
     * <tt>sRGB</tt> color with the specified red, green, blue, and alpha
     * values in the range (0.0 - 1.0). The actual color used in rendering
     * depends on finding the best match given the color space available
     * for a particular output device.
     *
     * @param x coordinate in the X axis of the pixel
     * @param y coordinate in the Y axis of the pixel
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @param a the alpha component
     * @throws IllegalArgumentException if <tt>r</tt>, <tt>g</tt>, <tt>b</tt>
     *                                  or <tt>a</tt> are outside the range
     *                                  0.0 to 1.0, inclusive
     * @see #getX
     * @see #getY
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     * @see #getRGB
     * @see #getVector
     */
    public PixelPattern(final int x, final int y, final float r, final float g, final float b, final float a) {
        this(x, y, (int) (r * 255 + 0.5), (int) (g * 255 + 0.5), (int) (b * 255 + 0.5), (int) (a * 255 + 0.5));

        ColorUtils.checkColorValueRange(r, g, b, a);
    }

    /**
     * Returns the red component in the range 0-255 in the default sRGB space.
     *
     * @return the red component.
     * @see #getRGB
     */
    public int getRed() {
        return (int) vector[0];
    }

    /**
     * Returns the green component in the range 0-255 in the default sRGB space.
     *
     * @return the green component.
     * @see #getRGB
     */
    public int getGreen() {
        return (int) vector[1];
    }

    /**
     * Returns the blue component in the range 0-255 in the default sRGB space.
     *
     * @return the blue component.
     * @see #getRGB
     */
    public int getBlue() {
        return (int) vector[2];
    }

    /**
     * Returns the alpha component in the range 0-255.
     *
     * @return the alpha component.
     * @see #getRGB
     */
    public int getAlpha() {
        return (int) vector[3];
    }

    /**
     * Returns the RGB value representing the color in the default sRGB
     * {@link ColorModel}.
     * (Bits 24-31 are alpha, 16-23 are red, 8-15 are green, 0-7 are blue).
     *
     * @return the RGB value of the color in the default sRGB <tt>ColorModel</tt>.
     * @see ColorModel#getRGBdefault
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     */
    public int getRGB() {
        final int r = getRed();
        final int g = getGreen();
        final int b = getBlue();
        final int a = getAlpha();

        return ((a & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                (b & 0xFF);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("x", x)
                .add("y", y)
                .add("rgba", vector)
                .toString();
    }
}
