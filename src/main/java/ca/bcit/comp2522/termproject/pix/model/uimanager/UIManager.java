package ca.bcit.comp2522.termproject.pix.model.uimanager;

import ca.bcit.comp2522.termproject.pix.MainApplication;
import ca.bcit.comp2522.termproject.pix.model.Enemy.EnemyType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class UIManager {
    private final ArrayList<HBox> enemyHealthBars;
    private final HBox batteryCounter;
    private final VBox meleeSlot;
    private final VBox rangeSlot;
    private final VBox ammoSlot;
    private final VBox potionSlot;
    private final VBox meleeIcon;
    private final VBox rangeIcon;
    private final VBox ammoIcon;
    private final VBox potionIcon;
    private final HBox worldName;
    private final HBox playerStatus;
    private final HBox backpack;
    private final ProgressBar healthBar;
    private final Label healthLabel;


    public UIManager(final double playerHP) throws IOException {
       final Insets backpackPadding = new Insets(5, 10, 5, 10);
       this.enemyHealthBars = new ArrayList<>();
       this.batteryCounter = new HBox();
       this.worldName = new HBox();
       this.playerStatus = new HBox();
       this.backpack = new HBox(20);
       this.meleeSlot = new VBox(5);
       this.rangeSlot = new VBox();
       this.ammoSlot = new VBox();
       this.potionSlot = new VBox();
       this.meleeIcon = new VBox();
       this.rangeIcon = new VBox();
       this.ammoIcon = new VBox();
       this.potionIcon = new VBox();
       this.healthBar = new ProgressBar();
       this.healthLabel = new Label();
       this.initialSetting(batteryCounter);
       this.refreshBatteryCounter(0);
       this.initialSetting(worldName);
       this.refreshWorldName(0);
       this.initialHealthBar();
       this.refreshHealthBar(playerHP, playerHP);
       this.initialSetting(playerStatus);
       this.initialSetting(backpack, backpackPadding, "rgba(0, 0, 0, 0.5)");
       this.initializeSlots();
       this.initializeBackPack();
       this.refreshPlayerStatus();
    }

    private void initialSetting(final Pane section) {
        final Insets padding = new Insets(5, 40, 5, 10);
        final String colorCode = "rgba(0, 0, 0, 0.5)";
        this.initialSetting(section, padding, colorCode);
    }

    private void initialSetting(final Pane section, final Insets padding, final String colorCode) {
        final int borderRadius = 10;
        section.setPadding(padding);
        section.setStyle("-fx-background-color:" + colorCode + "; -fx-background-radius: " + borderRadius + ";");
    }

    /**
     * Refreshes the backpack slot.
     *
     * @param slot the slot to refresh as a VBox
     * @param icon the icon to refresh as a VBox
     * @param imagePath the image path as a String
     * @param text the text to refresh as a String
     * @throws IOException if the image path is invalid
     */
    public void refreshBackpackSlot(final VBox slot, final VBox icon, final String imagePath,
                                    final String text) throws IOException {
        final Insets innerPadding = new Insets(5, 5, 5, 5);
        final int fontSize = 13;
        final int textTranslateProperty = 0;
        icon.getChildren().clear();
        setPictureContent(icon, imagePath);
        initialSetting(icon, innerPadding, "rgba(255, 255, 255, 0.5)");
        slot.getChildren().clear();
        slot.setAlignment(Pos.CENTER);
        slot.getChildren().add(icon);
        setTextContent(slot, fontSize, text, textTranslateProperty, textTranslateProperty);
    }

    public void addEnemyHealthBar(final EnemyType type, final double healthPoint) throws IOException {
        HBox enemyHealthBar = new HBox(5);
        ProgressBar enemyHealth = new ProgressBar(healthPoint);
        this.initialSetting(enemyHealthBar, new Insets(5, 5, 5, 5), "rgba(0, 0, 0, 0.5)");
        this.setTextContent(enemyHealthBar, 15, type.name(), 0, 0);
        enemyHealthBar.getChildren().add(enemyHealth);
        enemyHealthBar.setTranslateX(250 * enemyHealthBars.size() + 50);
        enemyHealthBar.setTranslateY(MainApplication.WINDOW_HEIGHT - 50);
        enemyHealthBars.add(enemyHealthBar);
    }

    public ArrayList<HBox> getEnemyHealthBars() {
        return enemyHealthBars;
    }

    public void clearEnemyHealthBars() {
        enemyHealthBars.clear();
    }

    public void refreshMeleeSlot(final boolean isSword) throws IOException {
        if (isSword) {
            refreshBackpackSlot(meleeSlot, meleeIcon, "ui/sword.png", "Sword");
        } else {
            refreshBackpackSlot(meleeSlot, meleeIcon, "ui/punch.png", "Fist");
        }
    }

    public void refreshRangeSlot(final boolean isBow) throws IOException {
        if (isBow) {
            refreshBackpackSlot(rangeSlot, rangeIcon, "ui/bow.png", "Bow");
        } else {
            refreshBackpackSlot(rangeSlot, rangeIcon, "ui/backpack.png", "Empty");
        }
    }

    public void refreshAmmoSlot(final int ammoCount) throws IOException {
        if (ammoCount <= 0) {
            refreshBackpackSlot(ammoSlot, ammoIcon, "ui/backpack.png", "Empty");
        } else {
            final String text = String.format("%s", ammoCount);
            refreshBackpackSlot(ammoSlot, ammoIcon, "ui/ammo.png", text);
        }
    }

    public void refreshPotionSlot(final int potionCount) throws IOException {
        if (potionCount <= 0) {
            refreshBackpackSlot(potionSlot, potionIcon, "ui/backpack.png", "Empty");
        } else {
            final String text = String.format("%s", potionCount);
            refreshBackpackSlot(potionSlot, potionIcon, "ui/potion.png", text);
        }
    }


    /*
     * Initializes the backpack slots.
     * @throws IOException if the image path is invalid
     */
    private void initializeSlots() throws IOException {
        refreshMeleeSlot(false);
        refreshRangeSlot(false);
        refreshAmmoSlot(0);
        refreshPotionSlot(0);
    }

    private void initializeBackPack() {
        final double translateY = MainApplication.WINDOW_HEIGHT - 100;
        final double translateX = MainApplication.WINDOW_WIDTH - 350;
        backpack.getChildren().add(meleeSlot);
        backpack.getChildren().add(rangeSlot);
        backpack.getChildren().add(ammoSlot);
        backpack.getChildren().add(potionSlot);
        backpack.setTranslateX(translateX);
        backpack.setTranslateY(translateY);
    }

    public final void refreshBatteryCounter(int batteryCount) throws IOException {
        final double translateY = 20;
        final double translateX = MainApplication.WINDOW_WIDTH - 200;
        batteryCounter.getChildren().clear();
        setPictureContent(batteryCounter, "ui/battery.png");
        setTextContent(batteryCounter, 24, String.format("x %s", batteryCount),
                18, 10);
        batteryCounter.setTranslateX(translateX);
        batteryCounter.setTranslateY(translateY);
    }

    public final void refreshPlayerStatus() throws IOException {
        final double translateY = 80;
        final double translateX = 50;
        playerStatus.getChildren().clear();
        setTextContent(playerStatus, 18, "HP",0, 10);
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
        setTextContent(worldName, 18, String.format("Timeline - %s", world[level]),12, 10);
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

    private void setTextContent(final Pane box, final int fontSize, final String text,
                                final double fontTranslateY, final double fontTranslateX) throws IOException {
        Text description = new Text(text);
        description.setFont(this.setDefaultFont(fontSize));
        description.setFill(Color.WHITE);
        description.setTranslateY(fontTranslateY);
        description.setTranslateX(fontTranslateX);

        box.getChildren().add(description);
    }

    private void setPictureContent(final Pane box, final String imagePath) {
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

    public HBox getBackpack() {
        return backpack;
    }
}
