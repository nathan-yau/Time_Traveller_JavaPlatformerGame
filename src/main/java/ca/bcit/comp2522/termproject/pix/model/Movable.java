package ca.bcit.comp2522.termproject.pix.model;

/**
 * Represents an object that can move by player.
 * This interface is used to implement the jump and moveX methods.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023
 */
public interface Movable {

    /**
     * Makes the object to move up or fall.
     *
     * @param movingDown true if falling, false if jumping
     */
    void moveY(boolean movingDown);

    /**
     * Makes the object move left or right.
     *
     * @param movingRight true if moving right, false if moving left
     */
    void moveX(boolean movingRight);
}
