package ca.bcit.comp2522.termproject.pix.gamecontroller;

import ca.bcit.comp2522.termproject.pix.MainApplication;
import ca.bcit.comp2522.termproject.pix.model.block.StandardBlock;
import ca.bcit.comp2522.termproject.pix.model.platformgenerator.PlatformManager;
import ca.bcit.comp2522.termproject.pix.model.player.Player;
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
import java.util.HashMap;

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

    /**
     * Constructs a GameController object with default values.
     * Set up the initial platform and player.
     */
    public GameController() {
        final double initialPlayerX = 0;
        final double initialPlayerY = 700;
        this.appRoot = new Pane();
        this.gameRoot = new Pane();
        this.uiRoot = new Pane();
        this.platform = new PlatformManager();
        this.keyboardChecker = new HashMap<>();
        this.player = new Player(initialPlayerX, initialPlayerY, "Player/idle.png");
        this.setBackground("Background/1.png");
        this.setUpPlatform();
        this.setUpCamera();
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
    }

    /**
     * Sets up the camera.
     */
    private void setUpCamera() {
        final DoubleProperty playerX = player.translateXProperty();
        final int xCameraThreshold = 300;
        final int xCameraAdjustment = 300;

        final DoubleProperty playerY = player.translateYProperty();
        final int yCameraUpperThreshold = 600;
        final int yCameraLowerThreshold = 400;
        final int yCameraAdjustment = 300;

        // Adjust the camera horizontal position based on player's location
        playerX.addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();

            if (offset > xCameraThreshold && offset < platform.getTotalLevelWidth() - xCameraThreshold) {
                gameRoot.setLayoutX(-(offset - xCameraAdjustment));
            }
        });

        // Adjust the camera vertical position based on player's location
        playerY.addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();

            if (offset > yCameraUpperThreshold && offset < platform.getTotalLevelHeight() - yCameraLowerThreshold) {
                gameRoot.setLayoutY(-(offset - yCameraAdjustment));
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
     * Listens to the keyboard.
     * @throws IOException if the image is not found
     */
    private void keyboardListeners() throws IOException {
        final int outOfBounds = 5;
        final int pixelPerStep = 5;
        // Listen to jump signal and prevent for jumping out of the map
        if (isPressed(KeyCode.W) && player.getTranslateY() >= outOfBounds) {
            player.setJumpSpeed();
        }

        // Listen to backward signal and prevent for running out of the map
        if (isPressed(KeyCode.A) && player.getTranslateX() >= outOfBounds) {
            player.updateHorizontalMovement(-pixelPerStep);
        }

        // Listen to forward signal
        if (isPressed(KeyCode.D)) {
            player.updateHorizontalMovement(pixelPerStep);
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
                    keyboardListeners();
                    player.updateVerticalMovement();
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

