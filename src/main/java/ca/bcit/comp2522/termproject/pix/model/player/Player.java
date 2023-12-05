package ca.bcit.comp2522.termproject.pix.model.player;

import ca.bcit.comp2522.termproject.pix.MainApplication;
import ca.bcit.comp2522.termproject.pix.model.AttackEffect.AttackEffect;
import ca.bcit.comp2522.termproject.pix.model.AttackEffect.MeleeEffect;
import ca.bcit.comp2522.termproject.pix.model.AttackEffect.RangeEffect;
import ca.bcit.comp2522.termproject.pix.model.AttackEffect.TeleportEffect;
import ca.bcit.comp2522.termproject.pix.model.Combative;
import ca.bcit.comp2522.termproject.pix.model.Damageable;
import ca.bcit.comp2522.termproject.pix.model.GameObject;
import ca.bcit.comp2522.termproject.pix.model.Movable;
import ca.bcit.comp2522.termproject.pix.model.ObjectType;
import ca.bcit.comp2522.termproject.pix.model.weapon.Weapon;
import ca.bcit.comp2522.termproject.pix.model.weapon.WeaponType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
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
    private static final int MAX_HEALTH_POINTS = 20;
    private static final double WALK_SPEED = 5;
    private static final double RUN_SPEED = 10;
    private int healthPoint;
    private boolean jumpEnable;
    private boolean attackEnable;
    private boolean damageEnable;
    private boolean climbEnable;
    private boolean turnOffGravity;
    private AttackEffect meleeHitBox;
    private AttackEffect rangeHitBox;
    private double speed;
    private String currentImagePath;
    private Action action;
    private Direction direction;
    private Point2D velocity;
    private int healthPotionCounter;
    private int energyCounter;
    private Timeline meleeAnimation;
    private Timeline punchAnimation;
    private Timeline rangeAnimation;
    private Timeline walkAnimation;
    private Timeline jumpAnimation;
    private Timeline climbAnimation;
    private final Weapon[] weaponArray = new Weapon[2];
    private Timeline hurtAnimation;
    private Direction climbDirection;

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
        this.jumpEnable = true;
        this.attackEnable = true;
        this.damageEnable = true;
        this.climbEnable = false;
        this.meleeHitBox = null;
        this.rangeHitBox = null;
        this.climbDirection = Direction.FORWARD;
        this.currentImagePath = String.format("player/%s", direction.name());
        this.initializeMeleeAttackingAnimation();
        this.initializeRangeAttackingAnimation();
        this.initializeWalkingAnimation();
        this.initializeJumpingAnimation();
        this.initializeHurtingAnimation();
        this.initializePunchAttackingAnimation();
        this.initializeClimbingAnimation();
        this.healthPoint = MAX_HEALTH_POINTS;
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
        final int jumpFrameCount = 10;
        jumpAnimation = new Timeline(
                new KeyFrame(Duration.millis(jumpDuration), event -> {
                    this.action = Action.JUMPING;
                    this.currentImagePath = String.format("player/%s", direction.name());
                    this.updatePlayerImage(String.format("%s/Jumping_%d.png", currentImagePath, jumpFrame[0]));
                    jumpFrame[0] = (jumpFrame[0] + 1) % (jumpFrameCount);
                })
        );
        jumpAnimation.setCycleCount(jumpFrameCount);
        jumpAnimation.setOnFinished(event -> {
            this.setIdle();
            jumpFrame[0] = 0;
        });
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
     * Initializes the jumping animation.
     */
    private void initializeClimbingAnimation() {
        final int[] climbFrame = {0};
        final int climbDuration = 100;
        final int climbFrameCount = 6;
        climbAnimation = new Timeline(
                new KeyFrame(Duration.millis(climbDuration), event -> {
                    this.action = Action.JUMPING;
                    this.currentImagePath = String.format("player/%s", direction.name());
                    this.updatePlayerImage(String.format("%s/Climb_%d.png", currentImagePath, climbFrame[0]));
                    climbFrame[0] = (climbFrame[0] + 1) % (climbFrameCount);
                })
        );
        climbAnimation.setCycleCount(climbFrameCount);
        climbAnimation.setOnFinished(event -> {
            this.setIdle();
            climbFrame[0] = 0;
        });
    }

    /*
     * Initializes the range attacking animation.
     */
    private void initializeRangeAttackingAnimation() {
        final int[] rangeFrame = {0};
        final int rangeAttackDuration = 100;
        final int rangeAttackFrameCount = 8;
        rangeAnimation = new Timeline(
                new KeyFrame(Duration.millis(rangeAttackDuration), event -> {
                    this.action = Action.RANGE_ATTACK;
                    this.currentImagePath = String.format("player/%s", direction.name());
                    this.updatePlayerImage(String.format("%s/range_attack_%d.png", currentImagePath, rangeFrame[0]));
                    rangeFrame[0] = (rangeFrame[0] + 1) % (rangeAttackFrameCount);
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
        final int meleeAttackFrameCount = 5;
        meleeAnimation = new Timeline(
                new KeyFrame(Duration.millis(meleeAttackDuration), event -> {
                    this.action = Action.MELEE_ATTACK;
                    this.currentImagePath = String.format("player/%s", direction.name());
                    this.updatePlayerImage(String.format("%s/melee_attack_%d.png", currentImagePath, meleeFrame[0]));
                    meleeFrame[0] = (meleeFrame[0] + 1) % (meleeAttackFrameCount);
                })
        );
        meleeAnimation.setCycleCount(meleeAttackFrameCount);
        meleeAnimation.setOnFinished(event -> this.setIdle());
    }

    /*
     * Initializes the punch attacking animation.
     */
    private void initializePunchAttackingAnimation() {
        final int[] punchFrame = {0};
        final int punchAttackDuration = 100;
        final int punchAttackFrameCount = 7;
        punchAnimation = new Timeline(
                new KeyFrame(Duration.millis(punchAttackDuration), event -> {
                    this.action = Action.MELEE_ATTACK;
                    this.currentImagePath = String.format("player/%s", direction.name());
                    this.updatePlayerImage(String.format("%s/punch_%d.png", currentImagePath, punchFrame[0]));
                    punchFrame[0] = (punchFrame[0] + 1) % (punchAttackFrameCount);
                })
        );
        punchAnimation.setCycleCount(punchAttackFrameCount);
        punchAnimation.setOnFinished(event -> this.setIdle());
    }

    /*
     * Initializes the melee attacking animation.
     */
    private void initializeHurtingAnimation() {
        final int[] hurtFrame = {0};
        final int hurtDuration = 80;
        final int hurtFrameCount = 7;
        hurtAnimation = new Timeline(
                new KeyFrame(Duration.millis(hurtDuration), event -> {
                    this.action = Action.HURTING;
                    this.setOpacity(hurtFrame[0] % 2);
                    this.currentImagePath = String.format("player/%s", direction.name());
                    this.updatePlayerImage(String.format("%s/hurting_%d.png", currentImagePath, hurtFrame[0]));
                    hurtFrame[0] = (hurtFrame[0] + 1) % (hurtFrameCount);
                })
        );
        hurtAnimation.setCycleCount(hurtFrameCount);
        hurtAnimation.setOnFinished(event -> {
            this.setIdle();
            this.setOpacity(1);
        });
    }

    /**
     * Moves the Player in the y direction by 1 pixel.
     * @param climbUp true if climbing up, false if climbing down
     */
    public void climb(final boolean climbUp) {
        final int climbPerFrame;
        if (climbUp) {
            climbPerFrame = -1;
        } else {
            climbPerFrame = 1;
        }
        if (climbEnable) {
            this.setTranslateY(this.getTranslateY() + climbPerFrame);
            this.direction = this.climbDirection;
            turnOffGravity = true;
            attackEnable = false;
            jumpAnimation.stop();
            walkAnimation.stop();
            climbAnimation.play();
        }
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
        final double offset = 0.8;
        this.setTranslateY(this.getTranslateY() - offset);
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
        final double jumpingPixel = 1.0;
        final double fallingPixel = 0.8;
        if (movingDown) {
            if (!turnOffGravity) {
                this.setTranslateY(this.getTranslateY() + fallingPixel);
            }
        } else {
            this.currentImagePath = String.format("player/%s", direction.name());
            walkAnimation.stop();
            climbAnimation.stop();
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
        if (movingRight) {
            this.direction = Direction.FORWARD;
            this.setTranslateX(this.getTranslateX() + 1);
        } else {
            this.direction = Direction.BACKWARD;
            this.setTranslateX(this.getTranslateX() - 1);
        }
        jumpAnimation.stop();
        if (!(meleeAnimation.getStatus() == Animation.Status.RUNNING
                || rangeAnimation.getStatus() == Animation.Status.RUNNING
                || punchAnimation.getStatus() == Animation.Status.RUNNING)) {
            walkAnimation.play();
        }
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
            if (walkAnimation.getStatus() == Animation.Status.RUNNING) {
                this.updatePlayerImage(String.format("%s/idle.png", currentImagePath));
            }
            this.action = Action.IDLE;
            walkAnimation.stop();
            jumpAnimation.stop();
            climbAnimation.stop();
            this.attackEnable = true;
        }

    }

    /**
     * Add a weapon to the Player's weapons array.
     *
     * @param weapon the weapon to add
     */
    public void addWeapon(final Weapon weapon) {
        if (weapon.getWeaponType() == WeaponType.MELEE_WEAPON) {
            this.weaponArray[0] = weapon;
        } else {
            this.weaponArray[1] = weapon;
        }
        System.out.println("Added: " + weapon);
    }

    /**
     * Gets the weapon from the Player's weapons array.
     *
     * @param weaponType the weapon type to get
     * @return the weapon from the Player's weapons array if it exists, or null if not
     */
    public Weapon getWeapon(final WeaponType weaponType) {
        if (weaponType == WeaponType.MELEE_WEAPON && this.weaponArray[0] != null
                && this.weaponArray[0].weaponIsAvailable()) {
            return this.weaponArray[0];
        } else if (weaponType == WeaponType.RANGE_WEAPON && this.weaponArray[1] != null
                && this.weaponArray[1].weaponIsAvailable()) {
            return this.weaponArray[1];
        } else {
            return null;
        }
    }

    /**
     * Increments the player's health potion counter by one.
     */
    public void incrementHealthPotionCounter() {
        this.healthPotionCounter++;
    }


    /**
     * Gets the player's health potion counter.
     * @return the player's health potion counter
     */
    public int getHealthPotionCounter() {
        return this.healthPotionCounter;
    }

    /**
     * Increments the player's energy counter by one.
     */
    public void incrementEnergyCounter() {
        this.energyCounter++;
    }

    /**
     * Decrements the player's energy counter by one.
     */
    public void decrementEnergyCounter() {
        this.energyCounter--;
    }


    /**
     * Gets the player's energy counter.
     *
     * @return the player's energy counter
     */
    public int getEnergyCounter() {
        return this.energyCounter;
    }

    /**
     * Gets the player's teleport effect.
     *
     * @return the player's teleport effect
     */
    public TeleportEffect teleport() {
        final int teleportDuration = 250;
        final int teleportOffset = 75;
        final int teleportDimension = 200;
        this.setVisible(false);
        PauseTransition pause = new PauseTransition(Duration.millis(teleportDuration));
        pause.setOnFinished(event -> this.setVisible(true));
        pause.play();
        return new TeleportEffect(this.getMinX() - teleportOffset,
                this.getMinY() - teleportOffset, teleportDimension, teleportDimension, "Teleport");
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
            double effectY = this.getBoundsInParent().getMinY();
            double effectX = this.getBoundsInParent().getMinX() - hitBoxWidth;
            if (direction == Direction.FORWARD) {
                effectX = this.getBoundsInParent().getMaxX();
            }
            attackEnable = false;
            walkAnimation.stop();
            jumpAnimation.stop();
            if (this.weaponArray[0] != null) {
                meleeAnimation.play();
                this.meleeHitBox = new MeleeEffect(effectX, effectY + effectYOffset, hitBoxWidth, hitBoxHeight,
                        "Explosion");
            } else {
                punchAnimation.play();
                this.meleeHitBox = new MeleeEffect(effectX, effectY + effectYOffset, hitBoxWidth, hitBoxHeight,
                        "Punch");
            }

            return this.meleeHitBox;
        }
        return null;
    }

    /**
     * Create a range attack hit box.
     * @return the range attack hit box
     */
    @Override
    public AttackEffect rangeAttack() {
        if (attackEnable & this.weaponArray[1] != null) {
            final int hitBoxWidth = 70;
            final int hitBoxHeight = 50;
            final int effectYOffset = 10;
            final double forwardHitRange = 140;
            final double backwardHitRange = -140;
            attackEnable = false;
            walkAnimation.stop();
            jumpAnimation.stop();
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
            this.rangeHitBox = new RangeEffect(effectX, effectY + effectYOffset, hitBoxWidth, hitBoxHeight,
                    "FireBall", hitRange, this.direction);
            return this.rangeHitBox;
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
        if (damageEnable) {
            healthPoint -= point;
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event -> damageEnable = true);
            pause.play();
        }
        return healthPoint;
    }

    /**
     * Get Range Hit Box.
     * @return the range hit box
     */
    public AttackEffect getRangeHitBox() {
        return this.rangeHitBox;
    }

    /**
     * Set the melee hit box to null.
     */
    public void vanishMeleeHitBox() {
        this.meleeHitBox = null;
    }

    /**
     * Set the range hit box to null.
     */
    public void vanishRangeHitBox() {
        this.rangeHitBox = null;
    }

    /**
     * Checks if the Player has no hit box.
     * @return true if the Player has no hit box, false otherwise
     */
    public boolean noHitBox() {
        return !(this.meleeHitBox != null || this.rangeHitBox != null);
    }

    /**
     * Shows the Player getting hurt.
     */
    @Override
    public void getHurt() {
        if (damageEnable) {
            this.attackEnable = false;
            damageEnable = false;
            this.hurtAnimation.play();
            this.action = Action.HURTING;
        }
    }

    /**
     * Perform the Player's death.
     * @return CompletableFuture true if the Player is dead, false otherwise
     */
    @Override
    public CompletableFuture<Boolean> startDying() {
        return null;
    }

    /**
     * Use a health potion.
     */
    public void useHealthPotion() {
        if (healthPotionCounter > 0 && healthPoint < MAX_HEALTH_POINTS) {
            this.healthPotionCounter--;
            this.healthPoint++;
        }
    }

    /**
     * Use a weapon.
     *
     * @param weaponType the type of weapon to be used as a WeaponType
     */
    public void useWeapon(final WeaponType weaponType) {
        if (weaponType == WeaponType.MELEE_WEAPON) {
            if (this.weaponArray[0] != null && this.weaponArray[0].weaponIsAvailable()) {
                this.weaponArray[0].useWeapon();
            }
        } else {
            if (this.weaponArray[1] != null && this.weaponArray[1].weaponIsAvailable()) {
                this.weaponArray[1].useWeapon();
            }
        }
    }

    /**
     * Gets the damage from a weapon.
     *
     * @param weaponType the weapon type to use as a WeaponType
     * @return the resulting damage as an int
     */
    public int getWeaponDamage(final WeaponType weaponType) {
        if (weaponType == WeaponType.MELEE_WEAPON) {
            if (this.weaponArray[0] == null || !this.weaponArray[0].weaponIsAvailable()) {
                return 1;
            }
            return this.weaponArray[0].getWeaponDamage();
        } else {
            if (this.weaponArray[1] == null || !this.weaponArray[1].weaponIsAvailable()) {
                return 0;
            }
            return this.weaponArray[1].getWeaponDamage();
        }
    }

    /**
     * Checks if the Player is next to a ladder.
     * @return true if the Player is next to a ladder, false otherwise
     */
    public boolean isNextToLadder() {
        return this.climbEnable;
    }

    /**
     * Sets the Player to be next to a ladder.
     * @param nextToLadder true if the Player is next to a ladder, false otherwise
     */
    public void setNextToLadder(final boolean nextToLadder) {
        this.climbEnable = nextToLadder;
        if (!climbEnable & this.turnOffGravity) {
            this.turnOffGravity = false;
        }
    }

    /**
     * Gets the direction of the Player.
     * @param climbingDirection the direction of the Player
     */
    public void setClimbDirection(final Direction climbingDirection) {
        this.climbDirection = climbingDirection;
    }

    /**
     * Gets the health point of the Player.
     * @return the health point of the Player
     */
    public int getHealthPoint() {
        return healthPoint;
    }

    /**
     * Gets the max health point of the Player.
     * @return the max health point of the Player
     */
    public int getMaxHealthPoints() {
        return MAX_HEALTH_POINTS;
    }
}
