package ca.bcit.comp2522.termproject.pix.model.bossfight;

/**
 * Represents a Laser object.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public class Laser extends BossWeapon {

    /**
     * Constructs a Laser.
     *
     * @param x the x coordinate of the Laser as an int
     * @param y the y coordinate of the Laser as an int
     * @param width the width of the Laser as an int
     * @param height the height of the Laser as an int
     */
    public Laser(final int x, final int y, final int width, final int height) {
        super(x, y, width, height, BossWeaponType.PROJECTILE, "redLaser");
    }
}
