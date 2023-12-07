package ca.bcit.comp2522.termproject.pix;

import ca.bcit.comp2522.termproject.pix.gamecontroller.GameController;
import ca.bcit.comp2522.termproject.pix.model.Enemy.Enemy;
import ca.bcit.comp2522.termproject.pix.model.block.StandardBlock;
import ca.bcit.comp2522.termproject.pix.model.pickupitem.PickUpItem;
import ca.bcit.comp2522.termproject.pix.model.player.Player;
import ca.bcit.comp2522.termproject.pix.screens.Screen;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
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
    public static void startGame(final Stage stage) throws IOException {
        GameController gameApp = new GameController(WINDOW_WIDTH, WINDOW_HEIGHT, stage);
        Scene scene = new Scene(gameApp.getAppRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.setScene(scene);
        gameApp.insertKeyboardListeners();
        gameApp.startGameLoop();
    }

    /*
     * Starts the Game.
     *
     * @param stage the stage to run the game in as a Stage
     * @param currentLevel the current level of the game as an int
     * @param player the player of the game as a Player
     * @param loadedBlocks the blocks of the game as an ArrayList
     * @param loadedItems the pickup items of the game as an ArrayList
     * @param loadedEnemies the enemies of the game as an ArrayList
     * @throws IOException if the game cannot be started
     */
    private void startGame(final int currentLevel, final Player player,
                          final ArrayList<ArrayList<StandardBlock>> loadedBlocks,
                          final ArrayList<ArrayList<PickUpItem>> loadedItems,
                          final ArrayList<ArrayList<Enemy>> loadedEnemies,
                          final Stage stage) throws IOException {
        GameController gameApp = new GameController(WINDOW_WIDTH, WINDOW_HEIGHT, currentLevel, player,
                loadedBlocks, loadedItems, loadedEnemies, stage);
        Scene scene = new Scene(gameApp.getAppRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.setScene(scene);
        gameApp.insertKeyboardListeners();

        System.out.println("Game loaded successfully.");
        gameApp.startGameLoop();
    }

    /*
     * Loads the Game.
     *
     * @param stage the stage to run the game in as a Stage
     * @throws IOException if the game cannot be started
     */
    private void loadGame(final Stage stage) throws IOException {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("gameState.sav"));
            int loadedCurrentLevel = (int) ois.readObject();
            Player loadedPlayer = (Player) ois.readObject();
            ArrayList<ArrayList<StandardBlock>> loadedBlocks = (ArrayList<ArrayList<StandardBlock>>) ois.readObject();
            ArrayList<ArrayList<PickUpItem>> loadedItems = (ArrayList<ArrayList<PickUpItem>>) ois.readObject();
            ArrayList<ArrayList<Enemy>> loadedEnemies = (ArrayList<ArrayList<Enemy>>) ois.readObject();
            this.startGame(loadedCurrentLevel, loadedPlayer, loadedBlocks, loadedItems, loadedEnemies, stage);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Loading error. Starting a new game.");
            GameController gameApp = new GameController(WINDOW_WIDTH, WINDOW_HEIGHT, stage);
            Scene scene = new Scene(gameApp.getAppRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);

            stage.setScene(scene);
            gameApp.insertKeyboardListeners();
            gameApp.startGameLoop();
        }
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

        final int menuBoxXOffset = 80;
        final int menuBoxYOffset = 525;
        final int menuBoxItemTopPadding = 20;

        final LinkedHashMap<String, Command> menuItems = new LinkedHashMap<>();
        menuItems.put("New Game", () -> {
            try {
                startGame(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        menuItems.put("Load", () -> {
            try {
                loadGame(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        menuItems.put("Quit", Platform::exit);

        Screen menuScreen = new Screen(WINDOW_WIDTH, WINDOW_HEIGHT);

        menuScreen.addBackground("menuBg.gif");
        menuScreen.addTitle("Lowkey Time Travellers", Color.WHITE, TextAlignment.LEFT,
                maxMenuTitleWidth, titleXOffset, titleYOffset);
        menuScreen.addMenu(menuItems, TextAlignment.LEFT, menuBoxXOffset, menuBoxYOffset, menuBoxItemTopPadding);
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


