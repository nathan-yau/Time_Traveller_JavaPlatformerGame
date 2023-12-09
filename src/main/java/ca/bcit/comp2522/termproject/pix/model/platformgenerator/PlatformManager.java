package ca.bcit.comp2522.termproject.pix.model.platformgenerator;

import ca.bcit.comp2522.termproject.pix.model.Enemy.Robot;
import ca.bcit.comp2522.termproject.pix.model.Enemy.Enemy;
import ca.bcit.comp2522.termproject.pix.model.Enemy.Knight;
import ca.bcit.comp2522.termproject.pix.model.Enemy.Minion;
import ca.bcit.comp2522.termproject.pix.model.Enemy.Minotaur;
import ca.bcit.comp2522.termproject.pix.model.Enemy.Spector;
import ca.bcit.comp2522.termproject.pix.model.Enemy.SpaceShip;
import ca.bcit.comp2522.termproject.pix.model.Enemy.Wraith;
import ca.bcit.comp2522.termproject.pix.model.block.BlockType;
import ca.bcit.comp2522.termproject.pix.model.block.MovingBlock;
import ca.bcit.comp2522.termproject.pix.model.block.StandardBlock;
import ca.bcit.comp2522.termproject.pix.model.levelmanager.LevelLayout;
import ca.bcit.comp2522.termproject.pix.model.levelmanager.LevelManager;
import ca.bcit.comp2522.termproject.pix.model.pickupitem.Energy;
import ca.bcit.comp2522.termproject.pix.model.pickupitem.HealthPotion;
import ca.bcit.comp2522.termproject.pix.model.pickupitem.PickUpItem;
import ca.bcit.comp2522.termproject.pix.model.pickupitem.WeaponPickup;
import ca.bcit.comp2522.termproject.pix.model.pickupitem.SaveEvent;
import ca.bcit.comp2522.termproject.pix.model.pickupitem.BossEvent;
import ca.bcit.comp2522.termproject.pix.model.pickupitem.PickUpItemType;
import ca.bcit.comp2522.termproject.pix.model.pickupitem.Ammo;


import java.util.ArrayList;
import java.util.Arrays;

