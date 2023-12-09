package ca.bcit.comp2522.termproject.pix.model.Enemy;

/**
 * Represents a minotaur object that extends from minion.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public class Spector extends Minion {
    private static final int HEALTH_POINT = 6;
    private static final int ATTACK_POINT = 2;
    private static final int HEIGHTS = 75;
    private static final int WIDTHS = 75;
    private static final int MOVING_RANGE = 100;
    private static final int MOVING_DURATION = 100;
    private static final int MOVING_FRAME = 11;
    private static final int HURTING_DURATION = 100;
    private static final int HURTING_FRAME = 11;
    private static final int ATTACKING_DURATION = 100;
    private static final int ATTACKING_FRAME = 11;
    private static final int DYING_DURATION = 50;
    private static final int DYING_FRAME = 14;

    /**
     * Constructs a Minotaur.
     *
     * @param x the x coordinate of the Minotaur as an int
     * @param y the y coordinate of the Minotaur as an int
     * @param moveOnXAxis the boolean value of whether the Minotaur moves on the x-axis
     */
    public Spector(final int x, final int y, final boolean moveOnXAxis) {
        super(x - WIDTHS, y - HEIGHTS, WIDTHS, HEIGHTS, EnemyType.SPECTOR, MOVING_RANGE, MOVING_DURATION,
                MOVING_FRAME, HURTING_DURATION, HURTING_FRAME, ATTACKING_DURATION, ATTACKING_FRAME,
                DYING_DURATION, DYING_FRAME, HEALTH_POINT, ATTACK_POINT, moveOnXAxis);
    }
}
