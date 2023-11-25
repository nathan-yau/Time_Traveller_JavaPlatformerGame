package ca.bcit.comp2522.termproject.pix.model.block;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.effect.ColorAdjust;
import javafx.util.Duration;

/**
 * Represents a game block that disappears.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public class DisappearingBlock extends StandardBlock {
    /**
     * Constructs a DisappearingBlock.
     *
     * @param x the x-coordinate of the block as an int
     * @param y the y-coordinate of the block as an int
     * @param w the width of the block as an int
     * @param h the height of the block as an int
     * @param blockType the type of the block as a BlockType
     * @param currentLevel the current game level as an int
     * @param imageName the name of the image as a String
     */
    public DisappearingBlock(final int x, final int y, final int w, final int h, final BlockType blockType,
                             final int currentLevel, final String imageName) {
        super(x, y, w, h, blockType, currentLevel, imageName);
    }
}
