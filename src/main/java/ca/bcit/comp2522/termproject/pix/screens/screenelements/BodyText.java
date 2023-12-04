package ca.bcit.comp2522.termproject.pix.screens.screenelements;

import ca.bcit.comp2522.termproject.pix.DefaultFont;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
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
public class BodyText extends Pane {
    /**
     * Constructs a BodyText.
     *
     * @param content the body text as a String
     * @param color the color of the body text as a Color
     * @param alignment the alignment of the body text as a TextAlignment
     * @param maxWidth the maximum width of the body text as a double
     */
    public BodyText(final String content, final Color color, final TextAlignment alignment, final double maxWidth) {
        final int shadowRadius = 20;
        Font font = DefaultFont.getDefaultBodyFont();

        StringBuilder spread = new StringBuilder();
        for (char c : content.toCharArray()) {
            spread.append(c).append(" ");
        }

        Text text = new Text(spread.toString());
        text.setFont(font);
        text.setFill(color);
        text.setTextAlignment(alignment);
        text.setEffect(new DropShadow(shadowRadius, Color.GRAY));
        text.setWrappingWidth(maxWidth);

        getChildren().addAll(text);
    }
}

