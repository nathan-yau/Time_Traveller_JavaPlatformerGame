package ca.bcit.comp2522.termproject.pix.model;

import java.util.concurrent.CompletableFuture;

/**
 * Represents an object that can take damage.
 * This interface is used to implement the takeDamage, getHurt, and startDying methods.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023
 */
public interface Damageable {

    /**
     * Takes damage.
     *
     * @param point the damage point
     * @return the remaining health point
     */
    int takeDamage(int point);

    /**
     * Gets hurt.
     */
    void getHurt();

    /**
     * Starts the dying animation.
     *
     * @return a boolean representing the end of the dying animation
     */
    CompletableFuture<Boolean> startDying();
}
