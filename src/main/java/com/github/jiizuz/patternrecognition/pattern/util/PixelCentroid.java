package com.github.jiizuz.patternrecognition.pattern.util;

import com.github.jiizuz.patternrecognition.color.util.ColorUtils;
import com.google.common.base.MoreObjects;
import lombok.NonNull;

import java.awt.image.ColorModel;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * {@link Centroid} that holds the Red, Green, Blue and Alpha
 * components of a single <tt>Pixel</tt> in an image.
 *
 * <p>A {@link PixelCentroid} does not hold the X, Y coordinate
 * of the pixel itself, since the centroids have a non-centered
 * position in an image, it just holds the <tt>sRGB</tt> value.
 *
 * <p>This class is based on {@link java.awt.Color}.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @apiNote This is useful to retrieve direct methods with the components.
 * @see com.github.jiizuz.patternrecognition.pattern.util.Centroid
 * @since 1.7
 */
public class PixelCentroid extends Centroid implements Cloneable {

    /**
     * Creates a new {@link PixelCentroid} with a white <tt>sRGB</tt>
     * color. The components are set to 255. The actual color used in
     * rendering depends on finding the best match given the color
     * space available for a given output device.
     *
     * <p>This constructor is recommended for lazy initialization.
     */
    public PixelCentroid() {
        this(0, 0xFFFFFFFF); // white color
    }

    /**
     * Creates a new {@link PixelCentroid} with an <tt>sRGB</tt> color with
     * the specified red, green, and blue values in the range [0 - 255].
     * The actual color used in rendering depends on finding the best match
     * given the color space available for a given output device.
     *
     * <p>The vector must have 3 or 4 elements being red, green, blue and
     * alpha components correspondingly. Alpha is defaulted to 255 if not
     * present.
     *
     * @param id     of the centroid
     * @param vector the combined RGB components
     * @throws IllegalArgumentException if <tt>red</tt>, <tt>green</tt>, <tt>blue</tt> or
     *                                  <tt>alpha</tt> are outside the range 0 to 255, inclusive
     * @apiNote This constructor is useful with data like {@link Centroid(int, double[])}.
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     * @see #getAlpha
     * @see #getRGB
     * @see #getVector
     */
    public PixelCentroid(final int id, final double @NonNull [] vector) {
        this(id, (int) vector[0], (int) vector[1], (int) vector[2], vector.length == 3 ? 255 : (int) vector[3]);

        checkArgument(vector.length == 3 || vector.length == 4, "invalid vector length", vector.length);
    }

    /**
     * Creates a new {@link PixelCentroid} with an opaque <tt>sRGB</tt>
     * color with the specified red, green, and blue values in the range
     * [0 - 255]. The actual color used in rendering depends on finding
     * the best match given the color space available for a given output
     * device. Alpha is defaulted to 255.
     *
     * @param id of the centroid
     * @param r  the red component
     * @param g  the green component
     * @param b  the blue component
     * @throws IllegalArgumentException if <tt>r</tt>, <tt>g</tt> or <tt>b</tt>
     *                                  are outside the range 0 to 255, inclusive
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     * @see #getRGB
     * @see #getVector
     */
    public PixelCentroid(final int id, final int r, final int g, final int b) {
        this(id, r, g, b, 255);
    }

    /**
     * Creates a new {@link PixelCentroid} with an <tt>sRGB</tt> color with
     * the specified red, green, blue and alpha values in the range [0 - 255].
     *
     * @param id of the centroid
     * @param r  the red component
     * @param g  the green component
     * @param b  the blue component
     * @param a  the alpha component
     * @throws IllegalArgumentException if <tt>r</tt>, <tt>g</tt>, <tt>b</tt> or
     *                                  <tt>a</tt> are outside the range 0 to 255,
     *                                  inclusive
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     * @see #getRGB
     * @see #getVector
     */
    public PixelCentroid(final int id, final int r, final int g, final int b, final int a) {
        super(id, new double[] { r, g, b, a });

        ColorUtils.checkColorValueRange(r, g, b, a);
    }

