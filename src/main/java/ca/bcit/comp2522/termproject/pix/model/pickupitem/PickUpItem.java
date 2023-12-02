package ca.bcit.comp2522.termproject.pix.model.pickupitem;

import ca.bcit.comp2522.termproject.pix.model.GameObject;
import ca.bcit.comp2522.termproject.pix.model.ObjectType;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

/**
 * Represents an item that can be picked up.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public abstract class PickUpItem extends GameObject<PickUpItemType> {
    // The type of item pickup.
    private final PickUpItemType pickUpItemType;

    /**
     * Constructs a PickUpItem.
     *
     * @param x the x coordinate of the PickUpItem as an int
     * @param y the y coordinate of the PickUpItem as an int
     * @param w the width of the PickUpItem as an int
     * @param h the height of the PickUpItem as an int
     * @param itemType the type of the PickUpItem as an ObjectType
     * @param pickUpItemType the name of the PickUpItem as a PickUpItemType
     * @param currentLevel the current level of the PickUpItem as an int
     * @param imageName the name of the image file of the PickUpItem as a String
     */
    public PickUpItem(final int x, final int y, final int w, final int h, final ObjectType itemType,
                      final PickUpItemType pickUpItemType, final int currentLevel, final String imageName) {
        super(x, y, w, h, itemType, pickUpItemType,
                String.format("%d/%s/%s.png", currentLevel, pickUpItemType.name(), imageName));
        this.pickUpItemType = pickUpItemType;
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

    /**
     * Gets the type of the item.
     *
     * @return the type of the item as a PickUpItemType
     */
    public PickUpItemType getItemType() {
        return this.pickUpItemType;
    }
}
