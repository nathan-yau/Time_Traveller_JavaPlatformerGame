package ca.bcit.comp2522.termproject.pix.model.pickupitem;

public class Ammo extends PickUpItem {
    /**
     * Constructs a PickUpItem.
     *
     * @param x              the x coordinate of the PickUpItem as an int
     * @param y              the y coordinate of the PickUpItem as an int
     * @param w              the width of the PickUpItem as an int
     * @param h              the height of the PickUpItem as an int
     */
    public Ammo(int x, int y, int w, int h) {
        super(x, y, w, h, PickUpItemType.AMMO);
    }
}
