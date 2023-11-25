package ca.bcit.comp2522.termproject.pix.model.player;

import ca.bcit.comp2522.termproject.pix.MainApplication;
import ca.bcit.comp2522.termproject.pix.model.AttackEffect.AttackEffect;
import ca.bcit.comp2522.termproject.pix.model.AttackEffect.MeleeEffect;
import ca.bcit.comp2522.termproject.pix.model.AttackEffect.RangeEffect;
import ca.bcit.comp2522.termproject.pix.model.Combative;
import ca.bcit.comp2522.termproject.pix.model.Damageable;
import ca.bcit.comp2522.termproject.pix.model.GameObject;
import ca.bcit.comp2522.termproject.pix.model.Movable;
import ca.bcit.comp2522.termproject.pix.model.ObjectType;
import javafx.animation.Animation;
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
    private static final double WALK_SPEED = 5;
    private static final double RUN_SPEED = 10;
    private boolean jumpEnable = true;
    private boolean attackEnable = true;
    private double speed;
    private String currentImagePath;
    private Action action;
    private Direction direction;
    private Point2D velocity;
    private int healthPotionCounter;
    private int goldCoinCounter;
    private Timeline meleeAnimation;
    private Timeline rangeAnimation;
    private Timeline walkAnimation;
    private Timeline jumpAnimation;

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
        this.currentImagePath = String.format("player/%s", direction.name());
        this.initializeMeleeAttackingAnimation();
        this.initializeRangeAttackingAnimation();
        this.initializeWalkingAnimation();
        this.initializeJumpingAnimation();
        this.speed = WALK_SPEED;
    }

    /**
     * Gets the speed of the Player.
     * @return the speed of the Player
     */
    public double getSpeed() {
        return this.speed;
    }

    /**
     * Sets the speed of the Player to run.
     */
    public void run() {
        if (this.speed == WALK_SPEED) {
            this.speed = RUN_SPEED;
        }
    }

    /**
     * Sets the speed of the Player to walk.
     */
    public void walk() {
        if (this.speed == RUN_SPEED) {
            this.speed = WALK_SPEED;
        }
    }

    /*
     * Initializes the jumping animation.
     */
    private void initializeJumpingAnimation() {
        final int[] jumpFrame = {0};
        final int jumpDuration = 100;
        final int jumpFrameCount = 8;
        jumpAnimation = new Timeline(
                new KeyFrame(Duration.millis(jumpDuration), event -> {
                    this.action = Action.JUMPING;
                    this.currentImagePath = String.format("player/%s", direction.name());
                    this.updatePlayerImage(String.format("%s/Jumping_%d.png", currentImagePath, jumpFrame[0]));
                    jumpFrame[0] = (jumpFrame[0] + 1) % (jumpFrameCount + 1);
                })
        );
        jumpAnimation.setCycleCount(jumpFrameCount);
        jumpAnimation.setOnFinished(event -> this.setIdle());
    }

    /*
     * Initializes the walking animation.
     */
    private void initializeWalkingAnimation() {
        final int[] walkFrame = {0};
        final int walkDuration = 100;
        final int walkFrameCount = 7;
        walkAnimation = new Timeline(
                new KeyFrame(Duration.millis(walkDuration), event -> {
                    this.currentImagePath = String.format("player/%s", direction.name());
                    this.updatePlayerImage(String.format("%s/walking_%d.png", currentImagePath, walkFrame[0]));
                    this.action = Action.WALKING;
                    walkFrame[0] = (walkFrame[0] + 1) % (walkFrameCount + 1);
                })
        );
        walkAnimation.setCycleCount(Animation.INDEFINITE);
        walkAnimation.setOnFinished(event -> this.setIdle());
    }

    /*
     * Initializes the range attacking animation.
     */
    private void initializeRangeAttackingAnimation() {
        final int[] rangeFrame = {0};
        final int rangeAttackDuration = 100;
        final int rangeAttackFrameCount = 7;
        rangeAnimation = new Timeline(
                new KeyFrame(Duration.millis(rangeAttackDuration), event -> {
                    this.action = Action.RANGE_ATTACK;
                    this.updatePlayerImage(String.format("%s/range_attack_%d.png", currentImagePath, rangeFrame[0]));
                    rangeFrame[0] = (rangeFrame[0] + 1) % (rangeAttackFrameCount + 1);
                })
        );
        rangeAnimation.setCycleCount(rangeAttackFrameCount);
        rangeAnimation.setOnFinished(event -> this.setIdle());
    }

    /*
     * Initializes the melee attacking animation.
     */
    private void initializeMeleeAttackingAnimation() {
        final int[] meleeFrame = {0};
        final int meleeAttackDuration = 100;
        final int meleeAttackFrameCount = 4;
        meleeAnimation = new Timeline(
                new KeyFrame(Duration.millis(meleeAttackDuration), event -> {
                    this.action = Action.MELEE_ATTACK;
                    this.updatePlayerImage(String.format("%s/melee_attack_%d.png", currentImagePath, meleeFrame[0]));
                    meleeFrame[0] = (meleeFrame[0] + 1) % (meleeAttackFrameCount + 1);
                })
        );
        meleeAnimation.setCycleCount(meleeAttackFrameCount);
        meleeAnimation.setOnFinished(event -> this.setIdle());
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
        this.setTranslateY(this.getTranslateY() - 1);
        jumpEnable = true;
    }

    /**
     * Sets the Player to move along the Y axis based on jump velocity and gravity.
     */
    public void applyGravity() {
        final int gravity = 7;
        if (this.velocity.getY() < gravity) {
            this.velocity = this.velocity.add(0, 1);
        }
    }

    /**
     * Moves the Player in the y direction by 1 pixel.
     * @param movingDown true if moving down, false if moving up
     */
    public void moveY(final boolean movingDown) {
        final double jumpingPixel = 0.8;
        if (movingDown) {
            this.setTranslateY(this.getTranslateY() + 1);
        } else {
            this.currentImagePath = String.format("player/%s", direction.name());
            walkAnimation.stop();
            jumpAnimation.play();
            this.setTranslateY(this.getTranslateY() - jumpingPixel);
            this.action = Action.JUMPING;
        }
    }

    /**
     * Moves the Player in the x direction by 1 pixel.
     * @param movingRight true if moving right, false if moving left
     */
    @Override
    public void moveX(final boolean movingRight) {
        this.attackEnable = false;
        if (movingRight) {
            this.direction = Direction.FORWARD;
            this.setTranslateX(this.getTranslateX() + 1);
        } else {
            this.direction = Direction.BACKWARD;
            this.setTranslateX(this.getTranslateX() - 1);
        }
        jumpAnimation.stop();
        walkAnimation.play();
    }

    /**
     * Updates the Player image.
     * @param imageUrl the image path of the Player
     */
    private void updatePlayerImage(final String imageUrl) {
        this.setImage(new Image(String.valueOf(MainApplication.class.getResource(imageUrl))));
    }

    /**
     * Checks if the Player is in action.
     * @return true if the Player is in action, false otherwise
     */
    public boolean isPlayerInAction() {
        return action == Action.JUMPING
                || action == Action.MELEE_ATTACK
                || action == Action.RANGE_ATTACK
                || action == Action.HURTING;
    }

    /**
     * Sets the Player to idle.
     */
    public void setIdle() {
        if (this.action != Action.IDLE) {
            this.updatePlayerImage(String.format("%s/idle.png", currentImagePath));
            this.action = Action.IDLE;
            walkAnimation.stop();
            jumpAnimation.stop();
            this.attackEnable = true;
        }

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

    /**
     * Create a melee attack hit box.
     * @return the melee attack hit box
     */
    @Override
    public AttackEffect meleeAttack() {
        if (attackEnable) {
            final int hitBoxWidth = 50;
            final int hitBoxHeight = 50;
            final int effectYOffset = 10;
            attackEnable = false;
            meleeAnimation.play();
            double effectY = this.getBoundsInParent().getMinY();
            double effectX = this.getBoundsInParent().getMinX() - hitBoxWidth;
            if (direction == Direction.FORWARD) {
                effectX = this.getBoundsInParent().getMaxX();
            }
            return new MeleeEffect(effectX, effectY + effectYOffset, hitBoxWidth, hitBoxHeight,
                    "Explosion");
        }
        return null;
    }

    /**
     * Create a range attack hit box.
     * @return the range attack hit box
     */
    @Override
    public AttackEffect rangeAttack() {
        if (attackEnable) {
            final int hitBoxWidth = 70;
            final int hitBoxHeight = 50;
            final int effectYOffset = 10;
            final double forwardHitRange = 140;
            final double backwardHitRange = -140;
            attackEnable = false;
            rangeAnimation.play();
            double effectY = this.getBoundsInParent().getMinY();
            double effectX = this.getBoundsInParent().getMinX();
            if (direction == Direction.FORWARD) {
                effectX = this.getBoundsInParent().getMaxX() - hitBoxWidth;
            }
            double hitRange = forwardHitRange;
            if (direction == Direction.BACKWARD) {
                hitRange = backwardHitRange;
            }
            return new RangeEffect(effectX, effectY + effectYOffset, hitBoxWidth, hitBoxHeight,
                    "FireBall", hitRange, this.direction);
        }
        return null;
    }

    /**
     * Gets the attack point of the Player.
     * @return the attack point of the Player
     */
    @Override
    public int getAttackDamage() {
        return 0;
    }

    /**
     * Gets the health of the Player after taking hit.
     * @return the health of the Player
     */
    @Override
    public int takeDamage(final int point) {
        return 0;
    }

    /**
     * Shows the Player getting hurt.
     */
    @Override
    public void getHurt() {

    }

    /**
     * Perform the Player's death.
     * @return CompletableFuture true if the Player is dead, false otherwise
     */
    @Override
    public CompletableFuture<Boolean> startDying() {
        return null;
    }


}
