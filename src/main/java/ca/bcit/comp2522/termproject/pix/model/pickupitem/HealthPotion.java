package ca.bcit.comp2522.termproject.pix.model.pickupitem;

import ca.bcit.comp2522.termproject.pix.model.ObjectType;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

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
    public HealthPotion(final int x, final int y, final int w, final int h,
                        final int currentLevel, final String imageName) {
        super(x, y, w, h, ObjectType.ITEM, PickUpItemType.HEALTH_POTION, currentLevel, imageName);
    }

    /**
     * Disappear health potion when picked up.
     *
     * @return whether the health potion is picked up as a boolean
     */
    @Override
    public boolean onPickUp() {
        final int pauseDuration = 150;
        PauseTransition pause = new PauseTransition(Duration.millis(pauseDuration));
        pause.setOnFinished(event -> {
            this.setVisible(false);
        });
        pause.play();
        return true;
    }

    /**
     * Use health potion.
     *
     * @return whether the health potion is used as a boolean
     */
    @Override
    public boolean onUse() {
        return false;
    }
}

