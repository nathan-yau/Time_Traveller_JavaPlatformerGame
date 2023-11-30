package ca.bcit.comp2522.termproject.pix.gamecontroller;

import ca.bcit.comp2522.termproject.pix.GameType;
import ca.bcit.comp2522.termproject.pix.MainApplication;
import ca.bcit.comp2522.termproject.pix.model.AttackEffect.AttackEffect;
import ca.bcit.comp2522.termproject.pix.model.Enemy.Enemy;
import ca.bcit.comp2522.termproject.pix.model.GameObject;
import ca.bcit.comp2522.termproject.pix.model.block.BlockType;
import ca.bcit.comp2522.termproject.pix.model.block.StandardBlock;
import ca.bcit.comp2522.termproject.pix.model.pickupitem.PickUpItem;
import ca.bcit.comp2522.termproject.pix.model.pickupitem.PickUpItemType;
import ca.bcit.comp2522.termproject.pix.model.platformgenerator.PlatformManager;
import ca.bcit.comp2522.termproject.pix.model.player.Direction;
import ca.bcit.comp2522.termproject.pix.model.player.Player;
import ca.bcit.comp2522.termproject.pix.model.weapon.MeleeWeapon;
import ca.bcit.comp2522.termproject.pix.model.weapon.RangeWeapon;
import ca.bcit.comp2522.termproject.pix.model.weapon.Weapon;
import ca.bcit.comp2522.termproject.pix.model.weapon.WeaponType;
import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

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
    private boolean rangeTargetHit;

    /**
     * Constructs a GameController object with default values.
     * Set up the initial platform and player.
     */
    public GameController() {
        final double initialPlayerX = 0;
        final double initialPlayerY = 500;
        this.appRoot = new Pane();
        this.gameRoot = new Pane();
        this.uiRoot = new Pane();
        this.platform = new PlatformManager();
        this.keyboardChecker = new HashMap<>();
        this.player = new Player(initialPlayerX, initialPlayerY, "Player/idle.png");
        this.cachedBlockArray = new ArrayList<>();
        this.collisionDetector = new CollisionDetector();
        this.blockInteraction = new BlockInteraction();
        this.itemInteraction = new ItemInteraction();
        this.enemyInteraction = new EnemyInteraction();
        this.lastCacheXPosition = initialPlayerX;
        this.lastCacheYPosition = initialPlayerY;
        this.rangeTargetHit = false;
        this.setBackground("Background/1.png");
        this.setUpPlatform();
        this.setUpCamera();
        this.setCachedBlockArray();
        gameRoot.getChildren().add(player);
    }

    /**
     * Sets up the platform using the PlatformManager.
     */
    private void setUpPlatform() {
        platform.createGamePlatform();

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
        final DoubleProperty playerX = player.translateXProperty();
        final int xCameraThreshold = 300;
        final int xCameraAdjustment = 300;

        // Adjust the camera horizontal position based on player's location
        playerX.addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();

            if (offset > xCameraThreshold && offset < platform.getTotalLevelWidth() - xCameraThreshold) {
                gameRoot.setLayoutX(-(offset - xCameraAdjustment));
            }
        });

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
        private void interactWithItems() {
            final double itemCollisionPercentage = 40;
            Iterator<PickUpItem> iterator = platform.getItemArray().iterator();

            while (iterator.hasNext()) {
                PickUpItem item = iterator.next();

                if (collisionDetector.objectIntersect(player, item)
                        && collisionDetector.calculateCollisionPercentage(player, item) > itemCollisionPercentage) {
                    if (item.getSubtype() == PickUpItemType.HEALTH_POTION) {
                        player.incrementHealthPotionCounter();
                    } else if (item.getSubtype() == PickUpItemType.GOLD_COIN) {
                        player.incrementGoldCoinCounter();
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
                System.out.println(collisionDetector.calculateCollisionPercentage(hitBox, enemy));
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
                    if (enemy.takeDamage(meleeDamage) == 0) {
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
                enemy.setDamageEnable(false);
                existingRangeHitBox.stopInitialEffect();
                rangeTargetHit = true;
                existingRangeHitBox.startOnHitEffect().thenAccept(isDone -> {
                    gameRoot.getChildren().remove(existingRangeHitBox);
                    player.vanishRangeHitBox();
                    rangeTargetHit = false;
                    enemy.setDamageEnable(true);
                });
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
        for (boolean isPressed : keyboardChecker.values()) {
            if (isPressed) {
                return true;
            }
        }
        return false;
    }

    /**
     * Listens to the keyboard.
     * @throws IOException if the image is not found
     */
    private void keyboardListeners() throws IOException {
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

        // Listen to potion use signal
        if (isPressed(KeyCode.H)) {
            player.useHealthPotion();
        }

        // Listen to melee attack signal
        if (isPressed(KeyCode.O) && player.getTranslateX() >= outOfBounds) {
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
            }
        }

        // Listen to range attack signal
        if (isPressed(KeyCode.P) && player.getTranslateX() >= outOfBounds) {
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

        // Listen to idle signal
        if (!isAnyKeyPressed() & !player.isPlayerInAction()) {
            player.setIdle();
        }
    }

    /**
     * Checks if the game is over.
     */
    private void gameOverCondition() {
        if (player.getTranslateY() > MainApplication.WINDOW_HEIGHT) {
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
                    gameOverCondition();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        timer.start();
    }

    /**
     * Gets the root of the application.
     * @return the root of the application
     */
    public Pane getAppRoot() {
        return appRoot;
    }
}

