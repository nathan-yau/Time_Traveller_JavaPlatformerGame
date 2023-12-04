package ca.bcit.comp2522.termproject.pix.screens;

import ca.bcit.comp2522.termproject.pix.gamecontroller.GameController;
import ca.bcit.comp2522.termproject.pix.screens.screenelements.MenuItem;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents the main menu screen.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-12
 */
public final class MenuScreen extends Screen {
    private final ArrayList<String> menuItems;
    private final VBox menuBox;

    /**
     * Constructs a MenuScreen.
     *
     * @param windowWidth the width of the window as an int
     * @param windowHeight the height of the window as an int
     */
    public MenuScreen(final int windowWidth, final int windowHeight) {
        super(windowWidth, windowHeight);
        final int menuBoxOffset = -5;
        this.menuItems = new ArrayList<>();
        this.menuBox = new VBox(menuBoxOffset);
        this.menuItems.addAll(Arrays.asList("New Game", "Load", "Exit"));
    }

    /**
     * Adds the Menu Items.
     */
    public void addMenu() {
        final int menuItemTopPadding = 20;
        final int menuBoxXOffset = 75;
        final int menuBoxYOffset = 505;

        menuBox.setTranslateX(menuBoxXOffset);
        menuBox.setTranslateY(menuBoxYOffset);
        this.menuItems.forEach(data -> {
            MenuItem item;
            item = new MenuItem(data, TextAlignment.LEFT);
            item.setOnMouseClicked(event -> {
                switch (data) {
                    case "New Game" -> startGame(this.getStage());
                    case "Load" -> System.out.println("Load game");
                    case "Exit" -> Platform.exit();
                    default -> throw new IllegalStateException("Unexpected value: " + data);
                }
            });
            item.setPadding(new javafx.geometry.Insets(menuItemTopPadding, 0, 0, 0));
            menuBox.getChildren().addAll(item);
        });

        getRoot().getChildren().add(menuBox);
    }

    /**
     * Starts the Game.
     *
     * @param stage the stage to run the game in as a Stage
     */
    public void startGame(final Stage stage) {
        GameController gameApp = new GameController(this.getWindowWidth(), this.getWindowHeight(), this.getStage());
        Scene scene = new Scene(gameApp.getAppRoot(), this.getWindowWidth(), this.getWindowHeight());

        stage.setScene(scene);

        gameApp.insertKeyboardListeners();
        gameApp.startGameLoop();
    }
}
