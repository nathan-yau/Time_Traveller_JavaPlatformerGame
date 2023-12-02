package ca.bcit.comp2522.termproject.pix.model.bossfight;

import ca.bcit.comp2522.termproject.pix.model.Enemy.EnemyType;

/**
 * Represents a Hal boss object.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public class Hal extends Boss {
    private static final int INITIAL_HEALTH_POINTS = 10;
    private static final int X_OFFSET = -120;
    private static final int Y_OFFSET = -150;
    private static final int WIDTH = 180;
    private static final int HEIGHT = 150;
    private static final int ATTACK_POINT = 10;
    /**
     * Constructs a Hal boss.
     *
     * @param x the x coordinate of Hal as an int
     * @param y the y coordinate of Hal as an int
     */
    public Hal(final int x, final int y) {
        super(INITIAL_HEALTH_POINTS, x + X_OFFSET, y + Y_OFFSET, WIDTH, HEIGHT, EnemyType.HAL, ATTACK_POINT);
    }
}
