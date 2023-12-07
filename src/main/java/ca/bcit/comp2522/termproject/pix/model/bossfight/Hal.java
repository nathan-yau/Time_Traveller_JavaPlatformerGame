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
    private static final int X_OFFSET = 750;
    private static final int Y_OFFSET = -250;
    private static final int WIDTH = 450;
    private static final int HEIGHT = 450;
    private static final int ATTACK_POINT = 15;
    private static final int ATTACK_DAMAGE = 3;

    /**
     * Constructs a Hal boss.
     *
     * @param y the y coordinate of Hal as an int
     */
    public Hal(final int y) {
        super(INITIAL_HEALTH_POINTS, X_OFFSET - (WIDTH / 2), y + Y_OFFSET, WIDTH, HEIGHT,
                EnemyType.HAL, ATTACK_POINT);
        this.setVisible(false);
    }

    /**
     * Gets the attack damage to Hal.
     *
     * @return an int representing the attack damage to Hal
     */
    @Override
    public int getAttackDamage() {
        return ATTACK_DAMAGE;
    }
}
