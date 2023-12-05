package ca.bcit.comp2522.termproject.pix.model.pickupitem;

import ca.bcit.comp2522.termproject.pix.model.ObjectType;

/**
 * Represents a health potion.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public class HealthPotion extends PickUpItem {

    /**
     * Constructs a HealthPotion object.
     *
     * @param x the x-coordinate of the HealthPotion as an int
     * @param y the y-coordinate of the HealthPotion as an int
     * @param w the width of the HealthPotion as an int
     * @param h the height of the HealthPotion as an int
     * @param currentLevel the current game level as an int
     * @param imageName the name of the image file as a String
     */
    public HealthPotion(final int x, final int y, final int w, final int h) {
        super(x, y, w, h, PickUpItemType.HEALTH_POTION);
    }
}

