package ca.bcit.comp2522.termproject.pix.model.pickupitem;

import ca.bcit.comp2522.termproject.pix.model.GameObject;
import ca.bcit.comp2522.termproject.pix.model.ObjectType;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;

/**
 * Represents an item that can be picked up.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public abstract class PickUpItem extends GameObject<PickUpItemType> implements Serializable {
    private final int x;
    private final int y;
    private final int w;
    private final int h;

    /**
     * Constructs a PickUpItem.
     *
     * @param x the x coordinate of the PickUpItem as an int
     * @param y the y coordinate of the PickUpItem as an int
     * @param w the width of the PickUpItem as an int
     * @param h the height of the PickUpItem as an int
     * @param pickUpItemType the name of the PickUpItem as a PickUpItemType
     */
    public PickUpItem(final int x, final int y, final int w, final int h,
                      final PickUpItemType pickUpItemType) {
        super(x, y, w, h, ObjectType.ITEM, pickUpItemType,
                String.format("item/%s.png",  pickUpItemType.name()));
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    /*
     * Loads and sets up an existing Pick Up Item.
     */
    @Serial
    private void readObject(final ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        this.restoreGameObject(this.x, this.y, this.w, this.h);
    }

    /**
     * Disappear the pickup item.
     */
    public void disappearItem() {
        final int pauseDuration = 150;
        PauseTransition pause = new PauseTransition(Duration.millis(pauseDuration));
        pause.setOnFinished(event -> this.setVisible(false));
        pause.play();
    }

    /**
     * Determines what happens when the item is picked up.
     *
     * @return whether the item was picked up as a boolean
     */
    public boolean onPickup() {
        this.disappearItem();
        return true;
    }
}
