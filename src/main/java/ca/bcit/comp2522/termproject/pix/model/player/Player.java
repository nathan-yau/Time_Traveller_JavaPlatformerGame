package ca.bcit.comp2522.termproject.pix.model.player;

import ca.bcit.comp2522.termproject.pix.MainApplication;
import ca.bcit.comp2522.termproject.pix.model.Combative;
import ca.bcit.comp2522.termproject.pix.model.Damageable;
import ca.bcit.comp2522.termproject.pix.model.GameObject;
import ca.bcit.comp2522.termproject.pix.model.Movable;
import ca.bcit.comp2522.termproject.pix.model.ObjectType;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import java.util.concurrent.CompletableFuture;

/**
 * Represents a Player.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023
 */
public final class Player extends GameObject<PlayerType> implements Combative, Damageable, Movable {
    private boolean jumpEnable = true;
    private String currentImagePath;
    private int currentImageFrame;
    private Action action;
    private Direction direction;
    private Point2D velocity;
    /**
     * Constructs a Player object.
     * @param x the x coordinate of the Player
     * @param y the y coordinate of the Player
     * @param imagePath the image path of the Player
     */
    public Player(final double x, final double y, final String imagePath) {
        super(x, y, ObjectType.PLAYER, PlayerType.PLAYER, imagePath);
        this.velocity = new Point2D(0, 0);
        this.direction = Direction.FORWARD;
        this.action = Action.IDLE;
        this.currentImageFrame = 0;
    }

    /**
     * Gets the velocity of the Player.
     * @return the velocity of the Player
     */
    public Point2D getVelocity() {
        return velocity;
    }

    /**
     * Sets the velocity of the Player.
     * @param x the x coordinate of the velocity
     * @param y the y coordinate of the velocity
     */
    public void setVelocity(final int x, final int y) {
        velocity = velocity.add(x, y);
    }

    @Override
    public void jump() {
        if (jumpEnable) {
            final int jumpingVelocity = -30;
            velocity = velocity.add(0, jumpingVelocity);
            jumpEnable = false;
            this.action = Action.JUMPING;
        }
    }

    /**
     * Moves the Player in the x direction.
     */
    @Override
    public void moveX(final boolean movingRight) {
        final int numberOfFrames = 8;
        if (movingRight) {
            this.direction = Direction.FORWARD;
            this.setTranslateX(this.getTranslateX() + 1);
        } else {
            this.direction = Direction.BACKWARD;
            this.setTranslateX(this.getTranslateX() - 1);
        }
        if (this.action != Action.WALKING) {
            this.currentImageFrame = 0;
        }
        this.currentImagePath = String.format("player/%s", direction.name());
        this.updatePlayerImage(String.format("%s/walking_%d.png", currentImagePath,
                currentImageFrame % numberOfFrames));
        this.action = Action.WALKING;
    }

    /**
     * Set to the next frame of the Player.
     */
    public void nextImageFrame() {
        currentImageFrame += 1;
    }

    private void updatePlayerImage(final String imageUrl) {
        this.setImage(new Image(String.valueOf(MainApplication.class.getResource(imageUrl))));
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
