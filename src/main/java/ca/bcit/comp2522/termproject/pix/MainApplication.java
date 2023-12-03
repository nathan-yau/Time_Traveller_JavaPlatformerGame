package ca.bcit.comp2522.termproject.pix;

import ca.bcit.comp2522.termproject.pix.gamecontroller.GameController;
import ca.bcit.comp2522.termproject.pix.menu.MenuItem;
import ca.bcit.comp2522.termproject.pix.menu.MenuTitle;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

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
     */
    public void startGame(final Stage stage) {
        GameController gameApp = new GameController(WINDOW_WIDTH, WINDOW_HEIGHT);
        Scene scene = new Scene(gameApp.getAppRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.setScene(scene);
        stage.show();

        gameApp.insertKeyboardListeners();
        gameApp.startGameLoop();
    }

    private final class Menu {
        private final Pane root = new Pane();
        private final ArrayList<String> menuItems = new ArrayList<>();
        private final VBox menuBox = new VBox(-5);

        private Menu(final String[] menuItems) {
            this.menuItems.addAll(Arrays.asList(menuItems));
        }

        /**
         * Starts the Menu.
         */
        private void addMenu() {
            final int menuItemTopPadding = 20;

            menuBox.setTranslateX(75);
            menuBox.setTranslateY(505);
            this.menuItems.forEach(data -> {
                MenuItem item;
                item = new MenuItem(data);
                item.setOnMouseClicked(event -> {
                    switch (data) {
                        case "New Game" -> startGame((Stage) root.getScene().getWindow());
                        case "Load" -> System.out.println("Load game");
                        case "Exit" -> Platform.exit();
                        default -> throw new IllegalStateException("Unexpected value: " + data);
                    }
                });
                item.setPadding(new javafx.geometry.Insets(menuItemTopPadding, 0, 0, 0));
                menuBox.getChildren().addAll(item);
            });

            root.getChildren().add(menuBox);
        }

        /*
         * Add the title of the game.
         */
        private void addMenuTitle() {
            final int maxMenuTitleWidth = 390;
            final int titleXOffset = 75;
            final int titleYOffset = 410;
            MenuTitle title = new MenuTitle("Lowkey Time Travellers", maxMenuTitleWidth);

            title.setTranslateX(titleXOffset);
            title.setTranslateY(titleYOffset);

            root.getChildren().add(title);
        }

        /*
         * Adds the menu background.
         */
        private void addMenuBackground() {
            ImageView bgImg = new ImageView(new Image(
                    Objects.requireNonNull(getClass().getResourceAsStream(String.format("menu/%s", "menuBg.gif")))
                ));
            bgImg.setFitHeight(WINDOW_HEIGHT);
            bgImg.setFitWidth(WINDOW_WIDTH);
            root.getChildren().add(bgImg);
        }
    }

    /**
     * Starts the application.
     *
     * @param stage the stage to run the application
     */
    @Override
    public void start(final Stage stage) {
        Menu menu = new Menu(new String[]{"New Game", "Load", "Exit"});
        Scene scene = new Scene(menu.root, WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.setTitle("Lowkey Time Travellers");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        menu.addMenuBackground();
        menu.addMenuTitle();
        menu.addMenu();
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


