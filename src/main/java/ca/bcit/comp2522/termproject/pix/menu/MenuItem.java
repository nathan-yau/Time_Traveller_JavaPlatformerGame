package ca.bcit.comp2522.termproject.pix.menu;

import ca.bcit.comp2522.termproject.pix.DefaultFont;
import javafx.beans.binding.Bindings;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Represents a menu item.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-12
 */
public class MenuItem extends Pane {

    /**
     * Constructs a menu item.
     *
     * @param name the name of the menu item as a String
     */
    public MenuItem(final String name)  {
        final int textXOffset = 5;
        final int textYOffset = 20;
        final int shadowRadius = 25;
        Font font = DefaultFont.getDefaultBodyFont();

        Text text = new Text(name);
        text.setTranslateX(textXOffset);
        text.setTranslateY(textYOffset);
        text.setFont(font);
        text.setFill(Color.WHITE);
        text.effectProperty().bind(
                Bindings.when(hoverProperty())
                        .then(new DropShadow(shadowRadius, Color.BLACK))
                        .otherwise(new DropShadow(0, Color.BLACK)
                        )
        );
        text.cursorProperty().bind(
                Bindings.when(hoverProperty())
                        .then(Cursor.HAND)
                        .otherwise(Cursor.DEFAULT)
        );
        getChildren().addAll(text);
    }
}

