package ca.bcit.comp2522.termproject.pix.model.levelmanager;

import java.util.Arrays;

/**
 * Manages the game's platform level.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public class LevelManager {
    // The current game level.4
    private static final int BOSS_LEVEL = 3;
    private int currentLevel;

    // The current level layout.
    private String[] currentLevelLayout;

    /**
     * Constructs a LevelManager with a specific current level.
     * @param currentLevel the current level
     */
    public LevelManager(final int currentLevel) {
        this.currentLevel = currentLevel;
        this.currentLevelLayout = LevelLayout.getLevelData(this.currentLevel);
    }

    /**
     * Gets the current level.
     *
     * @return the current level as an int
     */
    public int getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Gets the current level height.
     *
     * @return the current level height as an int
     */
    public int getCurrentLevelHeight() {
        return currentLevelLayout.length;
    }

    /**
     * Gets the current level width.
     *
     * @return the current level width as an int
     */
    public int getCurrentLevelWidth() {
        return currentLevelLayout[0].length();
    }

    /**
     * Sets the current level to next available level.
     */
    public void nextLevel() {
        this.currentLevel = Math.abs(this.currentLevel + 1) % LevelLayout.getNumberOfLevels();
        this.currentLevelLayout = LevelLayout.getLevelData(this.currentLevel);
    }

    /**
     * Sets the current level to previous available level.
     */
    public void previousLevel() {
        this.currentLevel = Math.abs(this.currentLevel + 2) % LevelLayout.getNumberOfLevels();
        this.currentLevelLayout = LevelLayout.getLevelData(this.currentLevel);
    }

    /**
     * Sets the current level to boss level.
     *
     */
    public void enterBossLevel() {
        this.currentLevel = BOSS_LEVEL;
        this.currentLevelLayout = LevelLayout.getBossLevelData();
    }

    /**
     * Checks if this LevelManager is equal to another object.
     *
     * @param o the object to compare to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LevelManager that = (LevelManager) o;

        if (currentLevel != that.currentLevel) {
            return false;
        }
        return Arrays.equals(currentLevelLayout, that.currentLevelLayout);
    }

    /**
     * Gets the hashcode of this LevelManager.
     *
     * @return the hashcode of this LevelManager as an int
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = currentLevel;
        result = prime * result + Arrays.hashCode(currentLevelLayout);
        return result;
    }

    /**
     * Gets the string representation of this LevelManager.
     *
     * @return the string representation of this LevelManager as a String
     */
    @Override
    public String toString() {
        return "LevelManager{"
                + "currentLevel="
                + currentLevel
                + ", currentLevelLayout="
                + Arrays.toString(currentLevelLayout)
                + '}';
    }
}
