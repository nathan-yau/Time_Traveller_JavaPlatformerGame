package ca.bcit.comp2522.termproject.pix.model.levelmanager;

/**
 * Stores the level layout data.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public final class LevelLayout {

    // Level 1 layout.
    private static final String[] LEVEL1 = new String[] {
            "................................................................................................................................................",
            "................................................................................................................................................",
            "................................................................................................................................................",
            "................................................................................................................................................",
            "..2.............................................................................................................................................",
            "..2000000........................................................................................................................................",
            "..2000000.......................................................................................................................................",
            "..2.................0...........................................................................................................................",
            "..2.................0.00000000000...............................................................................................................",
            "..2........S........0.00000000000...............................................................................................................",
            "..2.................0.00000000000...............................................................................................................",
            "..2.................0...........................................................................................................................",
            "..2.................0....................1111111111111..........................................................................................",
            "..2.....P.W.C.......0....................1111111111111..........................................................................................",
            "..2...000000000.....0.......M..R................................................................................................................",
            "..2...000000000.....0....33333333...............................................................................................................",
            "000000000000000333300000..........00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
            "000000000000000000000000..........00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
    };

    // Level 2 layout.
    private static final String[] LEVEL2 = new String[] {
            "................................................................................................................................................",
            "................................................................................................................................................",
            "................................................................................................................................................",
            "................................................................................................................................................",
            "..2.............................................................................................................................................",
            "..2000000........................................................................................................................................",
            "..2000000.......................................................................................................................................",
            "..2.............................................................................................................................................",
            "..2...................00000000000...............................................................................................................",
            "..2........S..........00000000000...............................................................................................................",
            "..2...................00000000000...............................................................................................................",
            "..2.............................................................................................................................................",
            "..2......................................1111111111111..........................................................................................",
            "..2.....P.W.C............................1111111111111..........................................................................................",
            "..2...000000000.................................................................................................................................",
            "..2...000000000.M..R.....33333333...............................................................................................................",
            "000000000000000333300000..........00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
            "000000000000000000000000..........00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
    };

    // Level 3 layout.
    private static final String[] LEVEL3 = new String[] {
            "................................................................................................................................................",
            "................................................................................................................................................",
            "................................................................................................................................................",
            "................................................................................................................................................",
            "..2.............................................................................................................................................",
            "..2000000........................................................................................................................................",
            "..2000000.......................................................................................................................................",
            "..2.............................................................................................................................................",
            "..2...................00000000000...............................................................................................................",
            "..2........S..........00000000000...............................................................................................................",
            "..2...................00000000000...............................................................................................................",
            "..2.............................................................................................................................................",
            "..2......................................1111111111111..........................................................................................",
            "..2.....P.W.C............................1111111111111..........................................................................................",
            "..2...000000000.................................................................................................................................",
            "..2...000000000.M..R.....33333333...............................................................................................................",
            "000000000000000333300000..........00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
            "000000000000000000000000..........00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
    };

    // Level 3 layout.
    private static final String[] LEVEL4 = new String[] {
            "..............................0",
            "..............................0",
            "..............................0",
            "..............................0",
            "..............................0",
            "..............................0",
            "..............................0",
            "..............................0",
            "..............................0",
            "..............................0",
            "..............................0",
            "..............................0",
            "..............................0",
            "..............................0",
            "..............................0",
            "..M..R........................0",
            "0000000000000000000000000000000",
            "0000000000000000000000000000000"
    };


    private static final String[][] LEVEL_DATA = new String[][]{LEVEL1, LEVEL2, LEVEL3};
    // Private constructor since this class should not be instantiated.
    private LevelLayout() { }

    /**
     * Gets the specific level data.
     *
     * @param level the level to get the data from
     * @throws IllegalArgumentException if the level does not exist
     * @return the level data as a String[]
     */
    public static String[] getLevelData(final int level) throws IllegalArgumentException {
        if (0 > level || level > LEVEL_DATA.length) {
            throw new IllegalArgumentException("Level does not exist!");
        }
        return LEVEL_DATA[level];
    }

    /**
     * Gets the number of levels.
     *
     * @return the number of levels as an int
     */
    public static int getNumberOfLevels() {
        return LEVEL_DATA.length;
    }

    /**
     * Gets all level data.
     *
     * @throws IllegalArgumentException if the level does not exist
     * @return the level data as a String[][]
     */
    public static String[][] getAllLevelsData() throws IllegalArgumentException {
        return LEVEL_DATA;
    }

}
