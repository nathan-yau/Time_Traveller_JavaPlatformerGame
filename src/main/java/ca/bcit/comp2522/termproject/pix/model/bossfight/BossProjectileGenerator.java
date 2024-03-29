package ca.bcit.comp2522.termproject.pix.model.bossfight;

import java.util.ArrayList;
import java.util.Random;

/**
 * Represents a boss fight.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-12
 */
public class BossProjectileGenerator {
    private static final Random RANDOM_GENERATOR = new Random();
    private final int maxProjectiles;
    private final int levelWidth;
    private final int levelHeight;
    private final int blockWidth;
    private final ArrayList<BossWeapon> projectiles = new ArrayList<>();

    /**
     * Constructs a BossFight.
     *
     * @param maxProjectiles the maximum number of projectiles as an int
     * @param levelWidth the level width as an int
     * @param levelHeight the level height as an int
     * @param blockWidth the block width as an int
     */
    public BossProjectileGenerator(final int maxProjectiles, final int levelWidth,
                                   final int levelHeight, final int blockWidth) {
        this.maxProjectiles = maxProjectiles;
        this.levelWidth = levelWidth;
        this.levelHeight = levelHeight;
        this.blockWidth = blockWidth;
    }

    /*
     * Picks a random column to spawn the projectiles.
     *
     * @return the random column as an int
     */
    private int pickRandomColumn() {
        return RANDOM_GENERATOR.nextInt(0, this.levelWidth);
    }

    /*
     * Generate projectiles and store them in an ArrayList.
     */
    private void generateProjectiles() {
        this.projectiles.clear();
        final int groundOffset = 75;
        for (int i = 0; i < this.maxProjectiles; i++) {
            int x = pickRandomColumn();
            int y = 0;
            Laser laser = new Laser(x, y, blockWidth, levelHeight - groundOffset);
            this.projectiles.add(laser);
        }
    }

    /**
     * Fire projectiles.
     *
     * @return the projectiles as an ArrayList
     */
    public ArrayList<BossWeapon> fireProjectiles() {
        generateProjectiles();
        return this.projectiles;
    }

    /**
     * Gets the projectiles.
     *
     * @return the projectiles as an ArrayList
     */
    public ArrayList<BossWeapon> getProjectiles() {
        return this.projectiles;
    }
}
