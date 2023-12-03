package ca.bcit.comp2522.termproject.pix.menu;

import ca.bcit.comp2522.termproject.pix.DefaultFont;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Represents a menu title.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-12
 */
public class MenuTitle extends Pane {
    /**
     * Constructs a menu title.
     *
     * @param title the title of the menu as a String
     * @param maxWidth the maximum width of the title as a double
     */
    public MenuTitle(final String title, final double maxWidth) {
        final int shadowRadius = 40;
        Font font = DefaultFont.getDefaultTitleFont();

        StringBuilder spread = new StringBuilder();
        for (char c : title.toCharArray()) {
            spread.append(c).append(" ");
        }

        Text text = new Text(spread.toString());
        text.setFont(font);
        text.setFill(Color.WHITE);
        text.setEffect(new DropShadow(shadowRadius, Color.BLACK));
        text.setWrappingWidth(maxWidth);

        getChildren().addAll(text);
    }
}

