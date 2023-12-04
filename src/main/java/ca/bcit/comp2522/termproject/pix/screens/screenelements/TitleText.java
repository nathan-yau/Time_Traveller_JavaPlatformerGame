package ca.bcit.comp2522.termproject.pix.screens.screenelements;

import ca.bcit.comp2522.termproject.pix.DefaultFont;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Represents a title.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-12
 */
public class TitleText extends TextElement {

    /**
     * Constructs a TitleText.
     *
     * @param content the title as a String
     * @param color the color of the title as a Color
     * @param alignment the alignment of the title as a TextAlignment
     * @param maxWidth the maximum width of the title as a double
     */
    public TitleText(final String content, final Color color, final TextAlignment alignment, final double maxWidth) {
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
        final int shadowRadius = 40;
        Font font = DefaultFont.getDefaultTitleFont();
        Text text = new Text(spreadContent);

        text.setFont(font);
        text.setFill(color);
        text.setTextAlignment(alignment);
        text.setEffect(new DropShadow(shadowRadius, Color.BLACK));
        text.setWrappingWidth(maxWidth);

        return text;
    }
}


