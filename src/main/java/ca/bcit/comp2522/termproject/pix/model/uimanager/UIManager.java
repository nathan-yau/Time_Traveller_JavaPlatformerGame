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

/**
 * Represents a UI manager.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
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
    private final HBox bossStatus;
    private final HBox backpack;
    private final ProgressBar healthBar;
    private final Label healthLabel;
    private final ProgressBar bossHealthBar;


    /**
     * Constructs an UIManager.
     *
     * @param playerHP the player's health point as a double
     * @throws IOException if the image path is invalid
     */
    public UIManager(final double playerHP) throws IOException {
       final Insets backpackOuterPadding = new Insets(5, 10, 5, 10);
       final int slotPadding = 5;
       final int backpackInnerPadding = 20;
       this.enemyHealthBars = new ArrayList<>();
       this.batteryCounter = new HBox();
       this.worldName = new HBox();
       this.playerStatus = new HBox();
       this.bossStatus = new HBox();
       this.backpack = new HBox(backpackInnerPadding);
       this.meleeSlot = new VBox(slotPadding);
       this.rangeSlot = new VBox();
       this.ammoSlot = new VBox();
       this.potionSlot = new VBox();
       this.meleeIcon = new VBox();
       this.rangeIcon = new VBox();
       this.ammoIcon = new VBox();
       this.potionIcon = new VBox();
       this.healthBar = new ProgressBar();
       this.healthLabel = new Label();
       this.bossHealthBar = new ProgressBar();
       this.initialSetting(batteryCounter);
       this.refreshBatteryCounter(0);
       this.initialSetting(worldName);
       this.refreshWorldName(0);
       this.initialHealthBar();
       this.refreshHealthBar(playerHP, playerHP);
       this.initialSetting(playerStatus);
       this.initialBossHealthBar();
       this.initialSetting(bossStatus);
       this.initialSetting(backpack, backpackOuterPadding, "rgba(0, 0, 0, 0.5)");
       this.initializeSlots();
       this.initializeBackPack();
       this.refreshPlayerStatus();

    }

    /* Set up the initial setting of a Pane.
     * @param the Pane to set up
     */
    private void initialSetting(final Pane section) {
        final Insets padding = new Insets(5, 40, 5, 10);
        final String colorCode = "rgba(0, 0, 0, 0.5)";
        this.initialSetting(section, padding, colorCode);
    }

    /* Set up the initial setting of a Pane.
     * @param the Pane to set up
     * @param the padding of the Pane
     * @param the color code of the Pane
     */
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

    /**
     * Adds an enemy health bar.
     *
     * @param type the enemy type as an EnemyType
     * @param healthPoint the enemy's health point as a double
     * @throws IOException if the image path is invalid
     */
    public void addEnemyHealthBar(final EnemyType type, final double healthPoint) throws IOException {
        final int padding = 5;
        final int fontSize = 15;
        final int lengthOfHealthBar = 250;
        final int startPointOffset = 50;
        final double inDamageHealthPoint = 0.5;
        HBox enemyHealthBar = new HBox(padding);
        ProgressBar enemyHealth = new ProgressBar(healthPoint);
        this.initialSetting(enemyHealthBar, new Insets(padding, padding, padding, padding), "rgba(0, 0, 0, 0.5)");
        this.setTextContent(enemyHealthBar, fontSize, type.name(), 0, 0);
        enemyHealthBar.getChildren().add(enemyHealth);
        enemyHealthBar.setTranslateX(lengthOfHealthBar * enemyHealthBars.size() + startPointOffset);
        enemyHealthBar.setTranslateY(MainApplication.WINDOW_HEIGHT - startPointOffset);
        if (healthPoint > inDamageHealthPoint) {
            enemyHealth.setStyle("-fx-accent: green;");
        } else {
            enemyHealth.setStyle("-fx-accent: red;");
        }
        if (healthPoint > 0) {
            enemyHealthBars.add(enemyHealthBar);
        }

    }

    /**
     * Removes an enemy health bar.
     *
     * @return an arraylist of enemy health bar as an HBox;
     */
    public ArrayList<HBox> getEnemyHealthBars() {
        return enemyHealthBars;
    }

    /**
     * Clears all enemy health bars.
     */
    public void clearEnemyHealthBars() {
        enemyHealthBars.clear();
    }

    /**
     * Refreshes the melee slot.
     *
     * @param isSword whether the melee weapon is a sword as a boolean
     * @throws IOException if the image path is invalid
     */
    public void refreshMeleeSlot(final boolean isSword) throws IOException {
        if (isSword) {
            refreshBackpackSlot(meleeSlot, meleeIcon, "ui/sword.png", "Sword");
        } else {
            refreshBackpackSlot(meleeSlot, meleeIcon, "ui/punch.png", "Fist");
        }
    }

    /**
     * Refreshes the range slot.
     *
     * @param isBow whether the range weapon is a bow as a boolean
     * @throws IOException if the image path is invalid
     */
    public void refreshRangeSlot(final boolean isBow) throws IOException {
        if (isBow) {
            refreshBackpackSlot(rangeSlot, rangeIcon, "ui/bow.png", "Bow");
        } else {
            refreshBackpackSlot(rangeSlot, rangeIcon, "ui/backpack.png", "Empty");
        }
    }

    /**
     * Refreshes the ammo slot.
     *
     * @param ammoCount the ammo count as an int
     * @throws IOException if the image path is invalid
     */
    public void refreshAmmoSlot(final int ammoCount) throws IOException {
        if (ammoCount <= 0) {
            refreshBackpackSlot(ammoSlot, ammoIcon, "ui/backpack.png", "Empty");
        } else {
            final String text = String.format("%s", ammoCount);
            refreshBackpackSlot(ammoSlot, ammoIcon, "ui/ammo.png", text);
        }
    }

    /**
     * Refreshes the potion slot.
     *
     * @param potionCount the potion count as an int
     * @throws IOException if the image path is invalid
     */
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

    /* Initializes the backpack. */
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

    /**
     * Refreshes the battery counter.
     *
     * @param batteryCount the battery count as an int
     * @throws IOException if the image path is invalid
     */
    public final void refreshBatteryCounter(final int batteryCount) throws IOException {
        final double translateY = 20;
        final double translateX = MainApplication.WINDOW_WIDTH - 200;
        final int fontSize = 24;
        final int textTranslateY = 18;
        final int textTranslateX = 10;
        batteryCounter.getChildren().clear();
        setPictureContent(batteryCounter, "ui/battery.png");
        setTextContent(batteryCounter, fontSize, String.format("x %s", batteryCount),
                textTranslateY, textTranslateX);
        batteryCounter.setTranslateX(translateX);
        batteryCounter.setTranslateY(translateY);
    }

    /**
     * Refreshes the player status.
     *
     * @throws IOException if the image path is invalid
     */
    public final void refreshPlayerStatus() throws IOException {
        final double translateY = 80;
        final double translateX = 50;
        final int fontSize = 18;
        final int textTranslateY = 0;
        final int textTranslateX = 10;
        playerStatus.getChildren().clear();
        setTextContent(playerStatus, fontSize, "HP", textTranslateY, textTranslateX);
        playerStatus.getChildren().add(healthLabel);
        playerStatus.getChildren().add(healthBar);
        playerStatus.setTranslateX(translateX);
        playerStatus.setTranslateY(translateY);
    }

    /**
     * Refreshes the player status.
     *
     * @throws IOException if the image path is invalid
     */
    public final void refreshBossStatus() throws IOException {
        final double translateY = MainApplication.WINDOW_HEIGHT - 50;
        final double translateX = 50;
        final int fontSize = 18;
        final int textTranslateY = 0;
        final int textTranslateX = 10;
        bossStatus.getChildren().clear();
        setTextContent(bossStatus, fontSize, "HAL", textTranslateY, textTranslateX);
        bossStatus.getChildren().add(bossHealthBar);
        bossStatus.setTranslateX(translateX);
        bossStatus.setTranslateY(translateY);
    }

    /**
     * Refreshes the world name.
     *
     * @param level the level as an int
     * @throws IOException if the image path is invalid
     */
    public final void refreshWorldName(final int level) throws IOException {
        final double translateY = 20;
        final double translateX = 50;
        final int fontSize = 18;
        final int textTranslateY = 12;
        final int textTranslateX = 10;
        String[] world = {"In the Past", "Present Day", "In the Future", "Boss Dimension"};
        worldName.getChildren().clear();
        setPictureContent(worldName, "ui/world.png");
        setTextContent(worldName, fontSize, String.format("Timeline - %s", world[level]),
                textTranslateY, textTranslateX);
        worldName.setTranslateX(translateX);
        worldName.setTranslateY(translateY);
    }

    /* Sets the default font.
     * @param the font size
     * @return the font
     * @throws IOException if the font path is invalid
     */
    private Font setDefaultFont(final int fontSize) throws IOException {
        Font font = Font.font("Verdana", FontWeight.BOLD, fontSize);
        InputStream fontStream = getClass().getResourceAsStream("BungeeSpice-Regular.ttf");

        if (fontStream != null) {
            font = Font.loadFont(fontStream, fontSize);
            fontStream.close();
        }

        return font;
    }

    /* Sets the text content of a Pane.
     * @param the Pane to set the text content
     * @param the font size
     * @param the text to set
     * @param the font translate Y property
     * @param the font translate X property
     * @throws IOException if the font path is invalid
     */
    private void setTextContent(final Pane box, final int fontSize, final String text,
                                final double fontTranslateY, final double fontTranslateX) throws IOException {
        Text description = new Text(text);
        description.setFont(this.setDefaultFont(fontSize));
        description.setFill(Color.WHITE);
        description.setTranslateY(fontTranslateY);
        description.setTranslateX(fontTranslateX);

        box.getChildren().add(description);
    }

    /* Sets the picture content of a Pane.
     * @param the Pane to set the picture content
     * @param the image path
     * @throws IOException if the image path is invalid
     */
    private void setPictureContent(final Pane box, final String imagePath) {
        ImageView imageView = new ImageView(new Image(String.valueOf(MainApplication.class.getResource(imagePath))));
        box.getChildren().add(imageView);
    }
    /* Initializes the health bar. */
    private void initialHealthBar() throws IOException {
        final int width = 240;
        final int height = 20;
        final int fontSize = 13;
        final int barXPosition = 30;
        final int labelXPosition = 20;
        final int labelYPosition = 3;
        healthBar.setPrefWidth(width);
        healthBar.setPrefHeight(height);
        healthBar.setTranslateX(barXPosition);
        healthLabel.setFont(this.setDefaultFont(fontSize));
        healthLabel.setStyle("-fx-text-fill: white;");
        healthLabel.setTranslateX(labelXPosition);
        healthLabel.setTranslateY(labelYPosition);
    }

    /* Initializes the boss health bar. */
    private void initialBossHealthBar() {
        final int width = 500;
        final int height = 20;
        final int barXPosition = 30;
        bossHealthBar.setPrefWidth(width);
        bossHealthBar.setPrefHeight(height);
        bossHealthBar.setTranslateX(barXPosition);
    }

    /**
     * Refreshes the boss health bar.
     *
     * @param progress the progress of the boss health bar as a double
     */
    public void refreshBossHealthBar(final double progress) {
        healthBarDefaultSetting(progress, bossHealthBar);
    }

    /**
     * Refreshes the health bar.
     *
     * @param currentHP the current health point as a double
     * @param maxHP the max health point as a double
     */
    public void refreshHealthBar(final double currentHP, final double maxHP) {
        final double progress = currentHP / maxHP;
        healthBarDefaultSetting(progress, healthBar);
        healthLabel.setText(String.format("%.0f / %.0f", currentHP, maxHP));
    }

    /* Sets the default setting of a health bar.*/
    private void healthBarDefaultSetting(final double progress, final ProgressBar targetHealthBar) {
        final String color;
        final double inWarningHealthPoint = 0.5;
        final double inDangerHealthPoint = 0.25;
        targetHealthBar.setProgress(progress);
        if (progress > inWarningHealthPoint) {
            color = "-fx-accent: lightgreen;";
        } else if (progress > inDangerHealthPoint) {
            color = "-fx-accent: yellow;";
        } else {
            color = "-fx-accent: red;";
        }
        targetHealthBar.setStyle(color);
    }

    /**
     * Gets the melee slot.
     *
     * @return the melee slot as a VBox
     */
    public HBox getWorldName() {
        return worldName;
    }
    /**
     * Gets the melee slot.
     *
     * @return the melee slot as a VBox
     */
    public HBox getBatteryCounter() {
        return batteryCounter;
    }

    /**
     * Gets the melee slot.
     *
     * @return the melee slot as a VBox
     */
    public HBox getPlayerStatus() {
        return playerStatus;
    }

    /**
     * Gets the melee slot.
     *
     * @return the melee slot as a VBox
     */
    public HBox getBackpack() {
        return backpack;
    }

    /**
     * Get the boss status.
     * @return the boss status as a HBox
     */
    public HBox getBossStatus() {
        return bossStatus;
    }
}
