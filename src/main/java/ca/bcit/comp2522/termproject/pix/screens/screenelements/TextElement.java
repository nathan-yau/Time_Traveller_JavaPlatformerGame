package ca.bcit.comp2522.termproject.pix.screens.screenelements;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Represents a text element.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-12
 */
public abstract class TextElement extends Pane {
    private final String textContent;

    /**
     * Constructs a TextElement.
     *
     * @param textContent content of the TextElement as a String
     * @param textColor color of the TextElement as a Color
     * @param textAlignment alignment of the TextElement as a TextAlignment
     * @param textMaxWidth maximum width of the TextElement as a double
     */
    public TextElement(final String textContent, final Color textColor,
                       final TextAlignment textAlignment, final double textMaxWidth) {
        this.textContent = textContent;
        String spreadContent = spreadText();
        getChildren().add(formatText(spreadContent, textColor, textAlignment, textMaxWidth));
    }

    // Spread the text content using a space.
    private String spreadText() {
        StringBuilder spread = new StringBuilder();
        for (char c : this.textContent.toCharArray()) {
            spread.append(c).append(" ");
        }
        return spread.toString();
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
    protected abstract Text formatText(String spreadContent, Color color, TextAlignment alignment, double maxWidth);
}
