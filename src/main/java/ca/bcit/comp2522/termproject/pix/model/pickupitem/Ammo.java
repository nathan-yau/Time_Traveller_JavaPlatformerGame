package ca.bcit.comp2522.termproject.pix.model.pickupitem;

/**
 * Represents an Ammo object.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public class Ammo extends PickUpItem {
    /**
     * Constructs an Ammo object.
     *
     * @param x the x coordinate of the Ammo
     * @param y the y coordinate of the Ammo
     * @param w the width of the Ammo
     * @param h the height of the Ammo
     */
    public Ammo(final int x, final int y, final int w, final int h) {
        super(x, y, w, h, PickUpItemType.AMMO);
    }
}
