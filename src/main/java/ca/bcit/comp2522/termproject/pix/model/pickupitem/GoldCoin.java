package ca.bcit.comp2522.termproject.pix.model.pickupitem;

import ca.bcit.comp2522.termproject.pix.model.ObjectType;

/**
 * Represents a gold coin pickup item.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public class GoldCoin extends PickUpItem {
    /**
     * Constructs a GoldCoin.
     *
     * @param x the x coordinate of the GoldCoin as an int
     * @param y the y coordinate of the GoldCoin as an int
     * @param w the width of the GoldCoin as an int
     * @param h the height of the GoldCoin as an int
     * @param currentLevel the current game level as an int
     * @param imageName the image name of the GoldCoin as a String
     */
    public GoldCoin(final int x, final int y, final int w, final int h,
                    final int currentLevel, final String imageName) {
        super(x, y, w, h, ObjectType.ITEM, PickUpItemType.GOLD_COIN, currentLevel, imageName);
    }
}
