package ca.bcit.comp2522.termproject.pix.gamecontroller;

import javafx.scene.layout.Pane;

/**
 * Represents the main game loop.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public class GameController {
    private final Pane appRoot;

    public GameController() {
        this.appRoot = new Pane();
    };

    public void startGameLoop() {};

    public Pane getAppRoot() {
        return appRoot;
    }
}

