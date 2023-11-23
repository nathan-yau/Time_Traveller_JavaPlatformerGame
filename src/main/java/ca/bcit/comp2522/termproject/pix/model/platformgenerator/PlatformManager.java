package ca.bcit.comp2522.termproject.pix.model.platformgenerator;

import ca.bcit.comp2522.termproject.pix.model.block.BlockType;
import ca.bcit.comp2522.termproject.pix.model.block.MovingBlock;
import ca.bcit.comp2522.termproject.pix.model.block.StandardBlock;
import ca.bcit.comp2522.termproject.pix.model.levelmanager.LevelManager;

import java.util.ArrayList;
import java.util.HashMap;

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

    // The level manager.
    private final LevelManager levelManager;

    // The width of the current level.
    private final int levelWidth;

    // The height of the current level.
    private final int leveHeight;

    /**
     * Constructs a PlatformManager.
     */
    public PlatformManager() {
        this.blockArray = new ArrayList<>();
        this.levelManager = new LevelManager();
        this.levelWidth = levelManager.getCurrentLevelWidth();
        this.leveHeight = levelManager.getCurrentLevelHeight();
    }

    /**
     * Gets the array of blocks that make up the game platform.
     *
     * @return the list of blocks as an ArrayList
     */
    public ArrayList<StandardBlock> getBlockArray() {
        return blockArray;
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
                            BLOCK_HEIGHT, BlockType.DECORATION_BLOCK, currentLevel, "rock");
                    blockArray.add(decorationBlock);
                } else if (categorySymbol == '3') {
                    StandardBlock decorationBlock = new StandardBlock(xPosition, yPosition, BLOCK_WIDTH,
                            BLOCK_HEIGHT, BlockType.DISAPPEARING_BLOCK, currentLevel, "floor");
                    blockArray.add(decorationBlock);
                }
            }
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

    // Get the position of the block.
    private HashMap<String, Boolean> findPosition(final int row, final int col, final String[] level,
                                                  final char categorySymbol, final boolean ceiling,
                                                  final boolean floor) {
        HashMap<String, Boolean> blockPositions = new HashMap<>();
        if (!ceiling) {
            blockPositions.put("topLeft", level[row - 1].charAt(col - 1) == categorySymbol);
            blockPositions.put("topMid", level[row - 1].charAt(col) == categorySymbol);
            blockPositions.put("topRight", level[row - 1].charAt(col + 1) == categorySymbol);
        }
        blockPositions.put("midLeft", level[row].charAt(col - 1) == categorySymbol);
        blockPositions.put("midRight", level[row].charAt(col + 1) == categorySymbol);

        if (!floor) {
            blockPositions.put("bottomLeft", level[row + 1].charAt(col - 1) == categorySymbol);
            blockPositions.put("bottomMid", level[row + 1].charAt(col) == categorySymbol);
            blockPositions.put("bottomRight", level[row + 1].charAt(col + 1) == categorySymbol);
        }
        return blockPositions;
    }

    // Get the adjacent blocks.
    private HashMap<String, Boolean> findAdjacentBlocks(final int row, final int col, final String[] level,
                                                        final char categorySymbol, final boolean ceiling,
                                                        final boolean floor) {
        HashMap<String, Boolean> blockPositions = findPosition(row, col, level, categorySymbol, ceiling, floor);

        boolean topLeft = Boolean.TRUE.equals(blockPositions.put("topLeft", false));
        boolean topMid = Boolean.TRUE.equals(blockPositions.put("topMid", false));
        boolean topRight = Boolean.TRUE.equals(blockPositions.put("topRight", false));
        boolean midLeft = Boolean.TRUE.equals(blockPositions.put("midLeft", false));
        boolean midRight = Boolean.TRUE.equals(blockPositions.put("midRight", false));
        boolean bottomLeft = Boolean.TRUE.equals(blockPositions.put("bottomLeft", false));
        boolean bottomMid = Boolean.TRUE.equals(blockPositions.put("bottomMid", false));
        boolean bottomRight = Boolean.TRUE.equals(blockPositions.put("bottomRight", false));

        HashMap<String, Boolean> adjacentBlocks = new HashMap<>();
        adjacentBlocks.put("topMid", Boolean.TRUE.equals(blockPositions.put("topMid", false)));

        adjacentBlocks.put("noLeftBottomBlock", !bottomLeft & bottomMid & bottomRight);
        adjacentBlocks.put("noMiddleBottomBlock", bottomLeft & !bottomMid & bottomRight);
        adjacentBlocks.put("noRightBottomBlock", bottomLeft & bottomMid & !bottomRight);

        adjacentBlocks.put("onlyLeftBottomBlock", bottomLeft & !bottomMid & !bottomRight);
        adjacentBlocks.put("onlyMiddleBottomBlock", !bottomLeft & bottomMid & !bottomRight);
        adjacentBlocks.put("onlyRightBottomBlock", !bottomLeft & !bottomMid & bottomRight);

        adjacentBlocks.put("allRowBottomBlock", topLeft & topMid & topRight);
        adjacentBlocks.put("noBottomBlock", !bottomLeft & !bottomMid & !bottomRight);

        adjacentBlocks.put("allRowTopBlock", topLeft & topMid & topRight);
        adjacentBlocks.put("noTopBlock", !topLeft & !topMid & !topRight);

        adjacentBlocks.put("allRowMiddleBlock", midLeft & midRight);
        adjacentBlocks.put("noMiddleBlock", !midLeft & !midRight);

        adjacentBlocks.put("onlyLeftMiddleBlock", midLeft & !midRight);
        adjacentBlocks.put("onlyRightMiddleBlock", !midLeft & midRight);

        adjacentBlocks.put("noLeftTopBlock", !topLeft & topMid & topRight);
        adjacentBlocks.put("noMiddleTopBlock", topLeft & !topMid & topRight);
        adjacentBlocks.put("noRightTopBlock", topLeft & topMid & !topRight);

        adjacentBlocks.put("onlyLeftTopBlock", topLeft & !topMid & !topRight);
        adjacentBlocks.put("onlyMiddleTopBlock", !topLeft & topMid & !topRight);
        adjacentBlocks.put("onlyRightTopBlock", !topLeft & !topMid & topRight);

        return adjacentBlocks;
    }

    // Get the image for the block.
    private PlatformPosition getImageForPlatformBlock(final int row, final int col, final String[] level,
                                                      final char categorySymbol) {
        int numRows = level.length;
        int numCols = level[0].length();
        boolean intendedEmptyCol = (col == 0 || col + 1 == numCols);
        boolean ceiling = row == 0;
        boolean floor = row + 1 == numRows;

        if (!intendedEmptyCol) {
            HashMap<String, Boolean> adjacentBlocks =
                    findAdjacentBlocks(row, col, level, categorySymbol, ceiling, floor);

            boolean topMid = Boolean.TRUE.equals(adjacentBlocks.put("topMid", false));

            boolean noLeftBottomBlock = Boolean.TRUE.equals(adjacentBlocks.put("noLeftBottomBlock", false));
            boolean noMiddleBottomBlock = Boolean.TRUE.equals(adjacentBlocks.put("noMiddleBottomBlock", false));
            boolean noRightBottomBlock = Boolean.TRUE.equals(adjacentBlocks.put("noRightBottomBlock", false));

            boolean onlyLeftBottomBlock = Boolean.TRUE.equals(adjacentBlocks.put("onlyLeftBottomBlock", false));
            boolean onlyMiddleBottomBlock = Boolean.TRUE.equals(adjacentBlocks.put("onlyMiddleBottomBlock", false));
            boolean onlyRightBottomBlock = Boolean.TRUE.equals(adjacentBlocks.put("onlyRightBottomBlock", false));

            boolean allRowBottomBlock = Boolean.TRUE.equals(adjacentBlocks.put("allRowBottomBlock", false));
            boolean noBottomBlock = Boolean.TRUE.equals(adjacentBlocks.put("noBottomBlock", false));

            boolean allRowTopBlock = Boolean.TRUE.equals(adjacentBlocks.put("allRowTopBlock", false));
            boolean noTopBlock = Boolean.TRUE.equals(adjacentBlocks.put("noTopBlock", false));

            boolean allRowMiddleBlock = Boolean.TRUE.equals(adjacentBlocks.put("allRowMiddleBlock", false));
            boolean noMiddleBlock = Boolean.TRUE.equals(adjacentBlocks.put("noMiddleBlock", false));

            boolean onlyLeftMiddleBlock = Boolean.TRUE.equals(adjacentBlocks.put("onlyLeftMiddleBlock", false));
            boolean onlyRightMiddleBlock = Boolean.TRUE.equals(adjacentBlocks.put("onlyRightMiddleBlock", false));

            boolean noLeftTopBlock = Boolean.TRUE.equals(adjacentBlocks.put("noLeftTopBlock", false));
            boolean noMiddleTopBlock = Boolean.TRUE.equals(adjacentBlocks.put("noMiddleTopBlock", false));
            boolean noRightTopBlock = Boolean.TRUE.equals(adjacentBlocks.put("noRightTopBlock", false));

            boolean onlyLeftTopBlock = Boolean.TRUE.equals(adjacentBlocks.put("onlyLeftTopBlock", false));
            boolean onlyMiddleTopBlock = Boolean.TRUE.equals(adjacentBlocks.put("onlyMiddleTopBlock", false));
            boolean onlyRightTopBlock = Boolean.TRUE.equals(adjacentBlocks.put("onlyRightTopBlock", false));

            if (ceiling) {
                if (noLeftBottomBlock) {
                    if (allRowMiddleBlock) {
                        return PlatformPosition.DIRT_LEFT_CORNER;
                    } else {
                        return PlatformPosition.CEILING;
                    }
                } else if (allRowBottomBlock) {
                    return PlatformPosition.DIRT;
                } else if (noRightBottomBlock) {
                    return PlatformPosition.DIRT_RIGHT_CORNER;
                } else if (onlyMiddleBottomBlock) {
                    return PlatformPosition.WALL_TOP;
                } else {
                    return PlatformPosition.CEILING;
                }
            }

            if (floor) {
                if (noLeftTopBlock) {
                    if (allRowMiddleBlock) {
                        return PlatformPosition.DIRT_RIGHT_CORNER;
                    } else {
                        return PlatformPosition.FLOOR;
                    }
                } else if (allRowTopBlock) {
                    return PlatformPosition.DIRT;
                } else if (noRightTopBlock) {
                    return PlatformPosition.DIRT_LEFT_CORNER;
                } else if (onlyMiddleTopBlock) {
                    return PlatformPosition.WALL_BOTTOM;
                } else {
                    return PlatformPosition.FLOOR;
                }
            }

            if (!topMid) {
                if (onlyRightMiddleBlock) {
                    return PlatformPosition.FLOOR_LEFT_CORNER;
                } else if (onlyLeftMiddleBlock) {
                    return PlatformPosition.FLOOR_RIGHT_CORNER;
                } else if (allRowMiddleBlock) {
                    return PlatformPosition.FLOOR;
                } else {
                    return PlatformPosition.FLOOR;
                }
            } else {
                if (onlyRightMiddleBlock) {
                    if (noBottomBlock) {
                        return PlatformPosition.FLOOR_BOTTOM_LEFT_CORNER;
                    } else {
                        return PlatformPosition.LEFT_EDGE;
                    }
                } else if (onlyLeftMiddleBlock) {
                    if (noBottomBlock) {
                        return PlatformPosition.FLOOR_BOTTOM_RIGHT_CORNER;
                    } else {
                        return PlatformPosition.RIGHT_EDGE;
                    }
                } else if (allRowMiddleBlock) {
                    if (noBottomBlock || onlyRightBottomBlock) {
                        return PlatformPosition.FLOOR_BOTTOM;
                    } else if (noLeftBottomBlock) {
                        return PlatformPosition.DIRT_TURN_LEFT_CORNER;
                    } else if (noRightTopBlock) {
                        return PlatformPosition.DIRT_TURN_UP_CORNER;
                    } else {
                        return PlatformPosition.DIRT;
                    }
                } else {
                    return PlatformPosition.WALL;
                }
            }
        } else {
            if (ceiling) {
                return PlatformPosition.CEILING;
            } else if (floor) {
                return PlatformPosition.FLOOR;
            } else {
                return PlatformPosition.FLOOR_BOTTOM;
            }
        }
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