package ca.bcit.comp2522.termproject.pix;

import ca.bcit.comp2522.termproject.pix.gamecontroller.GameController;
import ca.bcit.comp2522.termproject.pix.screens.Screen;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;

import java.util.LinkedHashMap;

/**
 * Represents the main application window.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023
 */
public class MainApplication extends Application {

    /** Width of the window. */
    public static final int WINDOW_WIDTH = 1500;
    /** Height of the window. */
    public static final int WINDOW_HEIGHT = 720;

    /**
     * Starts the Game.
     *
     * @param stage the stage to run the game in as a Stage
     * @throws IOException if the game cannot be started
     */
    public void startGame(final Stage stage) throws IOException {
        GameController gameApp = new GameController(WINDOW_WIDTH, WINDOW_HEIGHT, stage);
        Scene scene = new Scene(gameApp.getAppRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.setScene(scene);
        gameApp.insertKeyboardListeners();
        gameApp.startGameLoop();
    }

    /**
     * Creates the menu screen.
     *
     * @param stage the stage to run the menu screen in as a Stage
     * @return the menu screen as a Screen
     */
    public Screen createMenuScreen(final Stage stage) {
        final int maxMenuTitleWidth = 390;
        final int titleXOffset = 75;
        final int titleYOffset = 410;

        final int menuBoxXOffset = 75;
        final int menuBoxYOffset = 505;
        final int menuBoxItemTopPadding = 20;

        final LinkedHashMap<String, Command> menuItems = new LinkedHashMap<>();
        menuItems.put("New Game", () -> {
            try {
                startGame(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        menuItems.put("Load", () -> System.out.println("Load"));
        menuItems.put("Quit", Platform::exit);

        Screen menuScreen = new Screen(WINDOW_WIDTH, WINDOW_HEIGHT);

        menuScreen.addBackground("menuBg.gif");
        menuScreen.addTitle("Lowkey Time Travellers", Color.WHITE, TextAlignment.LEFT,
                maxMenuTitleWidth, titleXOffset, titleYOffset);
        menuScreen.addMenu(menuItems, TextAlignment.LEFT, menuBoxXOffset,
                menuBoxYOffset, menuBoxItemTopPadding);

        return menuScreen;
    }

    /**
     * Starts the application.
     *
     * @param stage the stage to run the application
     */
    @Override
    public void start(final Stage stage) {
        Screen menuScreen = createMenuScreen(stage);
        Scene scene = new Scene(menuScreen.getRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.setTitle("Lowkey Time Travellers");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Launches the application.
     *
     * @param args not used
     */
    public static void main(final String[] args) {
        launch(args);
    }
}


