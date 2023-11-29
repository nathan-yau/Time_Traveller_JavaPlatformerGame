package ca.bcit.comp2522.termproject.pix.model.Enemy;
/**
 * Represents a wraith object that extends from minion.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public class Wraith extends Minion {
    private static final int HEALTH_POINT = 4;
    private static final int ATTACK_POINT = 1;
    private static final int HEIGHTS = 84;
    private static final int WIDTHS = 59;
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
     * @param moveOnXAxis the direction of the Minotaur as a boolean
     */
    public Wraith(final int x, final int y, final boolean moveOnXAxis) {
        super(x - WIDTHS, y - HEIGHTS, WIDTHS, HEIGHTS, EnemyType.WRAITH, MOVING_RANGE, MOVING_DURATION,
                MOVING_FRAME, HURTING_DURATION, HURTING_FRAME, ATTACKING_DURATION, ATTACKING_FRAME,
                DYING_DURATION, DYING_FRAME, HEALTH_POINT, ATTACK_POINT, moveOnXAxis);
    }
}
