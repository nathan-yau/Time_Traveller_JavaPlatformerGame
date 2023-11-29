package ca.bcit.comp2522.termproject.pix.model.pickupitem;

import ca.bcit.comp2522.termproject.pix.model.ObjectType;

/**
 * Represents a weapon.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public class WeaponPickup extends PickUpItem {
    /**
     * Constructs a weapon pickup.
     *
     * @param x the x coordinate of the weapon pickup as an int
     * @param y the y coordinate of the weapon pickup as an int
     * @param w  the width of the weapon pickup as an int
     * @param h  the height of the weapon pickup as an int
     * @param weaponPickupType the name of the weapon pickup as a PickUpItemType
     * @param currentLevel the current level of the weapon pickup as an int
     * @param imageName the name of the image file of the weapon pickup as a String
     */
    public WeaponPickup(final int x, final int y, final int w, final int h, final PickUpItemType weaponPickupType,
                        final int currentLevel, final String imageName) {
        super(x, y, w, h, ObjectType.ITEM, weaponPickupType, currentLevel, imageName);
    }
}
