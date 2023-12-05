package ca.bcit.comp2522.termproject.pix;

import java.io.IOException;
import java.io.InputStream;
import javafx.scene.text.Font;

/**
 * Class to retrieve the default font for the game.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-12
 */
public final class DefaultFont {
    private static final Font DEFAULT_BODY_FONT;
    private static final Font DEFAULT_TITLE_FONT;

    // Loads the font from the resource folder.
    static {
        final int bodyFontSize = 24;
        final int titleFontSize = 36;
        InputStream fontStream;

        fontStream = DefaultFont.class.getResourceAsStream("BungeeSpice-Regular.ttf");
        if (fontStream != null) {
            DEFAULT_BODY_FONT = Font.loadFont(fontStream, bodyFontSize);
            try {
                fontStream.close();
            } catch (IOException e) {
                System.out.println("Error closing font stream.");
            }
        } else {
            DEFAULT_BODY_FONT = Font.font("Verdana", bodyFontSize);
        }

        fontStream = DefaultFont.class.getResourceAsStream("BungeeSpice-Regular.ttf");
        if (fontStream != null) {
            DEFAULT_TITLE_FONT = Font.loadFont(fontStream, titleFontSize);
            try {
                fontStream.close();
            } catch (IOException e) {
                System.out.println("Error closing font stream.");
            }
        } else {
            DEFAULT_TITLE_FONT = Font.font("Verdana", titleFontSize);
        }
    }

    private DefaultFont() {
    }

    /**
     * Returns the default body font for the game at size 24.
     *
     * @return the default body font for the game as a Font
     */
    public static Font getDefaultBodyFont() {
        return DEFAULT_BODY_FONT;
    }

    /**
     * Returns the default title font for the game at size 36.
     *
     * @return the default title font for the game as a Font
     */
    public static Font getDefaultTitleFont() {
        return DEFAULT_TITLE_FONT;
    }
}
