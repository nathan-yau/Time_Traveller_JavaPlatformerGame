package ca.bcit.comp2522.termproject.pix.gamecontroller;

import ca.bcit.comp2522.termproject.pix.GameType;
import ca.bcit.comp2522.termproject.pix.MainApplication;
import ca.bcit.comp2522.termproject.pix.model.AttackEffect.AttackEffect;
import ca.bcit.comp2522.termproject.pix.model.GameObject;
import ca.bcit.comp2522.termproject.pix.model.block.BlockType;
import ca.bcit.comp2522.termproject.pix.model.block.StandardBlock;
import ca.bcit.comp2522.termproject.pix.model.pickupitem.PickUpItem;
import ca.bcit.comp2522.termproject.pix.model.pickupitem.PickUpItemType;
import ca.bcit.comp2522.termproject.pix.model.platformgenerator.PlatformManager;
import ca.bcit.comp2522.termproject.pix.model.player.Player;
import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
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
import java.util.Objects;

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
        this.lastCacheXPosition = initialPlayerX;
        this.lastCacheYPosition = initialPlayerY;
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

        for (Node item: platform.getItemArray()) {
            gameRoot.getChildren().add(item);
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
            if (Objects.equals(block.getSubtype(), BlockType.DECORATION_BLOCK)) {
                continue;
            }
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
        private final int xTolerance = 5;
        private final int yTolerance = 20;

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
            double distance = Math.abs(firstGameObject.getMinY() - secondGameObject.getMaxY());
            if (upSide) {
                distance = Math.abs(firstGameObject.getMaxY() - secondGameObject.getMinY());
            }
            return distance <= yTolerance;
        }

        /**
         * Checks if the two objects are on the same y-axis.
         * @param firstGameObject the first game object
         * @param secondGameObject the second game object
         * @return true if the player is colliding with the platform, false otherwise
         */
        private boolean onSameYAxis(final GameObject<? extends GameType> firstGameObject,
                                    final GameObject<? extends GameType> secondGameObject) {
            return Math.abs(firstGameObject.getMaxY() - secondGameObject.getMaxY()) <= yTolerance;
        }

        /**
         * Checks if the two objects are on the same x-axis.
         * @param firstGameObject the first game object
         * @param secondGameObject the second game object
         * @return true if the player is colliding with the platform, false otherwise
         */
        private boolean onSameXAxis(final GameObject<? extends GameType> firstGameObject,
                                    final GameObject<? extends GameType> secondGameObject) {
            boolean firstObjectOnLeft = secondGameObject.getMinX() + xTolerance < firstGameObject.getMaxX()
                    && firstGameObject.getMaxX() <= secondGameObject.getMaxX();
            boolean firstObjectOnRight = secondGameObject.getMinX() < firstGameObject.getMinX()
                    && firstGameObject.getMinX() <= secondGameObject.getMaxX() - xTolerance;
            return firstObjectOnLeft || firstObjectOnRight;
        }
    }

    /**
     * Represents the block interaction.
     */
    private final class BlockInteraction {
        BlockInteraction() { };

        /**
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
                    if (collisionDetector.objectIntersect(player, block)) {
                        if (collisionDetector.collidingDetectorX(player, block, movingRight)) {
                            return;
                        }
                    }
                }
                player.moveX(movingRight);
            }
            player.nextImageFrame();
        }

        /**
         * Interacts with the blocks on the y-axis.
         */
        private void interactWithBlocksY() {
            final double vectorY = player.getVelocityY();
            final boolean movingDown = vectorY > 0;
            for (int i = 0; i < Math.abs(vectorY); i++) {
                for (StandardBlock block : cachedBlockArray) {
                    if (collisionDetector.objectIntersect(player, block)) {
                        if (collisionDetector.collidingDetectorY(player, block, movingDown)
                                && collisionDetector.onSameXAxis(player, block)) {
                            if (movingDown) {
                                player.offsetGravity();
                            }
                            return;
                        }
                    }
                }
                player.moveY(movingDown);
            }
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
            Iterator<PickUpItem> iterator = platform.getItemArray().iterator();
            boolean yAxisCollision;

            while (iterator.hasNext()) {
                final int yThreshold = 10;
                PickUpItem item = iterator.next();

                yAxisCollision = (player.getBoundsInParent().getMaxY()
                        + yThreshold >= item.getBoundsInParent().getMaxY()
                        & player.getBoundsInParent().getMinY() - yThreshold <= item.getBoundsInParent().getMaxY());

                if (collisionDetector.objectIntersect(player, item) & yAxisCollision) {
                    if (item.getSubtype() == PickUpItemType.HEALTH_POTION) {
                        player.incrementHealthPotionCounter();
                        System.out.println("Potion count: " + player.getHealthPotionCounter());
                    } else if (item.getSubtype() == PickUpItemType.GOLD_COIN) {
                        player.incrementGoldCoinCounter();
                        System.out.println("Gold Coin count: " + player.getGoldCoinCounter());
                    }
                    if (item.onPickUp()) {
                        iterator.remove();
                    }
                }
            }
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
        System.out.println(keyboardChecker);
        if (isPressed(KeyCode.W) && player.getTranslateY() >= outOfBounds) {
            player.setJumpSpeed();
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

        // Listen to melee attack signal
        if (isPressed(KeyCode.O) && player.getTranslateX() >= outOfBounds) {
            AttackEffect hitBox = player.meleeAttack();
            if (hitBox != null) {
                gameRoot.getChildren().add(hitBox);
                hitBox.startEffect().thenAccept(isDone -> {
                    if (isDone) {
                        gameRoot.getChildren().remove(hitBox);
                    }
                });
            }
        }
        // Listen to range attack signal
        if (isPressed(KeyCode.P) && player.getTranslateX() >= outOfBounds) {
            AttackEffect hitBox = player.rangeAttack();
            if (hitBox != null) {
                gameRoot.getChildren().add(hitBox);
                hitBox.startEffect().thenAccept(isDone -> {
                    if (isDone) {
                        gameRoot.getChildren().remove(hitBox);
                    }
                });
            }
        }

        if (!isAnyKeyPressed() & !player.isPlayerInAction()) {
            player.setIdle();
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

