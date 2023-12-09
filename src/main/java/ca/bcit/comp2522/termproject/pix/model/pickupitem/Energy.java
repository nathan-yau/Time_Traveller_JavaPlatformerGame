package ca.bcit.comp2522.termproject.pix.model.pickupitem;

/**
 * Represents an energy plate pickup item.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public class Energy extends PickUpItem {
    /**
     * Constructs an Energy Plate.
     *
     * @param x the x coordinate of the energy plate as an int
     * @param y the y coordinate of the energy plate as an int
     * @param w the width of the energy plate as an int
     * @param h the height of the energy plate as an int
     */
    public Energy(final int x, final int y, final int w, final int h) {
        super(x, y, w, h, PickUpItemType.ENERGY);
    }
}
