package ca.bcit.comp2522.termproject.pix.screens.screenelements;

import ca.bcit.comp2522.termproject.pix.DefaultFont;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
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
public class TitleText extends Pane {
    /**
     * Constructs a TitleText.
     *
     * @param title the title as a String
     * @param color the color of the title as a Color
     * @param alignment the alignment of the title as a TextAlignment
     * @param maxWidth the maximum width of the title as a double
     */
    public TitleText(final String title, final Color color, final TextAlignment alignment, final double maxWidth) {
        final int shadowRadius = 40;
        Font font = DefaultFont.getDefaultTitleFont();

        StringBuilder spread = new StringBuilder();
        for (char c : title.toCharArray()) {
            spread.append(c).append(" ");
        }

        Text text = new Text(spread.toString());
        text.setFont(font);
        text.setFill(color);
        text.setTextAlignment(alignment);
        text.setEffect(new DropShadow(shadowRadius, Color.BLACK));
        text.setWrappingWidth(maxWidth);

        getChildren().addAll(text);
    }
}

