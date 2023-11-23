package ca.bcit.comp2522.termproject.pix.gamecontroller;

import ca.bcit.comp2522.termproject.pix.model.player.Player;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.*;

import java.io.IOException;

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
    private final Player player;

    /**
     * Constructs a GameController object with default values.
     * Set up the initial platform and player.
     */
    public GameController() {
        final double initialPlayerX = 0;
        final double initialPlayerY = 100;
        this.appRoot = new Pane();
        this.gameRoot = new Pane();
        this.player = new Player(initialPlayerX, initialPlayerY, "Player/idle.png");
        gameRoot.getChildren().add(player);
    };

    /**
     * Creates the pane.
     */
    public void creatingPane() {
        appRoot.getChildren().addAll(gameRoot);
    }

    private void keyboardListeners() throws IOException { }

    /**
     * Starts the game loop.
     */
    public void startGameLoop() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                try {
                    keyboardListeners();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        timer.start();
    };

    /**
     * Gets the root of the application.
     * @return the root of the application
     */
    public Pane getAppRoot() {
        return appRoot;
    }
}

