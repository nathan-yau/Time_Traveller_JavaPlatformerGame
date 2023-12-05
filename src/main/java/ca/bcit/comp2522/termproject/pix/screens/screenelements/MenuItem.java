package ca.bcit.comp2522.termproject.pix.screens.screenelements;

import ca.bcit.comp2522.termproject.pix.DefaultFont;
import javafx.beans.binding.Bindings;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

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
     * @param alignment the alignment of the menu item as a TextAlignment
     */
    public MenuItem(final String name, final TextAlignment alignment)  {
        final int textXOffset = 5;
        final int textYOffset = 20;
        final int shadowRadius = 10;
        final int hoverShadowRadius = 25;
        Font font = DefaultFont.getDefaultBodyFont();

        Text text = new Text(name);
        text.setTranslateX(textXOffset);
        text.setTranslateY(textYOffset);
        text.setFont(font);
        text.setFill(Color.WHITE);
        text.setTextAlignment(alignment);
        text.effectProperty().bind(
                Bindings.when(hoverProperty())
                        .then(new DropShadow(hoverShadowRadius, Color.BLACK))
                        .otherwise(new DropShadow(shadowRadius, Color.GRAY)
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

