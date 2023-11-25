package ca.bcit.comp2522.termproject.pix.model.player;

import ca.bcit.comp2522.termproject.pix.MainApplication;
import ca.bcit.comp2522.termproject.pix.model.AttackEffect.AttackEffect;
import ca.bcit.comp2522.termproject.pix.model.AttackEffect.MeleeEffect;
import ca.bcit.comp2522.termproject.pix.model.Combative;
import ca.bcit.comp2522.termproject.pix.model.Damageable;
import ca.bcit.comp2522.termproject.pix.model.GameObject;
import ca.bcit.comp2522.termproject.pix.model.Movable;
import ca.bcit.comp2522.termproject.pix.model.ObjectType;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

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
    private boolean attackEnable = true;
    private boolean harmable = true;
    private String currentImagePath;
    private int currentImageFrame;
    private Action action;
    private Direction direction;
    private Point2D velocity;
    private int healthPotionCounter;
    private int goldCoinCounter;
    private Timeline attackingAnimation;

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
        this.currentImagePath = String.format("player/%s", direction.name());
        this.initializeAttackingAnimation();
    }

    private void initializeAttackingAnimation() {
        final int[] meleeFrame = {0};
        attackingAnimation = new Timeline(
                new KeyFrame(Duration.millis(100), event -> {
                    this.action = Action.MELEE_ATTACK;
                    System.out.printf("%s/melee_attack_%d.png%n", currentImagePath, meleeFrame[0]);
                    this.updatePlayerImage(String.format("%s/melee_attack_%d.png", currentImagePath,
                            (meleeFrame[0] % 5)));
                    harmable = false;
                    meleeFrame[0]++;
                })
        );
        attackingAnimation.setCycleCount(5);
        attackingAnimation.setOnFinished(event -> {
            attackEnable = true;
            harmable = true;
        });
    }

    /**
     * Gets the velocity of the Player.
     * @return the velocity of the Player
     */
    public double getVelocityY() {
        return velocity.getY();
    }

    /**
     * Sets the Player to the jump speed on Y axis.
     */
    public void setJumpSpeed() {
        if (jumpEnable) {
            final int jumpingVelocity = -25;
            velocity = velocity.add(0, jumpingVelocity);
            jumpEnable = false;
        }
    }

    /**
     *  Sets the Player when it is on the ground.
     */
    public void offsetGravity() {
        //Offset Gravity by 1 if on the ground
        this.setTranslateY(this.getTranslateY() - 1);
        jumpEnable = true;
        if (this.action == Action.JUMPING) {
            this.action = Action.IDLE;
        }
    }

    /**
     * Sets the Player to move along the Y axis based on jump velocity and gravity.
     */
    public void applyGravity() {
        final int gravity = 3;
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
            this.setTranslateY(this.getTranslateY() + 0.8);
        } else {
            this.setTranslateY(this.getTranslateY() - 0.6);
            this.currentImagePath = String.format("player/%s", direction.name());
            if (this.action != Action.JUMPING) {
                this.updatePlayerImage(String.format("%s/jumping_0.png", currentImagePath));
                this.action = Action.JUMPING;
            }
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

    /**
     * Updates the Player image.
     * @param imageUrl the image path of the Player
     */
    private void updatePlayerImage(final String imageUrl) {
        this.setImage(new Image(String.valueOf(MainApplication.class.getResource(imageUrl))));
    }

    public boolean isPlayerInAction() {
        return action == Action.JUMPING
                || action == Action.MELEE_ATTACK
                || action == Action.RANGE_ATTACK
                || action == Action.HURTING;
    }

    public void setIdle() {
        this.updatePlayerImage(String.format("%s/idle.png", currentImagePath));
        this.action = Action.IDLE;
    }

    /**
     * Increments the player's health potion counter by one.
     */
    public void incrementHealthPotionCounter() {
        this.healthPotionCounter++;
    }

    /**
     * Decrements the player's health potion counter by one.
     */
    public void decrementHealthPotionCounter() {
        this.healthPotionCounter--;
    }

    /**
     * Gets the player's health potion counter.
     * @return the player's health potion counter
     */
    public int getHealthPotionCounter() {
        return this.healthPotionCounter;
    }

    /**
     * Increments the player's gold coin counter by one.
     */
    public void incrementGoldCoinCounter() {
        this.goldCoinCounter++;
    }

    /**
     * Gets the player's gold coin counter.
     *
     * @return the player's gold coin counter
     */
    public int getGoldCoinCounter() {
        return this.goldCoinCounter;
    }

    @Override
    public AttackEffect meleeAttack() {
        if (attackEnable) {
            attackEnable = false;
            attackingAnimation.play();
            double effectY = this.getBoundsInParent().getMinY();
            double effectX = this.getBoundsInParent().getMinX() - 50;
            if (direction == Direction.FORWARD) {
                effectX = this.getBoundsInParent().getMaxX();
            }
            return new MeleeEffect(effectX, effectY + 10, 50, 50, "Explosion");
        }
        return null;
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
