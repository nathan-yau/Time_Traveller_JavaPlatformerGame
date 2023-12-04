package ca.bcit.comp2522.termproject.pix.model.uimanager;

import ca.bcit.comp2522.termproject.pix.MainApplication;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
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
    HBox playerStatus;
    HBox backpack;
    ProgressBar healthBar;
    Label healthLabel;
    public UIManager(double playerHP) throws IOException {
       this.uiItems = new ArrayList<>();
       this.batteryCounter = new HBox();
       this.worldName = new HBox();
       this.playerStatus = new HBox();
       this.healthBar = new ProgressBar();
       this.healthLabel = new Label();
       this.initialSetting(batteryCounter);
       this.refreshBatteryCounter(0);
       this.initialSetting(worldName);
       this.refreshWorldName(0);
       this.initialHealthBar();
       this.refreshHealthBar(playerHP, playerHP);
       this.initialSetting(playerStatus);
       this.refreshPlayerStatus();
    }

    public final void initialSetting(HBox section) {
        section.setPadding(new Insets(5, 40, 5, 10));
        section.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); " +
                "-fx-background-radius: " + 10 + ";");
    }

    public final void refreshBatteryCounter(int batteryCount) throws IOException {
        final double translateY = 20;
        final double translateX = MainApplication.WINDOW_WIDTH - 200;
        batteryCounter.getChildren().clear();
        setPictureContent(batteryCounter, "ui/battery.png");
        setTextContent(batteryCounter, 24, String.format("x %s", batteryCount),
                18);
        batteryCounter.setTranslateX(translateX);
        batteryCounter.setTranslateY(translateY);
    }

    public final void refreshPlayerStatus() throws IOException {
        final double translateY = 80;
        final double translateX = 50;
        playerStatus.getChildren().clear();
        setTextContent(playerStatus, 18, "HP",0);
        playerStatus.getChildren().add(healthLabel);
        playerStatus.getChildren().add(healthBar);
        playerStatus.setTranslateX(translateX);
        playerStatus.setTranslateY(translateY);
    }

    public final void refreshWorldName(int level) throws IOException {
        final double translateY = 20;
        final double translateX = 50;
        String[] world = {"In the Past", "Present Day", "In the Future", "Boss Dimension"};
        worldName.getChildren().clear();
        setPictureContent(worldName, "ui/world.png");
        setTextContent(worldName, 18, String.format("Timeline - %s", world[level]),12);
        worldName.setTranslateX(translateX);
        worldName.setTranslateY(translateY);
    }

    private Font setDefaultFont(int fontSize) throws IOException {
        Font font = Font.font("Verdana", FontWeight.BOLD, fontSize);
        InputStream fontStream = getClass().getResourceAsStream("BungeeSpice-Regular.ttf");

        if (fontStream != null) {
            font = Font.loadFont(fontStream, fontSize);
            fontStream.close();
        }

        return font;
    }

    private void setTextContent(final HBox box, final int fontSize, final String text,
                                final double fontTranslateY) throws IOException {
        Text description = new Text(text);
        description.setFont(this.setDefaultFont(fontSize));
        description.setFill(Color.WHITE);
        description.setTranslateY(fontTranslateY);
        description.setTranslateX(10);

        box.getChildren().add(description);
    }

    private void setPictureContent(final HBox box, final String imagePath) {
        ImageView imageView = new ImageView(new Image(String.valueOf(MainApplication.class.getResource(imagePath))));
        box.getChildren().add(imageView);
    }


    private void initialHealthBar() throws IOException {
        healthBar.setMaxWidth(240);
        healthBar.setPrefWidth(240);
        healthBar.setMinWidth(100);
        healthBar.setPrefHeight(20);
        healthBar.setTranslateX(30);
        healthLabel.setFont(this.setDefaultFont(13));
        healthLabel.setStyle("-fx-text-fill: white;");
        healthLabel.setTranslateX(20);
        healthLabel.setTranslateY(3);
    };

    public void refreshHealthBar(final double currentHP, final double maxHP){
        final double progress = currentHP / maxHP;
        final String color;
        healthBar.setProgress(progress);
        if (progress > 0.5) {
            color = "-fx-accent: lightgreen;";
        } else if (progress > 0.25) {
            color = "-fx-accent: yellow;";
        } else {
            color = "-fx-accent: red;";
        }
        healthBar.setStyle(color);
        healthLabel.setText(String.format("%.0f / %.0f", currentHP, maxHP));
    }

    public HBox getWorldName() {
        return worldName;
    }


    public HBox getBatteryCounter() {
        return batteryCounter;
    }

    public HBox getPlayerStatus() {
        return playerStatus;
    }
}
