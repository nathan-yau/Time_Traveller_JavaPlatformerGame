package ca.bcit.comp2522.termproject.pix.model.pickupitem;

/**
 * Represents a SaveEvent.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public class SaveEvent extends PickUpItem {
    /**
     * Constructs a SaveEvent.
     * @param x the x coordinate of the SaveEvent
     * @param y the y coordinate of the SaveEvent
     * @param w the width of the SaveEvent
     * @param h the height of the SaveEvent
     */
    public SaveEvent(final int x, final int y, final int w, final int h) {
        super(x, y, w, h, PickUpItemType.SAVE_TRIGGER);
    }
}
