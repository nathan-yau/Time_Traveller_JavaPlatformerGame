package ca.bcit.comp2522.termproject.pix.model.pickupitem;

/**
 * Represents a BossEvent object.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public class BossEvent extends PickUpItem {
    /**
     * Constructs a BossEvent object.
     *
     * @param x the x coordinate of the BossEvent
     * @param y the y coordinate of the BossEvent
     * @param w the width of the BossEvent
     * @param h the height of the BossEvent
     */
    public BossEvent(final int x, final int y, final int w, final int h) {
        super(x, y, w, h, PickUpItemType.BOSS_TRIGGER);
    }


}
