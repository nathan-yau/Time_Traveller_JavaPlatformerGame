package ca.bcit.comp2522.termproject.pix.model.block;

import ca.bcit.comp2522.termproject.pix.model.GameObject;
import ca.bcit.comp2522.termproject.pix.model.ObjectType;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.KeyValue;
import javafx.scene.effect.ColorAdjust;
import javafx.util.Duration;

/**
 * Represents a standard stationary game block.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public class StandardBlock extends GameObject<BlockType> {
    private Timeline stepOnAnimation;
    /**
     * Constructs a StandardBlock.
     *
     * @param x the x-coordinate of the block as an int
     * @param y the y-coordinate of the block as an int
     * @param w the width of the block as an int
     * @param h the height of the block as an int
     * @param blockType the type of the block as a BlockType
     * @param currentLevel the current game level as an int
     * @param imageName the name of the image as a String
     */
    public StandardBlock(final int x, final int y, final int w, final int h, final BlockType blockType,
                         final int currentLevel, final String imageName) {
        super(x, y, w, h, ObjectType.BLOCK, blockType, String.format("%d/%s/%s.png",
                currentLevel, blockType.name(), imageName));

        ColorAdjust colorAdjust = new ColorAdjust();
        this.setEffect(colorAdjust);
        stepOnAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(colorAdjust.brightnessProperty(), 0.0)),
                new KeyFrame(Duration.millis(300), new KeyValue(colorAdjust.brightnessProperty(), 0.25)),
                new KeyFrame(Duration.millis(300), new KeyValue(colorAdjust.brightnessProperty(), 0))
        );
        stepOnAnimation.setCycleCount(1);
        stepOnAnimation.setAutoReverse(true);
    }

    public void animate() {
        stepOnAnimation.play();
    }

    public void fadeAnimate() {
        stepOnAnimation.stop();
    }
}
