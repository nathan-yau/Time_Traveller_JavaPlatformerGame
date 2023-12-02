package ca.bcit.comp2522.termproject.pix.model.bossfight;

import ca.bcit.comp2522.termproject.pix.model.GameObject;
import ca.bcit.comp2522.termproject.pix.model.ObjectType;

/**
 * Represents a boss weapon.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-12
 */
public class BossWeapon extends GameObject<BossWeaponType> {

    /**
     * Constructs a BossWeapon.
     *
     * @param x the x-coordinate of the boss weapon as an int
     * @param y the y-coordinate of the boss weapon as an int
     * @param width the width of the boss weapon as an int
     * @param height the height of the boss weapon as an int
     * @param bossWeaponType the type of the boss weapon as a BossWeaponType
     * @param imageName the name of the image of the boss weapon as a String
     */
    public BossWeapon(final int x, final int y, final int width, final int height, final BossWeaponType bossWeaponType,
                         final String imageName) {
        super(x, y, width, height, ObjectType.BOSS_WEAPON, bossWeaponType, String.format("Boss/BossFight/%s/%s.png",
                bossWeaponType.name(), imageName));
    }
}
