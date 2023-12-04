package ca.bcit.comp2522.termproject.pix.screens;

import ca.bcit.comp2522.termproject.pix.screens.screenelements.BodyText;
import ca.bcit.comp2522.termproject.pix.screens.screenelements.TitleText;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Represents a screen.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-12
 */
public abstract class Screen {
    private final Pane root;
    private final int windowWidth;
    private final int windowHeight;

    /**
     * Constructs a Screen.
     *
     * @param windowWidth the width of the window as an int
     * @param windowHeight the height of the window as an int
     */
    protected Screen(final int windowWidth, final int windowHeight) {
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
     * Gets the width of the window.
     *
     * @return the width of the window as an int
     */
    protected int getWindowWidth() {
        return windowWidth;
    }

    /**
     * Gets the height of the window.
     *
     * @return the height of the window as an int
     */
    protected int getWindowHeight() {
        return windowHeight;
    }

    /**
     * Gets the stage.
     *
     * @return the stage as a Stage
     */
    public Stage getStage() {
        return (Stage) getRoot().getScene().getWindow();
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
