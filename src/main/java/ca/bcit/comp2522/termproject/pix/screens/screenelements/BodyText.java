package ca.bcit.comp2522.termproject.pix.screens.screenelements;

import ca.bcit.comp2522.termproject.pix.DefaultFont;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Represents body text.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-12
 */
public class BodyText extends TextElement {

    /**
     * Constructs a BodyText.
     *
     * @param content the body text as a String
     * @param color the color of the body text as a Color
     * @param alignment the alignment of the body text as a TextAlignment
     * @param maxWidth the maximum width of the body text as a double
     */
    public BodyText(final String content, final Color color, final TextAlignment alignment, final double maxWidth) {
        super(content, color, alignment, maxWidth);
    }

    /**
     * Formats the text.
     *
     * @param spreadContent the spread content of the text to be formatted as a String
     * @param color the color of the text as a Color
     * @param alignment the alignment of the text as a TextAlignment
     * @param maxWidth the maximum width of the text as a double
     * @return the formatted text as a Text
     */
    @Override
    protected Text formatText(final String spreadContent, final Color color,
                                       final TextAlignment alignment, final double maxWidth) {
        final int shadowRadius = 20;
        Font font = DefaultFont.getDefaultBodyFont();
        Text text = new Text(spreadContent);

        text.setFont(font);
        text.setFill(color);
        text.setTextAlignment(alignment);
        text.setEffect(new DropShadow(shadowRadius, Color.GRAY));
        text.setWrappingWidth(maxWidth);

        return text;
    }
}