    /**
     * Creates a new {@link PixelCentroid} with an opaque <tt>sRGB</tt>
     * color with the specified combined RGB value consisting of the red
     * component in bits 16-23, the green component in bits 8-15, and the
     * blue component in bits 0-7.   The actual color used in rendering
     * depends on finding the best match given the color space available
     * for a particular output device. Alpha is defaulted to 255.
     *
     * @param id  of the centroid
     * @param rgb the combined RGB components
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     * @see #getRGB
     * @see #getVector
     */
    public PixelCentroid(final int id, final int rgb) {
        this(id, (rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF, ((rgb >> 24) & 0xFF) | 0xFF);
    }

    /**
     * Creates a new {@link PixelCentroid} with an <tt>sRGB</tt> color with
     * the specified combined RGBA value consisting of the alpha component
     * in bits 24-31, the red component in bits 16-23, the green component
     * in bits 8-15, and the blue component in bits 0-7. If the <tt>hasAlpha</tt>
     * argument is <tt>false</tt>, alpha is defaulted to 255.
     *
     * @param id       of the centroid
     * @param rgba     the combined RGBA components
     * @param hasAlpha <tt>true</tt> if the alpha bits are valid;
     *                 <tt>false</tt> otherwise
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     * @see #getRGB
     * @see #getVector
     */
    public PixelCentroid(final int id, final int rgba, final boolean hasAlpha) {
        this(id, (rgba >> 16) & 0xFF, (rgba >> 8) & 0xFF, rgba & 0xFF, hasAlpha ? ((rgba >> 24) & 0xFF) : 0xFF);
    }

    /**
     * Creates a new {@link PixelCentroid} with an opaque <tt>sRGB</tt>
     * color with the specified red, green, and blue values in the range
     * [0.0 - 1.0].  Alpha is defaulted to 1.0. The actual color used in
     * rendering depends on finding the best match given the color space
     * available for a particular output device.
     *
     * @param id of the centroid
     * @param r  the red component
     * @param g  the green component
     * @param b  the blue component
     * @throws IllegalArgumentException if <tt>r</tt>, <tt>g</tt> or
     *                                  <tt>b</tt> are outside the
     *                                  range 0.0 to 1.0, inclusive
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     * @see #getRGB
     * @see #getVector
     */
    public PixelCentroid(final int id, final float r, final float g, final float b) {
        this(id, (int) (r * 255 + 0.5), (int) (g * 255 + 0.5), (int) (b * 255 + 0.5), 255);

        ColorUtils.checkColorValueRange(r, g, b, 1.0F);
    }

    /**
     * Creates a new {@link PixelCentroid} with an <tt>sRGB</tt> color with
     * the specified red, green, blue, and alpha values in the range (0.0 - 1.0).
     * The actual color used in rendering depends on finding the best match
     * given the color space available for a particular output device.
     *
     * @param id of the centroid
     * @param r  the red component
     * @param g  the green component
     * @param b  the blue component
     * @param a  the alpha component
     * @throws IllegalArgumentException if <tt>r</tt>, <tt>g</tt>, <tt>b</tt>
     *                                  or <tt>a</tt> are outside the range
     *                                  0.0 to 1.0, inclusive
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     * @see #getRGB
     * @see #getVector
     */
    public PixelCentroid(final int id, final float r, final float g, final float b, final float a) {
        this(id, (int) (r * 255 + 0.5), (int) (g * 255 + 0.5), (int) (b * 255 + 0.5), (int) (a * 255 + 0.5));

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
        return ((getAlpha() & 0xFF) << 24) |
                ((getRed() & 0xFF) << 16) |
                ((getGreen() & 0xFF) << 8) |
                (getBlue() & 0xFF);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PixelCentroid clone() {
        return (PixelCentroid) super.clone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("rgba", Arrays.toString(vector))
                .toString();
    }
}
