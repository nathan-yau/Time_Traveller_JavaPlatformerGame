package ca.bcit.comp2522.termproject.pix.screens;

import ca.bcit.comp2522.termproject.pix.Command;
import ca.bcit.comp2522.termproject.pix.screens.screenelements.BodyText;
import ca.bcit.comp2522.termproject.pix.screens.screenelements.MenuItem;
import ca.bcit.comp2522.termproject.pix.screens.screenelements.TitleText;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * Represents a screen.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-12
 */
public class Screen {
    private final Pane root;
    private final int windowWidth;
    private final int windowHeight;

    /**
     * Constructs a Screen.
     *
     * @param windowWidth the width of the window as an int
     * @param windowHeight the height of the window as an int
     */
    public Screen(final int windowWidth, final int windowHeight) {
        this.root = new Pane();
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }

    /**
     * Adds a background.
     *
     * @param imageName the name of the image as a String
     */
    public void addBackground(final String imageName) {
        ImageView bgImg = new ImageView(new Image(
                Objects.requireNonNull(getClass().getResourceAsStream(imageName))));
        bgImg.setFitHeight(this.windowHeight);
        bgImg.setFitWidth(this.windowWidth);
        root.getChildren().add(bgImg);
    }

    /**
     * Add a menu.
     *
     * @param menuItems the menu items as a LinkedHashMap
     * @param alignment the alignment of the menu items as a TextAlignment
     * @param xOffset the x offset of the menu items as an int
     * @param yOffset the y offset of the menu items as an int
     * @param itemTopPadding the top padding of the menu items as an int
     */
    public void addMenu(final LinkedHashMap<String, Command> menuItems, final TextAlignment alignment,
                        final int xOffset, final int yOffset, final int itemTopPadding) {
        final int menuBoxOffset = -5;
        VBox menuBox = new VBox(menuBoxOffset);

        menuBox.setTranslateX(xOffset);
        menuBox.setTranslateY(yOffset);

        menuItems.forEach((key, value) -> {
            MenuItem item;
            item = new MenuItem(key, alignment);
            item.setOnMouseClicked(event -> value.execute());
            item.setPadding(new javafx.geometry.Insets(itemTopPadding, 0, 0, 0));
            menuBox.getChildren().addAll(item);
        });
        getRoot().getChildren().add(menuBox);
    }

    /**
     * Add a title.
     *
     * @param titleCopy the title as a String
     * @param maxWidth the maximum width of the title as an int
     * @param color the color of the title as a Color
     * @param alignment the alignment of the title as a TextAlignment
     * @param xOffset the x offset of the title as an int
     * @param yOffset the y offset of the title as an int
     */
    public void addTitle(final String titleCopy, final Color color, final TextAlignment alignment,
                         final int maxWidth, final int xOffset, final int yOffset) {
        TitleText title = new TitleText(titleCopy, color, alignment, maxWidth);

        title.setTranslateX(xOffset);
        title.setTranslateY(yOffset);

        getRoot().getChildren().add(title);
    }

    /**
     * Add body text.
     *
     * @param textCopy the text as a String
     * @param maxWidth the maximum width of the text as an int
     * @param color the color of the text as a Color
     * @param alignment the alignment of the text as a TextAlignment
     * @param xOffset the x offset of the text as an int
     * @param yOffset the y offset of the text as an int
     */
    public void addBodyText(final String textCopy, final Color color, final TextAlignment alignment,
                            final int maxWidth, final int xOffset, final int yOffset) {
        BodyText text = new BodyText(textCopy, color, alignment, maxWidth);

        text.setTranslateX(xOffset);
        text.setTranslateY(yOffset);

        getRoot().getChildren().add(text);
    }

    /**
     * Gets the root of the Screen.
     *
     * @return the root of the Screen as a Pane
     */
    public Pane getRoot() {
        return root;
    }
}
