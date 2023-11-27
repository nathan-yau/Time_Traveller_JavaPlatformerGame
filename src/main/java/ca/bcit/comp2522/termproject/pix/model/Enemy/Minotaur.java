package ca.bcit.comp2522.termproject.pix.model.Enemy;

public class Minotaur extends Minion {
    private static final int HEALTH_POINT = 2;
    private static final int ATTACK_POINT = 1;
    private static final int HEIGHTS = 75;
    private static final int WIDTHS = 90;
    private static final int MOVING_RANGE = 100;
    private static final int MOVING_DURATION = 100;
    private static final int MOVING_FRAME = 17;
    private static final int HURTING_DURATION = 100;
    private static final int HURTING_FRAME = 5;
    private static final int ATTACKING_DURATION = 100;
    private static final int ATTACKING_FRAME = 9;
    private static final int DYING_DURATION = 50;
    private static final int DYING_FRAME = 14;

    /**
     * Constructs a Minotaur.
     *
     * @param x the x coordinate of the Minotaur as an int
     * @param y the y coordinate of the Minotaur as an int
     */
    public Minotaur(final int x, final int y, final boolean moveOnXAxis) {
        super(x - WIDTHS, y - HEIGHTS, WIDTHS, HEIGHTS, EnemyType.MINOTAUR, MOVING_RANGE, MOVING_DURATION,
                MOVING_FRAME, HURTING_DURATION, HURTING_FRAME, ATTACKING_DURATION, ATTACKING_FRAME,
                DYING_DURATION, DYING_FRAME, HEALTH_POINT, ATTACK_POINT, moveOnXAxis);
    }
}
