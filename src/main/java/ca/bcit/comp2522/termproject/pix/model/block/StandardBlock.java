package ca.bcit.comp2522.termproject.pix.model.block;

import ca.bcit.comp2522.termproject.pix.model.GameObject;
import ca.bcit.comp2522.termproject.pix.model.ObjectType;

/**
 * Represents a standard stationary game block.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public class StandardBlock extends GameObject<BlockType> {
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
    }
}
