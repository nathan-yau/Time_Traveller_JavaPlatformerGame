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
            "................................................................................................................................................",
            ".................................................................................................................................................",
            "................................................................................................................................................",
            "................................................................................................................................................",
            ".........................................000000000000...........................................................................................",
            ".........................................000000000000...........................................................................................",
            ".........................................000000000000...........................................................................................",
            ".........................................000000000000...................................................D.....0000..............................",
            ".........................................000000000000.....................................B..........000000...0000......................C...EE..",
            ".........................................000000000000..............................P..E..............000000...0000..........................PP..",
            ".....................................E...000000000000000........................000000000....................00000....................0000000000",
            "...................0000000000........E...000000000000000..................0000..000000000...................000000..................000000000000",
            "000000000000000....0000000000....00000000000000000000000...000000.....00000000..000000000............0000000000000.....0000000000...000000000000",
            "000000000000000....0000000000....00000000000000000000000...000000.....00000000..000000000............0000000000000.....0000000000000000000000000"
    };

    // Level 2 layout.
    private static final String[] LEVEL2 = new String[] {
            "................................................................................................................................................",
            "................................................................................................................................................",
            "................................................................................................................................................",
            "................................................................................................................................................",
            "................................................................................................................................................",
            ".................................................................................................................................................",
            "................................................................................................................................................",
            "........................................2......D................................................................................................",
            "........................................2000000000000...........................................................................................",
            "........................................2000000000000...........................................................................................",
            "........................................2000000000000..1111111..................................................................................",
            "........................................2000000000000..1111111...........................................P....0000..............................",
            "........................................2000000000000.............................C....E.............000000...0000............................P.",
            "........................................2000000000000................................S.E.............000000...0000.......................D....P.",
            "........................................2000000000000000...................H....000000000....................00000...............E....0000000000",
            "...................0000000000...........2000000000000000....M.............0000..000000000...................000000.........A........000000000000",
            "000000000000000....0000000000....00000000000000000000000...0000003333300000000..000000000............0000000000000.....0000000000...000000000000",
            "000000000000000....0000000000....00000000000000000000000...000000.....00000000..000000000............0000000000000.....0000000000000000000000000"
    };

    // Level 3 layout.
    private static final String[] LEVEL3 = new String[] {
            "................................................................................................................................................",
            "................................................................................................................................................",
            "................................................................................................................................................",
            "................................................................................................................................................",
            "................................................................................................................................................",
            ".................................................................................................................................................",
            "................................................................................................................................................",
            "................................................................................................................................................",
            ".........................................000000000000...........................................................................................",
            ".........................................000000000000....................................................................AHE....................",
            ".........................................000000000000..1111111.....................................................B.....EHA....................",
            ".........................................000000000000..1111111....................................R....A......0000..............................",
            ".........................................000000000000..........................................3333330000003330000......333333..................",
            ".........................................000000000000................................................000000AAA0000......................PP..S.F.",
            ".........................................000000000000000..B.....................000000000.............AAAAAAA00000....................0000000000",
            "...................0000000000............000000000000000...........H......0000..000000000111111.......AAAAAA000000........PP.....AAA000000000000",
            "000000000000000....0000000000....00000000000000000000000...0000003333300000000..000000000111111......0000000000000.....0000000000AAA000000000000",
            "000000000000000....0000000000....00000000000000000000000...000000.....00000000..000000000............0000000000000.....0000000000000000000000000"
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
            "................A...AA...A.A..0",
            "..M..R..A...A...A...AA...A.A..0",
            "0000000000000000000000000000000",
            "0000000000000000000000000000000"
    };


    private static final String[][] REG_LEVEL_DATA = new String[][]{LEVEL1, LEVEL2, LEVEL3};
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
        if (0 > level || level > REG_LEVEL_DATA.length) {
            throw new IllegalArgumentException("Level does not exist!");
        }
        return REG_LEVEL_DATA[level];
    }

    /**
     * Gets the boss level data.
     *
     * @return the boss level data as a String[]
     */
    public static String[] getBossLevelData() {
        return LEVEL4;
    }

    /**
     * Gets the number of levels.
     *
     * @return the number of levels as an int
     */
    public static int getNumberOfLevels() {
        return REG_LEVEL_DATA.length;
    }

    /**
     * Gets all level data.
     *
     * @throws IllegalArgumentException if the level does not exist
     * @return the level data as a String[][]
     */
    public static String[][] getRegLevelsData() throws IllegalArgumentException {
        return REG_LEVEL_DATA;
    }

}
