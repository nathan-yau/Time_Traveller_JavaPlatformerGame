package ca.bcit.comp2522.termproject.pix.gamecontroller;

import ca.bcit.comp2522.termproject.pix.GameType;
import ca.bcit.comp2522.termproject.pix.MainApplication;
import ca.bcit.comp2522.termproject.pix.model.AttackEffect.AttackEffect;
import ca.bcit.comp2522.termproject.pix.model.AttackEffect.TeleportEffect;
import ca.bcit.comp2522.termproject.pix.model.Enemy.Enemy;
import ca.bcit.comp2522.termproject.pix.model.GameObject;
import ca.bcit.comp2522.termproject.pix.model.block.BlockType;
import ca.bcit.comp2522.termproject.pix.model.block.StandardBlock;
import ca.bcit.comp2522.termproject.pix.model.bossfight.BossProjectileGenerator;
import ca.bcit.comp2522.termproject.pix.model.bossfight.BossWeapon;
import ca.bcit.comp2522.termproject.pix.model.bossfight.BossWeaponType;
import ca.bcit.comp2522.termproject.pix.model.bossfight.Hal;
import ca.bcit.comp2522.termproject.pix.model.levelmanager.LevelManager;
import ca.bcit.comp2522.termproject.pix.model.pickupitem.PickUpItem;
import ca.bcit.comp2522.termproject.pix.model.pickupitem.PickUpItemType;
import ca.bcit.comp2522.termproject.pix.model.platformgenerator.PlatformManager;
import ca.bcit.comp2522.termproject.pix.model.player.Direction;
import ca.bcit.comp2522.termproject.pix.model.player.Player;
import ca.bcit.comp2522.termproject.pix.model.uimanager.UIManager;
import ca.bcit.comp2522.termproject.pix.model.weapon.MeleeWeapon;
import ca.bcit.comp2522.termproject.pix.model.weapon.RangeWeapon;
import ca.bcit.comp2522.termproject.pix.model.weapon.Weapon;
import ca.bcit.comp2522.termproject.pix.model.weapon.WeaponType;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represents the main game loop.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public class GameController {
    private static final int X_CAMERA_THRESHOLD = 300;
    private static final int X_CAMERA_ADJUSTMENT = 300;
    private static ChangeListener<? super Number> playerXListener;
    private final int windowWidth;
    private final int windowHeight;
    private final Pane appRoot;
    private final Pane gameRoot;
    private final Pane uiRoot;
    private final PlatformManager platform;
    private final Player player;
    private final HashMap<KeyCode, Boolean> keyboardChecker;
    private final ArrayList<StandardBlock> cachedBlockArray;
    private double lastCacheXPosition;
    private double lastCacheYPosition;
    private final CollisionDetector collisionDetector;
    private final BlockInteraction blockInteraction;
    private final ItemInteraction itemInteraction;
    private final EnemyInteraction enemyInteraction;
    private final DoubleProperty playerX;
    private BossFight activeBossFight;
    private boolean rangeTargetHit;
    private final LevelManager levelManager;
    private final UIManager uiManager;
    private boolean switching = false;

    /**
     * Constructs a GameController object with default values.
     * Set up the initial platform and player.
     *
     * @param windowWidth the width of the window as an int
     * @param windowHeight the height of the window as an int
     */
    public GameController(final int windowWidth, final int windowHeight) throws IOException {
        final double initialPlayerX = 0;
        final double initialPlayerY = 500;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.appRoot = new Pane();
        this.gameRoot = new Pane();
        this.uiRoot = new Pane();
        this.levelManager = new LevelManager();
        this.platform = new PlatformManager(levelManager);
        this.keyboardChecker = new HashMap<>();
        this.player = new Player(initialPlayerX, initialPlayerY, "Player/idle.png");
        this.uiManager = new UIManager(player.getMaxHealthPoints());
        this.cachedBlockArray = new ArrayList<>();
        this.collisionDetector = new CollisionDetector();
        this.blockInteraction = new BlockInteraction();
        this.itemInteraction = new ItemInteraction();
        this.enemyInteraction = new EnemyInteraction();
        this.lastCacheXPosition = initialPlayerX;
        this.lastCacheYPosition = initialPlayerY;
        this.rangeTargetHit = false;
        this.playerX = player.translateXProperty();
        this.setBackground(this.getLevelBackground());
        this.platform.createGamePlatform();
        this.setUpPlatform();
        this.setUpCamera();
        this.setCachedBlockArray();
        gameRoot.getChildren().add(player);
        uiRoot.getChildren().add(uiManager.getBatteryCounter());
        uiRoot.getChildren().add(uiManager.getWorldName());
        uiRoot.getChildren().add(uiManager.getPlayerStatus());
        uiRoot.getChildren().add(uiManager.getBackpack());
    }

    /**
     * Sets up the platform using the PlatformManager.
     */
    private void setUpPlatform() {
        for (StandardBlock block: platform.getBlockArray()) {
            gameRoot.getChildren().add(block);
        }

        for (PickUpItem item: platform.getItemArray()) {
            gameRoot.getChildren().add(item);
        }

        for (Enemy enemy: platform.getEnemyArray()) {
            gameRoot.getChildren().add(enemy);
        }
    }



    /**
     * Sets up the camera.
     */
    private void setUpCamera() {
        // Adjust the camera horizontal position based on player's location
        playerXListener = (obs, old, newValue) -> {
            int offset = newValue.intValue();

            if (offset > X_CAMERA_THRESHOLD && offset < platform.getTotalLevelWidth() - X_CAMERA_THRESHOLD) {
                gameRoot.setLayoutX(-(offset - X_CAMERA_ADJUSTMENT));
            }
        };

        playerX.addListener(playerXListener);
    }

    private void disableCamera() {
        playerX.removeListener(playerXListener);
    }

    /**
     * Sets the background of the game.
     * @param backgroundImagePath the background image url
     */
    private void setBackground(final String backgroundImagePath) {
        final double backgroundWidth = 100;
        final double backgroundHeight = 100;
        Image backgroundImage = new Image(String.valueOf(MainApplication.class.getResource(backgroundImagePath)));
        BackgroundImage bgImage = new BackgroundImage(backgroundImage,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(backgroundWidth, backgroundHeight, true, true, true, true));
        Background bg = new Background(bgImage);
        appRoot.setBackground(bg);
    }

    private void fadeTransitionToLevels() {
        final int fadeDuration = 150;
        final double fadeOpacityMin = 0.75;
        final double fadeOpacityMax = 1.0;
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(fadeDuration), appRoot);
        fadeTransition.setFromValue(fadeOpacityMax);
        fadeTransition.setToValue(fadeOpacityMin);
        fadeTransition.setCycleCount(2);
        fadeTransition.setAutoReverse(true);
        fadeTransition.setOnFinished(event -> fadeTransition.stop());
        fadeTransition.play();
    }

    /**
     * Sets the cached block array.
     */
    private void setCachedBlockArray() {
        final int cacheThreshold = 500;
        cachedBlockArray.clear();
        for (StandardBlock block : platform.getBlockArray()) {
            if (player.getCenterX() + cacheThreshold >= block.getMinX()
                    && player.getCenterX() - cacheThreshold <= block.getMaxX()) {
                if (player.getCenterY() + cacheThreshold >= block.getMinY()
                        && player.getCenterY() - cacheThreshold <= block.getMaxY()) {
                    cachedBlockArray.add(block);
                }
            }
        }
        lastCacheXPosition = player.getCenterX();
        lastCacheYPosition = player.getCenterY();
    }


    /**
     * Resets the cached block array.
     */
    private void resetCachedBlockArray() {
        final int resetThreshold = 400;
        if (player.getCenterX() > lastCacheXPosition + resetThreshold
                || player.getCenterX() < lastCacheXPosition - resetThreshold
                || player.getCenterY() > lastCacheYPosition + resetThreshold
                || player.getCenterY() < lastCacheYPosition - resetThreshold) {
            setCachedBlockArray();
        }
    }

    /**
     * Switches the level.
     * @param dimension the dimension to switch to
     */
    private void switchLevel(final int dimension) {
        gameRoot.getChildren().clear();
        cachedBlockArray.clear();
        platform.setNextLevelArrays(dimension);
        this.setUpPlatform();
        setCachedBlockArray();
        TeleportEffect teleportEffect = player.teleport();
        gameRoot.getChildren().add(player);
        gameRoot.getChildren().add(teleportEffect);
        this.setBackground(this.getLevelBackground());
        this.fadeTransitionToLevels();
        teleportEffect.startInitialEffect().thenAccept(isDone -> {
            if (isDone) {
                gameRoot.getChildren().remove(teleportEffect);
                switching = false;
            }
        });
    }

    /**
     * Represents the collision detector.
     */
    private static final class CollisionDetector {

        /**
         * Constructs a CollisionDetector.
         */
        CollisionDetector() { }

        /**
         * Checks if the two objects are intersecting.
         * @param firstGameObject the first game object
         * @param secondGameObject the second game object
         * @return true if the player is colliding with the platform, false otherwise
         */
        private boolean objectIntersect(final GameObject<? extends GameType> firstGameObject,
                                        final GameObject<? extends GameType> secondGameObject) {
            return firstGameObject.checkIntersect(secondGameObject.getBoundsInParent());
        }

        /**
         * Checks if the two objects are colliding on the x-axis.
         * @param firstGameObject the first game object
         * @param secondGameObject the second game object
         * @param rightSide whether the collision is on the right side
         * @return true if the player is colliding with the platform, false otherwise
         */
        public boolean collidingDetectorX(final GameObject<? extends GameType> firstGameObject,
                                          final GameObject<? extends GameType> secondGameObject,
                                          final boolean rightSide) {
            final int xTolerance = 40;
            double distance = Math.abs(firstGameObject.getMinX() - secondGameObject.getMaxX());
            if (rightSide) {
                distance = Math.abs(firstGameObject.getMaxX() - secondGameObject.getMinX());
            }
            return distance <= xTolerance;
        }

        /**
         * Checks if the two objects are colliding on the y-axis.
         * @param firstGameObject the first game object
         * @param secondGameObject the second game object
         * @param upSide whether the collision is from the top
         * @return true if the player is colliding with the platform, false otherwise
         */
        private boolean collidingDetectorY(final GameObject<? extends GameType> firstGameObject,
                                           final GameObject<? extends GameType> secondGameObject,
                                           final boolean upSide) {
            final int yTolerance = 20;
            double distance = Math.abs(firstGameObject.getMinY() - secondGameObject.getMaxY());
            if (upSide) {
                distance = Math.abs(firstGameObject.getMaxY() - secondGameObject.getMinY());
            }
            return distance <= yTolerance;
        }

        /**
         * Checks if the two objects are on the same x-axis.
         * @param firstGameObject the first game object
         * @param secondGameObject the second game object
         * @return true if the player is colliding with the platform, false otherwise
         */
        private boolean onSameXAxis(final GameObject<? extends GameType> firstGameObject,
                                    final GameObject<? extends GameType> secondGameObject) {
            final int edgeOffset = 10;
            // Check if the first object is inside the second object
            boolean firstObjectInside = secondGameObject.getMinX() <= firstGameObject.getMinX()
                    && secondGameObject.getMaxX() >= firstGameObject.getMaxX();
            // Check if the first object is on the left edge of the second object
            boolean firstObjectOnLeft = secondGameObject.getMinX() >= firstGameObject.getMinX()
                    && firstGameObject.getMaxX() >= secondGameObject.getMinX() + edgeOffset;
            // Check if the first object is on the right edge of the second object
            boolean firstObjectOnRight = firstGameObject.getMaxX() >= secondGameObject.getMaxX()
                    && firstGameObject.getMinX() <= secondGameObject.getMaxX() - edgeOffset;
            return firstObjectInside || firstObjectOnLeft || firstObjectOnRight;
        }

        /**
         * Calculates the collision percentage of the two objects.
         * @param firstGameObject the first game object
         * @param secondGameObject the second game object
         * @return the collision percentage as double
         */
        public double calculateCollisionPercentage(final GameObject<? extends GameType>  firstGameObject,
                                                          final GameObject<? extends GameType>  secondGameObject) {

            final int divisor = 100;
            // Calculate overlapping area
            double overlapWidth = Math.max(0, Math.min(firstGameObject.getMaxX(), secondGameObject.getMaxX())
                    - Math.max(firstGameObject.getMinX(), secondGameObject.getMinX()));
            double overlapHeight = Math.max(0, Math.min(firstGameObject.getMaxY(), secondGameObject.getMaxY())
                    - Math.max(firstGameObject.getMinY(), secondGameObject.getMinY()));
            double overlappingArea = overlapWidth * overlapHeight;

            // Calculate total area of either object
            double totalArea = firstGameObject.getWidth() * firstGameObject.getHeight();

            // Calculate collision percentage
            return (overlappingArea / totalArea) * divisor;
        }
    }

    /**
     * Represents the block interaction.
     */
    private final class BlockInteraction {
        BlockInteraction() { }

        /*
         * Interacts with the blocks on the x-axis.
         * @param movingRight true if moving forward, false if moving backward
         */
        private void interactWithBlocksX(final boolean movingRight) {
            double movementDelta = player.getSpeed();
            if (!movingRight) {
                movementDelta = -movementDelta;
            }
            for (int i = 0; i < Math.abs(movementDelta); i++) {
                for (StandardBlock block : cachedBlockArray) {
                    if (block.getSubtype() == BlockType.LADDERS) {
                        continue;
                    }
                    if (collisionDetector.objectIntersect(player, block)) {
                        if (collisionDetector.collidingDetectorX(player, block, movingRight)) {
                            return;
                        }
                    }
                }
                player.moveX(movingRight);
            }
        }

        /*
         * Interacts with the blocks on the y-axis.
         */
        private void interactWithBlocksY() {
            final double vectorY = player.getVelocityY();
            final boolean movingDown = vectorY > 0;
            boolean onLadder = false;
            for (int i = 0; i < Math.abs(vectorY); i++) {
                for (StandardBlock block : cachedBlockArray) {
                    if (collisionDetector.objectIntersect(player, block)) {
                        if (block.getSubtype() == BlockType.LADDERS) {
                            onLadder = interactWithLadders(block, onLadder);
                            continue;
                        }
                        if (collisionDetector.collidingDetectorY(player, block, movingDown)
                                && collisionDetector.onSameXAxis(player, block)) {
                            if (movingDown) {
                                player.offsetGravity();
                            }
                            if (block.getSubtype() == BlockType.DISAPPEARING_BLOCK) {
                                interactWithDisappearingBlocks(block);
                            }
                            return;
                        }
                    }
                }
                if (!onLadder) {
                    player.setNextToLadder(false);
                }
                player.moveY(movingDown);
            }
        }

        /*
         * Removes the block from the game.
         * @param block the block to remove
         */
        private void blockRemoval(final StandardBlock block) {
            platform.getBlockArray().removeIf(block::equals);
            gameRoot.getChildren().removeIf(block::equals);
            cachedBlockArray.removeIf(block::equals);
        }

        /*
         * Interacts with the ladders.
         * @param block the ladder block
         * @param onLadder whether the player is on the ladder
         * @return true if the player is on the ladder, false otherwise
         */
        private boolean interactWithLadders(final StandardBlock block, final boolean onLadder) {
            final double ladderCollisionPercentage = 30;
            if (collisionDetector.onSameXAxis(player, block)
                    && collisionDetector.calculateCollisionPercentage(player, block)
                    > ladderCollisionPercentage) {
                player.setNextToLadder(true);
                if (player.getCenterX() > block.getCenterX()) {
                    player.setClimbDirection(Direction.BACKWARD);
                } else {
                    player.setClimbDirection(Direction.FORWARD);
                }
                return true;
            }
            return onLadder;
        }

        /*
         * Interacts with the disappearing blocks.
         * @param block the disappearing block
         */
        private void interactWithDisappearingBlocks(final StandardBlock block) {
            block.animate().thenAccept(isDone -> {
                if (isDone) {
                    this.blockRemoval(block);
                }
            });
        }
    }

    // Handle interactions with pickup items.
    private final class ItemInteraction {

        /**
         * Constructs an ItemInteraction object.
         */
        ItemInteraction() { }

        // Check and handle collision with items
        private void interactWithItems() throws IOException {
            final double itemCollisionPercentage = 40;
            Iterator<PickUpItem> iterator = platform.getItemArray().iterator();

            while (iterator.hasNext()) {
                PickUpItem item = iterator.next();

                if (collisionDetector.objectIntersect(player, item)
                        && collisionDetector.calculateCollisionPercentage(player, item) > itemCollisionPercentage) {
                    if (item.getSubtype() == PickUpItemType.HEALTH_POTION) {
                        player.incrementHealthPotionCounter();
                    } else if (item.getSubtype() == PickUpItemType.ENERGY) {
                        player.incrementEnergyCounter();
                        uiManager.refreshBatteryCounter(player.getEnergyCounter());
                    } else if (item.getSubtype() == PickUpItemType.MELEE_WEAPON) {
                        Weapon meleeWeapon = new MeleeWeapon(platform.getCurrentLevel());
                        player.addWeapon(meleeWeapon);
                    } else if (item.getSubtype() == PickUpItemType.RANGE_WEAPON) {
                        Weapon rangeWeapon = new RangeWeapon(platform.getCurrentLevel());
                        player.addWeapon(rangeWeapon);
                    }
                    if (item.onPickup()) {
                        iterator.remove();
                    }
                }
            }
        }
    }

    // Handle interactions with pickup items.
    private final class EnemyInteraction {
        final double hitBoxCollisionPercentage = 50;
        ArrayList<Enemy> enemiesToRemove = new ArrayList<>();

        /**
         * Constructs an EnemyInteraction object.
         */
        EnemyInteraction() { }


        /*
         * Interacts with the enemies with melee weapon.
         * @param hitBox the attack hit box
         */
        private void meleeWithEnemies(final AttackEffect hitBox) {
            AtomicBoolean found = new AtomicBoolean(false);
            hitBox.startInitialEffect().thenAccept(isDone -> {
                if (isDone && !found.get()) {
                    gameRoot.getChildren().remove(hitBox);
                    player.vanishMeleeHitBox();
                }
            });
            platform.getEnemyArray().removeIf(enemy -> {
                if (collisionDetector.objectIntersect(hitBox, enemy)
                        && collisionDetector.calculateCollisionPercentage(hitBox, enemy) > hitBoxCollisionPercentage) {
                    found.set(true);
                    hitBox.stopInitialEffect();
                    hitBox.startOnHitEffect().thenAccept(isDone -> {
                        gameRoot.getChildren().remove(hitBox);
                        player.vanishMeleeHitBox();
                    });
                    final int meleeDamage = player.getWeaponDamage(WeaponType.MELEE_WEAPON);
                    System.out.println("Melee damage: " + meleeDamage);
                    if (enemy.takeDamage(meleeDamage) <= 0) {
                        enemy.startDying().thenAccept(isCompleted -> gameRoot.getChildren().remove(enemy));
                        return true;
                    } else {
                        enemy.getHurt();
                    }

                }
                return false;
            });
        }


        /*
         * Interacts with the enemies with range weapon.
         * @param existingRangeHitBox the existing range hit box
         * @param enemy the enemy
         */
        private void rangeWithEnemies(final AttackEffect existingRangeHitBox, final Enemy enemy) {
            if (collisionDetector.objectIntersect(existingRangeHitBox, enemy) & enemy.getDamageEnable()) {
                final int rangeDamage = player.getWeaponDamage(WeaponType.RANGE_WEAPON);
                System.out.println("Range damage: " + rangeDamage);
                rangeCombat(existingRangeHitBox, enemy);
                if (enemy.takeDamage(rangeDamage) != 0) {
                    enemy.getHurt();
                } else {
                    enemiesToRemove.add(enemy);
                }
            }
        }

        /*
         * Interacts with the enemies.
         */
        private void interactWithEnemies() {
            final double enemyCollisionPercentage = 40;
            AttackEffect existingRangeHitBox = player.getRangeHitBox();
            for (Enemy enemy : platform.getEnemyArray()) {
                if (collisionDetector.objectIntersect(player, enemy)
                    && collisionDetector.calculateCollisionPercentage(player, enemy) > enemyCollisionPercentage) {
                        enemy.setDirection(Direction.FORWARD);
                        if (player.getCenterX() < enemy.getCenterX()) {
                            enemy.setDirection(Direction.BACKWARD);
                        }
                        enemy.meleeAttack();
                        player.getHurt();
                        uiManager.refreshHealthBar(player.getHealthPoint(), player.getMaxHealthPoints());
                        return;
                }
                if (existingRangeHitBox != null) {
                    this.rangeWithEnemies(existingRangeHitBox, enemy);
                }
            }
            enemiesRemoval();
        }

        /*
         * Removes the enemy from the game.
         */
        private void enemiesRemoval() {
            platform.getEnemyArray().removeIf(deadEnemy -> {
                if (enemiesToRemove.contains(deadEnemy)) {
                    deadEnemy.startDying().thenAccept(isCompleted -> gameRoot.getChildren().remove(deadEnemy));
                    return true;
                }
                return false;
            });
        }
    }

    // Handle ranged combat hit box and collision detection
    private void rangeCombat(final AttackEffect existingRangeHitBox, final Enemy enemy) {
        enemy.setDamageEnable(false);
        existingRangeHitBox.stopInitialEffect();
        rangeTargetHit = true;
        existingRangeHitBox.startOnHitEffect().thenAccept(isDone -> {
            gameRoot.getChildren().remove(existingRangeHitBox);
            player.vanishRangeHitBox();
            rangeTargetHit = false;
            enemy.setDamageEnable(true);
        });
    }

    /**
     * Inserts the keyboard listeners.
     */
    public void insertKeyboardListeners() {
        appRoot.getChildren().addAll(gameRoot, uiRoot);
        appRoot.setOnKeyPressed(event -> keyboardChecker.put(event.getCode(), true));
        appRoot.setOnKeyReleased(event -> keyboardChecker.put(event.getCode(), false));
        appRoot.requestFocus();
    }

    /**
     * Checks if the key is pressed.
     * @param key the key to check
     * @return true if the key is pressed, false otherwise
     */
    private boolean isPressed(final KeyCode key) {
        return keyboardChecker.getOrDefault(key, false);
    }

    /**
     * Checks if any key is pressed.
     * @return true if any key is pressed, false otherwise
     */
    private boolean isAnyKeyPressed() {
        final KeyCode[] keysToCheck = {KeyCode.W, KeyCode.A, KeyCode.S, KeyCode.D, KeyCode.I, KeyCode.O, KeyCode.P};
        for (KeyCode key : keysToCheck) {
            if (keyboardChecker.containsKey(key)) {
                Boolean isPressed = keyboardChecker.get(key);
                if (isPressed != null && isPressed) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Listens to the keyboard.
     * @throws IOException if the image is not found
     */
    private void keyboardListeners() throws IOException {
        this.movementKeyListeners();
        this.switchPlatformKeyListener();
        this.meleeAttackKeyListener();
        this.rangeAttackKeyListener();
        // Listen to potion use signal
        if (isPressed(KeyCode.H)) {
            player.useHealthPotion();
        }
        // Listen to idle signal
        if (!isAnyKeyPressed() & !player.isPlayerInAction()) {
            player.setIdle();
        }
    }
    /* Listen to movement signals */
    private void movementKeyListeners() {
        final int outOfBounds = 5;
        // Listen to jump signal and prevent for jumping out of the map
        // If the player is next to a ladder, climb instead of jump
        if (isPressed(KeyCode.W) && player.getTranslateY() >= outOfBounds) {
            if (player.isNextToLadder()) {
                player.climb(true);
            } else {
                player.setJumpSpeed();
            }
        }

        // Listen to climb down signal
        if (isPressed(KeyCode.S) && player.getMinY() >= outOfBounds) {
            if (player.isNextToLadder()) {
                player.climb(false);
            }
        }

        // Listen to running signal
        if (isPressed(KeyCode.I)) {
            player.run();
        }

        // Listen to walk signal
        if (!isPressed(KeyCode.I)) {
            player.walk();
        }

        // Listen to backward signal and prevent for running out of the map
        if (isPressed(KeyCode.A) && player.getTranslateX() >= outOfBounds) {
            blockInteraction.interactWithBlocksX(false);
        }

        // Listen to forward signal
        if (isPressed(KeyCode.D)  && player.getMaxX() <= platform.getTotalLevelWidth() - outOfBounds) {
            blockInteraction.interactWithBlocksX(true);
        }
    }
    /* Listen to attack signals */
    private void meleeAttackKeyListener() {
        // Listen to melee attack signal
        if (isPressed(KeyCode.O)) {
            AttackEffect hitBox;
            if (player.noHitBox()) {
                hitBox = player.meleeAttack();
                player.useWeapon(WeaponType.MELEE_WEAPON);
            } else {
                hitBox = null;
            }
            if (hitBox != null) {
                gameRoot.getChildren().add(hitBox);
                enemyInteraction.meleeWithEnemies(hitBox);
                if (activeBossFight != null) {
                    activeBossFight.meleeWithBoss(hitBox);
                }
            }
        }
    }

    /* Listen to attack signals */
    private void rangeAttackKeyListener() {
        if (isPressed(KeyCode.P)) {
            Weapon activeWeapon = player.getWeapon(WeaponType.RANGE_WEAPON);
            if (activeWeapon != null) {
                AttackEffect hitBox;
                if (player.noHitBox()) {
                    hitBox = player.rangeAttack();
                    player.useWeapon(WeaponType.RANGE_WEAPON);
                } else {
                    hitBox = null;
                }
                if (hitBox != null) {
                    gameRoot.getChildren().add(hitBox);
                    hitBox.startInitialEffect().thenAccept(isDone -> {
                        if (isDone && !rangeTargetHit) {
                            gameRoot.getChildren().remove(hitBox);
                            player.vanishRangeHitBox();
                        }
                    });
                }
            }
        }
    }

    /* Listen to switch platform signals */
    private void switchPlatformKeyListener() throws IOException {
        if (isPressed(KeyCode.L)) {
            if (!switching & player.getEnergyCounter() > 0) {
                levelManager.nextLevel();
                this.switchLevel(levelManager.getCurrentLevel());
                uiManager.refreshWorldName(levelManager.getCurrentLevel());
                player.decrementEnergyCounter();
                uiManager.refreshBatteryCounter(player.getEnergyCounter());
                switching = true;
            }
        }

        if (isPressed(KeyCode.K)) {
            if (!switching & player.getEnergyCounter() > 0) {
                levelManager.previousLevel();
                this.switchLevel(levelManager.getCurrentLevel());
                uiManager.refreshWorldName(levelManager.getCurrentLevel());
                player.decrementEnergyCounter();
                uiManager.refreshBatteryCounter(player.getEnergyCounter());
                switching = true;
            }
        }
    }

    /*
     * Checks if this level is a boss level.
     */
    private void checkForBossPresence() {
        final int[] bossLevels = {3};
        if (platform.getCurrentLevel() == bossLevels[0]) {
            final int numberOfProjectiles = 10;
            final int projectileWidth = 50;
            final int startDelay = 2;
            final int laserDuration = 3;
            final int endDuration = 2;

            if (activeBossFight == null || activeBossFight.bossLevel != bossLevels[0]) {
                Enemy hal = new Hal(windowWidth / 2, windowHeight);
                activeBossFight = new BossFight(bossLevels[0], hal);
            }
            activeBossFight.startBossFight(numberOfProjectiles,
                    projectileWidth, startDelay, laserDuration, endDuration);
        } else {
            if (activeBossFight != null) {
                activeBossFight.endBossFight();
                activeBossFight = null;
            }
        }
    }

    /**
     * Represents a boss fight object.
     */
    private class BossFight {
        private static final double HIT_BOX_COLLISION_PERCENTAGE = 50;
        private final ArrayList<BossWeapon> projectiles = new ArrayList<>();
        private final int bossLevel;
        private final Enemy activeBoss;
        private BossProjectileGenerator projectileGenerator;
        private Timeline projectileTimeline;

        /**
         * Constructs a BossFight object.
         *
         * @param bossLevel the boss level
         * @param boss the boss to fight
         */
        BossFight(final int bossLevel, final Enemy boss) {
            this.bossLevel = bossLevel;
            this.activeBoss = boss;
        }

        /**
         * Starts the boss fight.
         *
         * @param numberOfProjectiles the number of projectiles to be fired at once
         * @param projectileWidth the width of the projectile
         * @param startDelay the delay before the boss attack
         * @param laserDuration the duration of the laser
         * @param totalDuration the total duration of the boss attack
         */
        public void startBossFight(final int numberOfProjectiles, final int projectileWidth,
                                   final int startDelay, final int laserDuration, final int totalDuration) {
            disableCamera();

            if (!gameRoot.getChildren().contains(activeBoss)) {
                gameRoot.getChildren().add(activeBoss);
            }

            if (this.projectileGenerator == null) {
                this.projectileGenerator = new BossProjectileGenerator(numberOfProjectiles, windowWidth,
                        windowHeight, projectileWidth);
            }

            if (this.projectileTimeline == null) {
                this.projectileTimeline = createProjectileTimeline(startDelay, laserDuration, totalDuration);
                this.projectileTimeline.setCycleCount(Timeline.INDEFINITE);
                this.projectileTimeline.play();
            }
        }

        /*
         * Creates the timeline for the boss projectiles.
         *
         * @param startDelay delay before lasers are fired
         * @param laserDuration duration of the laser
         * @param endDelay delay after lasers are fired
         */
        private Timeline createProjectileTimeline(final int startDelay, final int laserDuration, final int endDelay) {
            final double projectileDamageDelay = 0.5;
            return new Timeline(
                    new KeyFrame(Duration.seconds(startDelay), event -> {
                        // Add projectiles to gameRoot
                        for (BossWeapon projectile : projectileGenerator.fireProjectiles()) {
                            if (projectile.getSubtype() == BossWeaponType.PROJECTILE) {
                                gameRoot.getChildren().add(projectile);
                            }
                        }
                    }),

                    new KeyFrame(Duration.seconds(projectileDamageDelay + startDelay), event -> {
                        // Add projectiles to the projectiles list after a delay
                        projectiles.addAll(projectileGenerator.getProjectiles());
                    }),

                    new KeyFrame(Duration.seconds(laserDuration + projectileDamageDelay + startDelay), event -> {
                        Iterator<BossWeapon> iterator = projectiles.iterator();
                        while (iterator.hasNext()) {
                            BossWeapon laser = iterator.next();
                            gameRoot.getChildren().remove(laser);
                            iterator.remove();
                        }
                    }),
                    new KeyFrame(Duration.seconds(laserDuration + projectileDamageDelay + startDelay + endDelay))
            );
        }

        /**
         * Ends the current boss fight.
         */
        public void endBossFight() {
            for (BossWeapon laser : projectiles) {
                gameRoot.getChildren().remove(laser);
            }
            this.projectileTimeline.stop();
            Iterator<BossWeapon> iterator = projectiles.iterator();
            while (iterator.hasNext()) {
                BossWeapon laser = iterator.next();
                gameRoot.getChildren().remove(laser);
                iterator.remove();
            }
            this.activeBoss.setDamageEnable(false);
            this.activeBoss.setVisible(false);
        }

        /*
         * Interacts with the boss with melee weapons.
         * @param hitBox the melee attack hit box
         */
        private void meleeWithBoss(final AttackEffect hitBox) {
            if (activeBoss.getDamageEnable()) {
                AtomicBoolean found = new AtomicBoolean(false);
                hitBox.startInitialEffect().thenAccept(isDone -> {
                    if (isDone && !found.get()) {
                        gameRoot.getChildren().remove(hitBox);
                        player.vanishMeleeHitBox();
                    }
                });

                if (collisionDetector.objectIntersect(hitBox, activeBoss)
                        && collisionDetector.calculateCollisionPercentage(hitBox, activeBoss)
                        > HIT_BOX_COLLISION_PERCENTAGE) {
                    found.set(true);
                    hitBox.stopInitialEffect();
                    hitBox.startOnHitEffect().thenAccept(isDone -> {
                        gameRoot.getChildren().remove(hitBox);
                        player.vanishMeleeHitBox();
                    });
                    final int meleeDamage = player.getWeaponDamage(WeaponType.MELEE_WEAPON);
                    if (activeBoss.takeDamage(meleeDamage) <= 0) {
                        endBossFight();
                    } else {
                        activeBoss.getHurt();
                    }
                }
            }
        }

        /*
         * Interacts with the boss with range weapons.
         * @param existingRangeHitBox the existing range hit box
         */
        private void rangeWithBoss(final AttackEffect existingRangeHitBox) {
            if (activeBoss.getDamageEnable()) {
                if (collisionDetector.objectIntersect(existingRangeHitBox, activeBoss) & activeBoss.getDamageEnable()) {
                    final int rangeDamage = player.getWeaponDamage(WeaponType.RANGE_WEAPON);
                    rangeCombat(existingRangeHitBox, activeBoss);
                    if (activeBoss.takeDamage(rangeDamage) != 0) {
                        activeBoss.getHurt();
                    } else {
                        endBossFight();
                    }
                }
            }
        }

        /*
         * Interacts with the boss.
         */
        private void interactWithBoss() {
            AttackEffect existingRangeHitBox = player.getRangeHitBox();
            if (existingRangeHitBox != null) {
                this.rangeWithBoss(existingRangeHitBox);
            }
        }

        /*
         * Check and handle collision with boss weapons
         */
        private void interactWithBossProjectiles() {
            final double itemCollisionPercentage = 40;
            Iterator<BossWeapon> iterator = this.projectiles.iterator();

            while (iterator.hasNext()) {
                BossWeapon projectile = iterator.next();

                if (collisionDetector.objectIntersect(player, projectile)
                        && collisionDetector.calculateCollisionPercentage(player, projectile)
                        > itemCollisionPercentage) {
                    if (projectile.getSubtype() == BossWeaponType.PROJECTILE) {
                        player.getHurt();
                        uiManager.refreshHealthBar(player.getHealthPoint(), player.getMaxHealthPoints());
                        iterator.remove();
                        gameRoot.getChildren().remove(projectile);
                    }
                }
            }
        }
    }

    /**
     * Checks if the game is over.
     */
    private void gameOverCondition() {
        if (player.getTranslateY() > MainApplication.WINDOW_HEIGHT
            || player.getHealthPoint() <= 0) {
            System.out.println("Game Over");
            System.exit(0);
        }
    }

    /**
     * Starts the game loop.
     */
    public void startGameLoop() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(final long l) {
                try {
                    resetCachedBlockArray();
                    keyboardListeners();
                    player.applyGravity();
                    blockInteraction.interactWithBlocksY();
                    itemInteraction.interactWithItems();
                    enemyInteraction.interactWithEnemies();
                    if (activeBossFight != null) {
                        activeBossFight.interactWithBoss();
                        activeBossFight.interactWithBossProjectiles();
                    }
                    checkForBossPresence();
                    gameOverCondition();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        timer.start();
    }

    // Get the background image for the level
    private String getLevelBackground() {
        final String fileFormat;
        final int bossLevel = 3;
        if (levelManager.getCurrentLevel() == bossLevel) {
            fileFormat = "gif";
        } else {
            fileFormat = "png";
        }
        return String.format("Background/%s.%s", levelManager.getCurrentLevel(), fileFormat);
    }

    /**
     * Gets the root of the application.
     * @return the root of the application
     */
    public Pane getAppRoot() {
        return appRoot;
    }
}