/**
 * Manages the components of the game platform.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public class PlatformManager {
    /**
     * The default width of a block.
     */
    public static final int BLOCK_WIDTH = 50;
    /**
     * The default height of a block.
     */
    public static final int BLOCK_HEIGHT = 40;

    private final ArrayList<ArrayList<StandardBlock>> totalBlockArray;
    private final ArrayList<ArrayList<Enemy>> totalEnemyArray;
    private final ArrayList<ArrayList<PickUpItem>> totalPickUpItemArray;
    // The array of blocks that make up the game platform.
    private ArrayList<StandardBlock> blockArray;

    // The array of pickup items.
    private ArrayList<PickUpItem> pickUpItemArray;

    // The array of enemies.
    private ArrayList<Enemy> enemyArray;

    // The width of the current level.
    private final int levelWidth;

    // The level manager.
    private final LevelManager levelManager;

    // The height of the current level.
    private final int levelHeight;

    /**
     * Constructs a PlatformManager.
     * @param levelController the current level manager
     * @param loadedBlocks the previously-loaded blocks as an ArrayList (can be empty)
     * @param loadedItems the previously-loaded pickup items as an ArrayList (can be empty)
     * @param loadedEnemies the previously-loaded enemies as an ArrayList (can be empty)
     */
    public PlatformManager(final LevelManager levelController, final ArrayList<ArrayList<StandardBlock>> loadedBlocks,
                           final ArrayList<ArrayList<PickUpItem>> loadedItems,
                           final ArrayList<ArrayList<Enemy>> loadedEnemies) {
        this.totalBlockArray = loadedBlocks;
        this.blockArray = new ArrayList<>();
        this.totalPickUpItemArray = loadedItems;
        this.pickUpItemArray = new ArrayList<>();
        this.enemyArray = new ArrayList<>();
        this.totalEnemyArray = loadedEnemies;
        this.levelManager = levelController;
        this.levelWidth = levelManager.getCurrentLevelWidth();
        this.levelHeight = levelManager.getCurrentLevelHeight();
    }

    /**
     * Gets the array of blocks that make up the game platform.
     *
     * @return the list of blocks as an ArrayList
     */
    public ArrayList<StandardBlock> getBlockArray() {
        return this.blockArray;
    }

    /**
     * Gets the array of blocks that make up the game platform on all levels.
     *
     * @return the list of blocks as an ArrayList
     */
    public ArrayList<ArrayList<StandardBlock>> getTotalBlockArray() {
        return this.totalBlockArray;
    }

    /**
     * Gets the array of pickup items on the game platform.
     *
     * @return the list of pickup items as an ArrayList
     */
    public ArrayList<PickUpItem> getItemArray() {
        return this.pickUpItemArray;
    }

    /**
     * Gets the array of pickup items on all levels.
     *
     * @return the list of pickup items as an ArrayList
     */
    public ArrayList<ArrayList<PickUpItem>> getTotalItemArray() {
        return this.totalPickUpItemArray;
    }

    /**
     * Gets the array of enemies on the game platform.
     *
     * @return the list of enemies as an ArrayList
     */
    public ArrayList<Enemy> getEnemyArray() {
        return enemyArray;
    }

    /**
     * Gets the array of enemies on all levels.
     *
     * @return the list of enemies as an ArrayList
     */
    public ArrayList<ArrayList<Enemy>> getTotalEnemyArray() {
        return this.totalEnemyArray;
    }

    /**
     * Creates the game platform.
     */
    public void createRegularLevels() {
        final String[][] allDimension = LevelLayout.getRegLevelsData();
        for (int level = 0; level < allDimension.length; level++) {
            this.createGamePlatform(allDimension[level], level);
        }
        blockArray = totalBlockArray.get(levelManager.getCurrentLevel());
        enemyArray = totalEnemyArray.get(levelManager.getCurrentLevel());
        pickUpItemArray = totalPickUpItemArray.get(levelManager.getCurrentLevel());
    }

    private void createGamePlatform(final String[] currentLevelData, final int currentLevel) {
        final int movingBlockYPadding = 5;
        totalBlockArray.add(new ArrayList<>());
        totalEnemyArray.add(new ArrayList<>());
        totalPickUpItemArray.add(new ArrayList<>());
        for (int row = 0; row < levelHeight; row++) {
            String line = currentLevelData[row];
            for (int col = 0; col < line.length(); col++) {
                final int xPosition = col * BLOCK_WIDTH;
                final int yPosition = row * BLOCK_HEIGHT;
                final int yPadding = 10;
                char categorySymbol = line.charAt(col);
                if (categorySymbol == '0') {
                    PlatformPosition imageIndex = getImageForPlatformBlock(row, col, currentLevelData, categorySymbol);
                    StandardBlock solidBlock = new StandardBlock(xPosition, yPosition, BLOCK_WIDTH, BLOCK_HEIGHT,
                            BlockType.SOLID_BLOCK, currentLevel, imageIndex.name());
                    totalBlockArray.get(currentLevel).add(solidBlock);
                } else if (categorySymbol == '1') {
                    PlatformPosition imageIndex = getImageForPlatformBlock(row, col, currentLevelData, categorySymbol);
                    StandardBlock movingBlock = new MovingBlock(xPosition, yPosition, BLOCK_WIDTH,
                            BLOCK_HEIGHT + movingBlockYPadding, currentLevel, imageIndex.name());
                    totalBlockArray.get(currentLevel).add(movingBlock);
                } else if (categorySymbol == '2') {
                    StandardBlock ladderBlock = new StandardBlock(xPosition, yPosition, BLOCK_WIDTH,
                            BLOCK_HEIGHT, BlockType.LADDERS, currentLevel, "ladder");
                    totalBlockArray.get(currentLevel).add(ladderBlock);
                } else if (categorySymbol == '3') {
                    StandardBlock disappearingBlock = new StandardBlock(xPosition, yPosition, BLOCK_WIDTH,
                            BLOCK_HEIGHT, BlockType.DISAPPEARING_BLOCK, currentLevel, "rope");
                    totalBlockArray.get(currentLevel).add(disappearingBlock);
                } else if (categorySymbol == 'P') {
                    HealthPotion healthPotion = new HealthPotion(xPosition, yPosition - yPadding, BLOCK_WIDTH,
                            BLOCK_HEIGHT + yPadding);
                    totalPickUpItemArray.get(currentLevel).add(healthPotion);
                } else if (categorySymbol == 'E') {
                    Energy battery = new Energy(xPosition, yPosition - yPadding, BLOCK_WIDTH,
                            BLOCK_HEIGHT + yPadding);
                    totalPickUpItemArray.get(currentLevel).add(battery);
                } else if (categorySymbol == 'D') {
                    Minion minion = determineMinionType(xPosition + BLOCK_WIDTH,
                            yPosition + BLOCK_HEIGHT, currentLevel, true, false);
                    totalEnemyArray.get(currentLevel).add(minion);
                    Thread minionThread = new Thread(minion);
                    minionThread.setDaemon(true);
                    minionThread.start();
                } else if (categorySymbol == 'B') {
                    Minion minion = determineMinionType(xPosition + BLOCK_WIDTH,
                            yPosition + BLOCK_HEIGHT, currentLevel, false, true);
                    totalEnemyArray.get(currentLevel).add(minion);
                    Thread minionThread = new Thread(minion);
                    minionThread.setDaemon(true);
                    minionThread.start();
                } else if (categorySymbol == 'C') {
                    Minion minion = determineMinionType(xPosition + BLOCK_WIDTH,
                            yPosition + BLOCK_HEIGHT, currentLevel, true, true);
                    totalEnemyArray.get(currentLevel).add(minion);
                    Thread minionThread = new Thread(minion);
                    minionThread.setDaemon(true);
                    minionThread.start();
                } else if (categorySymbol == 'M') {
                    WeaponPickup weapon = new WeaponPickup(xPosition, yPosition - yPadding, BLOCK_WIDTH,
                            BLOCK_HEIGHT + yPadding, PickUpItemType.MELEE_WEAPON);
                    totalPickUpItemArray.get(currentLevel).add(weapon);
                } else if (categorySymbol == 'R') {
                    WeaponPickup weapon = new WeaponPickup(xPosition, yPosition, BLOCK_WIDTH,
                            BLOCK_HEIGHT, PickUpItemType.RANGE_WEAPON);
                    totalPickUpItemArray.get(currentLevel).add(weapon);
                } else if (categorySymbol == 'S') {
                    SaveEvent saveItem = new SaveEvent(xPosition, yPosition, BLOCK_WIDTH,
                            BLOCK_HEIGHT);
                    totalPickUpItemArray.get(currentLevel).add(saveItem);
                } else if (categorySymbol == 'F') {
                    BossEvent bossItem = new BossEvent(xPosition, yPosition - yPadding, BLOCK_WIDTH,
                            BLOCK_HEIGHT + yPadding);
                    totalPickUpItemArray.get(currentLevel).add(bossItem);
                } else if (categorySymbol == 'A') {
                    Ammo ammo = new Ammo(xPosition, yPosition - yPadding, BLOCK_WIDTH,
                            BLOCK_HEIGHT + yPadding);
                    totalPickUpItemArray.get(currentLevel).add(ammo);
                }
            }
        }
        blockArray = totalBlockArray.get(levelManager.getCurrentLevel());
        enemyArray = totalEnemyArray.get(levelManager.getCurrentLevel());
        pickUpItemArray = totalPickUpItemArray.get(levelManager.getCurrentLevel());
    }

    /**
     * Sets the next level arrays.
     *
     * @param level the level to set as an int
     */
    public void setLevelArrays(final int level) {
        blockArray = totalBlockArray.get(level);
        enemyArray = totalEnemyArray.get(level);
        pickUpItemArray = totalPickUpItemArray.get(level);
    }

    // Determines the type of minion.
    private Minion determineMinionType(final int xAxis, final int yAxis,
                                       final int currentLevel, final boolean xWalker, final boolean airWalker) {
        if (currentLevel < 0 || currentLevel > 2) {
            throw new IllegalArgumentException("Invalid level");
        }
        Minion minion;
        if (airWalker) {
            minion = switch (currentLevel) {
                case 0 -> new Wraith(xAxis, yAxis, xWalker);
                case 1 -> new Spector(xAxis, yAxis, xWalker);
                case 2 -> new SpaceShip(xAxis, yAxis, xWalker);
                default -> throw new IllegalArgumentException("Invalid level");
            };
        } else {
            minion = switch (currentLevel) {
                case 0 -> new Minotaur(xAxis, yAxis, true);
                case 1 -> new Knight(xAxis, yAxis, true);
                case 2 -> new Robot(xAxis, yAxis, true);
                default -> throw new IllegalArgumentException("Invalid level");
            };
        }
        return minion;
    }

    /**
     * Gets the width of the current level.
     *
     * @return the width of the current level as an int
     */
    public int getTotalLevelWidth() {
        return levelWidth * BLOCK_WIDTH;
    }

    // Gets the images for the first and last column of a block.
    private PlatformPosition firstAndLastColumn(final int row, final int col, final String[] level,
                               final char categorySymbol, final int numCols) {
        if (!(level[row - 1].charAt(col) == categorySymbol)) {
            if (col == numCols - 1) {
                return PlatformPosition.FLOOR_RIGHT_CORNER;
            }
            return PlatformPosition.FLOOR_LEFT_CORNER;
        }
        if (!(level[row - 1].charAt(numCols - 1) == categorySymbol)) {
            return PlatformPosition.FLOOR_RIGHT_CORNER;
        }
        if (col == 0) {
            return PlatformPosition.LEFT_EDGE;
        }
        return PlatformPosition.RIGHT_EDGE;
    }
    // Gets the images for the gaming area of a block.
    private PlatformPosition gamingArea(final Boolean[][] positions) {
        boolean topRowFilled = Arrays.stream(positions[0]).allMatch(value -> value);
        boolean bottomRowFilled = Arrays.stream(positions[2]).allMatch(value -> value);
        boolean middleColFilled = positions[0][1] & positions[1][1] & positions[2][1];
        boolean leftColFilled = positions[0][0] & positions[1][0] & positions[2][0];
        boolean rightColFilled = positions[0][2] & positions[1][2] & positions[2][2];

        if (!positions[0][0] & !positions[1][0]
                & !positions[0][2] & !positions[1][2] & !positions[0][1] & !positions[2][1]) {
            return PlatformPosition.BLOCK;
        }


        if (!positions[0][0] & !positions[1][0]
                & !positions[0][2] & !positions[1][2] & middleColFilled) {
            return PlatformPosition.WALL;
        }

        if (!positions[0][0] & !positions[1][0]
                & !positions[0][2] & !positions[1][2] & !positions[0][1] & positions[2][1]) {
            return PlatformPosition.WALL_TOP;
        }

        if (!positions[0][0] & !positions[0][2] & middleColFilled) {
            return PlatformPosition.WALL_BOTTOM;
        }

        if (!positions[0][0] && positions[1][0] & positions[0][1]) {
            return PlatformPosition.DIRT_RIGHT_CORNER;
        }
        if (!positions[0][2] && positions[1][2] & positions[0][1]) {
            return PlatformPosition.DIRT_LEFT_CORNER;
        }

        if (rightColFilled & middleColFilled & !positions[1][0] & !positions[2][0]) {
            return PlatformPosition.LEFT_EDGE;
        }

        if (rightColFilled & middleColFilled & !positions[2][0]) {
            return PlatformPosition.DIRT_TURN_LEFT_CORNER;
        }

        if (leftColFilled & middleColFilled & !positions[1][2] & !positions[2][2]) {
            return PlatformPosition.RIGHT_EDGE;
        }

        if (leftColFilled & middleColFilled & !positions[2][2]) {
            return PlatformPosition.DIRT_TURN_UP_CORNER;
        }

        if (topRowFilled) {
            if (bottomRowFilled) {
                return PlatformPosition.DIRT;
            } else {
                return PlatformPosition.FLOOR_BOTTOM;
            }
        }

        if (middleColFilled & !leftColFilled & rightColFilled) {
            return PlatformPosition.LEFT_EDGE;
        }
        if (middleColFilled & leftColFilled & !rightColFilled) {
            return PlatformPosition.RIGHT_EDGE;
        }

        if (!positions[1][0] && positions[2][1]) {
            return PlatformPosition.FLOOR_LEFT_CORNER;
        }

        if (!positions[1][0] && positions[1][2]) {
            return PlatformPosition.FLOOR_BOTTOM_LEFT_CORNER;
        }

        if (!positions[1][2] && positions[2][1]) {
            return PlatformPosition.FLOOR_RIGHT_CORNER;
        }

        if (!leftColFilled && !bottomRowFilled) {
            return PlatformPosition.FLOOR_BOTTOM_RIGHT_CORNER;
        }

        return PlatformPosition.FLOOR;
    }
    // Gets the images for a block.
    private PlatformPosition getImageForPlatformBlock(final int row, final int col, final String[] level,
                                                      final char categorySymbol) {
        final int numRows = level.length;
        final int numCols = level[0].length();
        final boolean emptyCols = (col == 0 || col + 1 == numCols);
        final boolean floor = (row + 1 == numRows);

        if (row == 0) {
            return PlatformPosition.CEILING;
        }

        if (emptyCols) {
            return firstAndLastColumn(row, col, level, categorySymbol, numCols);
        }
        final int rows = 3;
        final int cols = 3;

        final Boolean[][] nearbyBlock = new Boolean[cols][rows];
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (floor & i == 1) {
                    nearbyBlock[i + 1][j + 1] = true;
                    continue;
                }
                nearbyBlock[i + 1][j + 1] = level[row + i].charAt(col + j) == categorySymbol;
            }
        }
        return gamingArea(nearbyBlock);
    }

    /**
     * Sets the next level arrays.
     *
     * @param level the level to set as an int
     */
    public void setNextLevelArrays(final int level) {
        blockArray = totalBlockArray.get(level);
        enemyArray = totalEnemyArray.get(level);
        pickUpItemArray = totalPickUpItemArray.get(level);
    }

    /**
     * Checks if the platform manager is equal to another object.
     *
     * @param o the other object
     * @return true if the game object is equal to the other object, false otherwise
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PlatformManager that = (PlatformManager) o;

        if (levelWidth != that.levelWidth) {
            return false;
        }
        if (levelHeight != that.levelHeight) {
            return false;
        }
        if (!blockArray.equals(that.blockArray)) {
            return false;
        }
        return levelManager.equals(that.levelManager);
    }

    /**
     * Gets the hashcode of the platform manager.
     *
     * @return the hashcode of the platform manager as an int
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = blockArray.hashCode();
        result = prime * result + levelManager.hashCode();
        result = prime * result + levelWidth;
        result = prime * result + levelHeight;
        return result;
    }

    /**
     * Gets the string representation of the platform manager.
     *
     * @return the string representation of the platform manager as a String
     */
    @Override
    public String toString() {
        return "PlatformManager{"
                + "blockArray="
                + blockArray
                + ", levelManager="
                + levelManager
                + ", levelWidth="
                + levelWidth
                + ", leveHeight="
                + levelHeight
                + '}';
    }

    /**
     * Creates the boss level.
     */
    public void createBossLevel() {
        this.createGamePlatform(LevelLayout.getBossLevelData(), levelManager.getCurrentLevel());
        blockArray = totalBlockArray.get(levelManager.getCurrentLevel());
        enemyArray = totalEnemyArray.get(levelManager.getCurrentLevel());
        pickUpItemArray = totalPickUpItemArray.get(levelManager.getCurrentLevel());
    }
}
