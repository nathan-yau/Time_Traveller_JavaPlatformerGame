package ca.bcit.comp2522.termproject.pix;

import ca.bcit.comp2522.termproject.pix.screens.MenuScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

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
     * Starts the application.
     *
     * @param stage the stage to run the application
     */
    @Override
    public void start(final Stage stage) {
        final int maxMenuTitleWidth = 390;
        final int titleXOffset = 75;
        final int titleYOffset = 410;

        MenuScreen menuScreen = new MenuScreen(WINDOW_WIDTH, WINDOW_HEIGHT);
        Scene scene = new Scene(menuScreen.getRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.setTitle("Lowkey Time Travellers");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        menuScreen.addBackground("menuBg.gif");
        menuScreen.addTitle("Lowkey Time Travellers", Color.WHITE, TextAlignment.LEFT,
                maxMenuTitleWidth, titleXOffset, titleYOffset);
        menuScreen.addMenu();
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


