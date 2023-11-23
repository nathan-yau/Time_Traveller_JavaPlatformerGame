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
            "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000.",
            ".......................................................................................................0.",
            ".......................................................................................................0.",
            ".......................................................................................................0.",
            ".......................................................................................................0.",
            ".......................................................................................................0.",
            ".......................................................................................................0.",
            ".......................................................................................................0.",
            ".......................................................................................................0.",
            ".......................................................................................................0.",
            ".......................................................................................................0.",
            ".......................................................................................................0.",
            ".......................................................................................................0.",
            ".......................................................................................................0.",
            ".......................................................................................................0.",
            ".......................................................................................................0.",
            ".......................................................................................................0.",
            ".......................................................................................................0.",
            ".......................................................................................................0.",
            ".......................................................................................................0.",
            ".......................................................................................................0.",
            ".......................................................................................................0.",
            ".......................................................................................................0.",
            ".......................................................................................................0.",
            ".......................................................................................................0.",
            ".......................................................................................................0.",
            ".......000000..........................................................................................0.",
            ".......................................................................................................0.",
            ".....0........0........................................................................................0.",
            "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000.",
    };

    // Level 2 layout.
    private static final String[] LEVEL2 = new String[] {
            ".........................................................................................................",
            ".........................................................................................................",
            ".........................................................................................................",
            ".........................................................................................................",
            ".........................................................................................................",
            ".........................................................................................................",
            ".........................................................................................................",
            ".........................................................................................................",
            ".........................................................................................................",
            ".........................................................................................................",
            ".........................................................................................................",
            ".........................................................................................................",
            "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
    };

    // Level 3 layout.
    private static final String[] LEVEL3 = new String[] {
            ".........................................................................................................",
            ".........................................................................................................",
            ".........................................................................................................",
            ".........................................................................................................",
            ".........................................................................................................",
            ".........................................................................................................",
            ".........................................................................................................",
            ".........................................................................................................",
            ".........................................................................................................",
            ".........................................................................................................",
            ".........................................................................................................",
            ".........................................................................................................",
            "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
    };

    // Private constructor since this class should not be instantiated.
    private LevelLayout() { }

    protected static String[] getLevelData(final int level) throws IllegalArgumentException {
        String[][] levelData = new String[][]{LEVEL1, LEVEL2, LEVEL3};
        if (0 >= level || level > levelData.length) {
            throw new IllegalArgumentException("Level does not exist!");
        }
        return levelData[level - 1];
    }

}
