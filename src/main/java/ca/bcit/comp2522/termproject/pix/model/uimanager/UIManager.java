package ca.bcit.comp2522.termproject.pix.model.uimanager;

import ca.bcit.comp2522.termproject.pix.MainApplication;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class UIManager {
    ArrayList<HBox> uiItems;
    HBox batteryCounter;
    HBox worldName;
    public UIManager() throws IOException {
       this.uiItems = new ArrayList<>();
       this.batteryCounter = new HBox();
       this.worldName = new HBox();
       this.initialSetting(batteryCounter);
       this.refreshBatteryCounter(0);
       this.initialSetting(worldName);
       this.refreshWorldName(0);
    }

    public final void initialSetting(HBox section) throws IOException {
        section.setPadding(new Insets(5, 40, 5, 10));
        section.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); " +
                "-fx-background-radius: " + 10 + ";");
    }

    public final void refreshBatteryCounter(int batteryCount) throws IOException {
        setAndClearTopHBox(batteryCounter, 24, String.format("x %s", batteryCount),
                18, "ui/battery.png",MainApplication.WINDOW_WIDTH - 200);
    }

    public final void refreshWorldName(int level) throws IOException {
        String[] world = {"In the Past", "Present Day", "In the Future", "Boss Dimension"};
        setAndClearTopHBox(worldName, 18, String.format("Timeline - %s", world[level]),
                12, "ui/world.png", 50);
    }

    private void setAndClearTopHBox(final HBox box, final int fontSize, final String text, final double fontTranslateY,
                                    final String imagePath, final double translateX) throws IOException {
        final double translateY = 50;
        box.getChildren().clear();

        Font font = Font.font("Verdana", FontWeight.BOLD, fontSize);
        InputStream fontStream = getClass().getResourceAsStream("BungeeSpice-Regular.ttf");

        if (fontStream != null) {
            font = Font.loadFont(fontStream, fontSize);
            fontStream.close();
        }

        Text description = new Text(text);
        description.setFont(font);
        description.setFill(Color.WHITE);
        description.setTranslateY(fontTranslateY);
        description.setTranslateX(10);

        box.getChildren().add(new ImageView(new Image(String.valueOf(MainApplication.class.getResource(imagePath)))));
        box.getChildren().add(description);
        box.setTranslateX(translateX);
        box.setTranslateY(translateY);
    }

    public HBox getWorldName() {
        return worldName;
    }


    public HBox getBatteryCounter() {
        return batteryCounter;
    }


}
