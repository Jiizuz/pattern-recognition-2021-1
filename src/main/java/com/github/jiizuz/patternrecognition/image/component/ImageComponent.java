package com.github.jiizuz.patternrecognition.image.component;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.awt.*;

/**
 * {@link JComponent} to draw a single {@link Image}.
 *
 * @author <a href="mailto:masterchack92@hotmail.com">Jiizuz</a>
 * @see javax.swing.JComponent
 * @since 1.7
 */
@RequiredArgsConstructor
public class ImageComponent extends JComponent {

    /**
     * Image to render.
     */
    @NonNull
    private final Image image;

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintComponent(final Graphics graphics) {
        graphics.drawImage(image, 0, 0, this);
    }
}
