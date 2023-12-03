package ca.bcit.comp2522.termproject.pix;

import ca.bcit.comp2522.termproject.pix.gamecontroller.GameController;
import javafx.application.Application;
import javafx.scene.Scene;
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
        GameController gameApp = new GameController(WINDOW_WIDTH, WINDOW_HEIGHT);
        Scene scene = new Scene(gameApp.getAppRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.setTitle("Lowkey Time Travellers");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        gameApp.insertKeyboardListeners();
        gameApp.startGameLoop();
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


