package ca.bcit.comp2522.termproject.pix.model.platformgenerator;

import ca.bcit.comp2522.termproject.pix.model.Enemy.Minion;
import ca.bcit.comp2522.termproject.pix.model.Enemy.Minotaur;
import ca.bcit.comp2522.termproject.pix.model.Enemy.Wraith;
import ca.bcit.comp2522.termproject.pix.model.Enemy.Enemy;
import ca.bcit.comp2522.termproject.pix.model.block.BlockType;
import ca.bcit.comp2522.termproject.pix.model.block.MovingBlock;
import ca.bcit.comp2522.termproject.pix.model.block.StandardBlock;
import ca.bcit.comp2522.termproject.pix.model.levelmanager.LevelManager;
import ca.bcit.comp2522.termproject.pix.model.pickupitem.GoldCoin;
import ca.bcit.comp2522.termproject.pix.model.pickupitem.HealthPotion;
import ca.bcit.comp2522.termproject.pix.model.pickupitem.PickUpItem;
import ca.bcit.comp2522.termproject.pix.model.pickupitem.PickUpItemType;
import ca.bcit.comp2522.termproject.pix.model.pickupitem.WeaponPickup;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

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

    // The array of blocks that make up the game platform.
    private final ArrayList<StandardBlock> blockArray;

    // The array of pickup items.
    private final ArrayList<PickUpItem> pickUpItemArray;

    // The array of enemies.
    private final ArrayList<Enemy> enemyArray;

    // The width of the current level.
    private final int levelWidth;

    // The level manager.
    private final LevelManager levelManager;

    // The height of the current level.
    private final int leveHeight;

    /**
     * Constructs a PlatformManager.
     * @param levelController the current level manager
     */
    public PlatformManager(final LevelManager levelController) {
        this.blockArray = new ArrayList<>();
        this.pickUpItemArray = new ArrayList<>();
        this.enemyArray = new ArrayList<>();
        this.levelManager = levelController;
        this.levelWidth = levelManager.getCurrentLevelWidth();
        this.leveHeight = levelManager.getCurrentLevelHeight();
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
     * Gets the array of pickup items on the game platform.
     *
     * @return the list of pickup items as an ArrayList
     */
    public ArrayList<PickUpItem> getItemArray() {
        return this.pickUpItemArray;
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
     * Creates the game platform.
     */
    public void createGamePlatform() {
        final int movingBlockYPadding = 5;
        String[] currentLevelData = levelManager.getCurrentLevelLayout();
        int currentLevel = levelManager.getCurrentLevel();
        for (int row = 0; row < leveHeight; row++) {
            String line = currentLevelData[row];
            for (int col = 0; col < line.length(); col++) {
                int xPosition = col * BLOCK_WIDTH;
                int yPosition = row * BLOCK_HEIGHT;
                char categorySymbol = line.charAt(col);
                if (categorySymbol == '0') {
                    PlatformPosition imageIndex = getImageForPlatformBlock(row, col, currentLevelData, categorySymbol);
                    StandardBlock solidBlock = new StandardBlock(xPosition, yPosition, BLOCK_WIDTH, BLOCK_HEIGHT,
                            BlockType.SOLID_BLOCK, currentLevel, imageIndex.name());
                    blockArray.add(solidBlock);
                } else if (categorySymbol == '1') {
                    PlatformPosition imageIndex = getImageForPlatformBlock(row, col, currentLevelData, categorySymbol);
                    StandardBlock movingBlock = new MovingBlock(xPosition, yPosition, BLOCK_WIDTH,
                            BLOCK_HEIGHT + movingBlockYPadding, currentLevel, imageIndex.name());
                    blockArray.add(movingBlock);
                } else if (categorySymbol == '2') {
                    StandardBlock decorationBlock = new StandardBlock(xPosition, yPosition, BLOCK_WIDTH,
                            BLOCK_HEIGHT, BlockType.LADDERS, currentLevel, "ladder");
                    blockArray.add(decorationBlock);
                } else if (categorySymbol == '3') {
                    StandardBlock decorationBlock = new StandardBlock(xPosition, yPosition, BLOCK_WIDTH,
                            BLOCK_HEIGHT, BlockType.DISAPPEARING_BLOCK, currentLevel, "rope");
                    blockArray.add(decorationBlock);
                } else if (categorySymbol == '9') {
                        StandardBlock decorationBlock = new StandardBlock(xPosition, yPosition, BLOCK_WIDTH,
                                BLOCK_HEIGHT, BlockType.TESTING_BLOCK, currentLevel, "dirt");
                        blockArray.add(decorationBlock);
                } else if (categorySymbol == 'P') {
                    final int yPadding = 10;
                    HealthPotion healthPotion = new HealthPotion(xPosition, yPosition - yPadding, BLOCK_WIDTH,
                            BLOCK_HEIGHT + yPadding, currentLevel, "healthPotion");
                    pickUpItemArray.add(healthPotion);
                } else if (categorySymbol == 'C') {
                    GoldCoin goldCoin = new GoldCoin(xPosition, yPosition, BLOCK_WIDTH,
                            BLOCK_HEIGHT, currentLevel, "goldCoin");
                    pickUpItemArray.add(goldCoin);
                } else if (categorySymbol == 'W') {
                    Minion minion = determineMinionType(xPosition + BLOCK_WIDTH,
                            yPosition + BLOCK_HEIGHT, currentLevel, true, false);
                    enemyArray.add(minion);
                    Thread minionThread = new Thread(minion);
                    minionThread.setDaemon(true);
                    minionThread.start();
                } else if (categorySymbol == 'F') {
                    Minion minion = determineMinionType(xPosition + BLOCK_WIDTH,
                            yPosition + BLOCK_HEIGHT, currentLevel, false, true);
                    enemyArray.add(minion);
                    Thread minionThread = new Thread(minion);
                    minionThread.setDaemon(true);
                    minionThread.start();
                } else if (categorySymbol == 'S') {
                    Minion minion = determineMinionType(xPosition + BLOCK_WIDTH,
                            yPosition + BLOCK_HEIGHT, currentLevel, true, true);
                    enemyArray.add(minion);
                    Thread minionThread = new Thread(minion);
                    minionThread.setDaemon(true);
                    minionThread.start();
                } else if (categorySymbol == 'M') {
                    final int yPadding = 10;
                    WeaponPickup weapon = new WeaponPickup(xPosition, yPosition - yPadding, BLOCK_WIDTH,
                            BLOCK_HEIGHT + yPadding, PickUpItemType.MELEE_WEAPON, currentLevel, "melee");
                    pickUpItemArray.add(weapon);
                } else if (categorySymbol == 'R') {
                    WeaponPickup weapon = new WeaponPickup(xPosition, yPosition, BLOCK_WIDTH,
                            BLOCK_HEIGHT, PickUpItemType.RANGE_WEAPON, currentLevel, "range");
                    pickUpItemArray.add(weapon);
                }
            }
        }
    }

    private Minion determineMinionType(final int xAxis, final int yAxis,
                                       final int currentLevel, final boolean xWalker, final boolean airWalker) {
        final int firstLevel = 1;
        final int secondLevel = 2;
        final int finalLevel = 3;
        if (currentLevel == firstLevel & xWalker) {
            if (airWalker) {
                return new Wraith(xAxis, yAxis, true);
            }
            return new Minotaur(xAxis, yAxis, true);
        } else if (currentLevel == firstLevel & !xWalker) {
            return new Wraith(xAxis, yAxis, false);
        } else if (currentLevel == secondLevel & xWalker) {
            if (airWalker) {
                return new Minotaur(xAxis, yAxis, true);
            }
            return new Minotaur(xAxis, yAxis, true);
        } else if (currentLevel == secondLevel & !xWalker) {
            return new Minotaur(xAxis, yAxis, false);
        } else if (currentLevel == finalLevel & xWalker) {
            if (airWalker) {
                return new Minotaur(xAxis, yAxis, true);
            }
            return new Minotaur(xAxis, yAxis, true);
        } else if (currentLevel == finalLevel & !xWalker) {
            return new Minotaur(xAxis, yAxis, false);
        } else {
            throw new IllegalArgumentException("Invalid level");
        }
    }

    /**
     * Gets the current level.
     *
     * @return the current level as an int
     */
    public int getCurrentLevel() {
        return levelManager.getCurrentLevel();
    }

    /**
     * Gets the height of the current level.
     *
     * @return the height of the current level as an int
     */
    public int getTotalLevelHeight() {
        return leveHeight * BLOCK_HEIGHT;
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
     * Clears all the arrays.
     */
    public void clearAllArray() {
        Iterator<StandardBlock> blockIterator = blockArray.iterator();
        while (blockIterator.hasNext()) {
            StandardBlock block = blockIterator.next();
            block.terminateAnimation();
            blockIterator.remove();
        }
        Iterator<Enemy> iterator = enemyArray.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            enemy.terminateAnimation();
            iterator.remove();
        }
        pickUpItemArray.clear();
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
        if (leveHeight != that.leveHeight) {
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
        result = prime * result + leveHeight;
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
                + leveHeight
                + '}';
    }
}
