package ca.bcit.comp2522.termproject.pix.model.player;

import ca.bcit.comp2522.termproject.pix.model.Combative;
import ca.bcit.comp2522.termproject.pix.model.Damageable;
import ca.bcit.comp2522.termproject.pix.model.GameObject;
import ca.bcit.comp2522.termproject.pix.model.Movable;
import ca.bcit.comp2522.termproject.pix.model.ObjectType;

import java.util.concurrent.CompletableFuture;

/**
 * Represents a Player.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023
 */
public class Player extends GameObject<PlayerType> implements Combative, Damageable, Movable {

    /**
     * Constructs a Player object.
     * @param x the x coordinate of the Player
     * @param y the y coordinate of the Player
     * @param imagePath the image path of the Player
     */
    public Player(final double x, final double y, final String imagePath) {
        super(x, y, ObjectType.PLAYER, PlayerType.PLAYER, imagePath);
    }

    @Override
    public void jump() {

    }

    @Override
    public void moveX(final boolean movingRight) {

    }

    @Override
    public void meleeAttack() {

    }

    @Override
    public void rangeAttack() {

    }

    @Override
    public int getAttackDamage() {
        return 0;
    }

    @Override
    public int takeDamage(final int point) {
        return 0;
    }

    @Override
    public void getHurt() {

    }

    @Override
    public CompletableFuture<Boolean> startDying() {
        return null;
    }


}
