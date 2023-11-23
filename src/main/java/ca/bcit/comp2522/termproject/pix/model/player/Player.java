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

    /**
     * Sets the Player to the jump speed on Y axis.
     */
    public void setJumpSpeed() {
        if (jumpEnable) {
            final int jumpingVelocity = -10;
            velocity = velocity.add(0, jumpingVelocity);
            jumpEnable = false;
            this.action = Action.JUMPING;
        }
    }

    /**
     * Sets the Player to move along the Y axis based on jump velocity and gravity.
     * @param movementDelta the movement delta
     */
    public void updateHorizontalMovement(final int movementDelta) {
        // Move the Player along X Axis
        for (int i = 0; i < Math.abs(movementDelta); i++) {
            this.moveX(movementDelta > 0);
        }
        this.nextImageFrame();
    }

    /**
     * Sets the Player to move along the Y axis based on jump velocity and gravity.
     */
    public void updateVerticalMovement() {
        final int gravity = 1;
        // Move the Player along Y Axis based on current velocity
        for (int i = 0; i < Math.abs(this.velocity.getY()); i++) {
            this.moveY(this.velocity.getY() > 0);
        }
        // Applying gravity
        if (this.velocity.getY() < gravity) {
            this.velocity = this.velocity.add(0, 1);
        }
    }

    /**
     * Moves the Player in the y direction by 1 pixel.
     * @param movingDown true if moving down, false if moving up
     */
    public void moveY(final boolean movingDown) {
        if (movingDown) {
            this.setTranslateY(this.getTranslateY() + 1);
        } else {
            this.setTranslateY(this.getTranslateY() - 1);
        }
    }

    /**
     * Moves the Player in the x direction by 1 pixel.
     * @param movingRight true if moving right, false if moving left
     */
    @Override
    public void moveX(final boolean movingRight) {
        final int numberOfFrames = 8;
        final int slowDownFrameUpdatesRate = 10;
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
        if (currentImageFrame % slowDownFrameUpdatesRate == 0) {
            this.currentImagePath = String.format("player/%s", direction.name());
            this.updatePlayerImage(String.format("%s/walking_%d.png", currentImagePath,
                    currentImageFrame % numberOfFrames));
        }

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
