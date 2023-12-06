package ca.bcit.comp2522.termproject.pix.model.weapon;

import java.io.Serializable;

/**
 * Represents a melee weapon.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public class MeleeWeapon extends Weapon implements Serializable {
    private static final double MELEE_HIT_RATE = 0.8;
    /**
     * Constructs a Melee Weapon.
     *
     * @param currentLevel the current game level as an int
     */
    public MeleeWeapon(final int currentLevel) {
        super(WeaponType.MELEE_WEAPON, RANDOM_GENERATOR.nextInt(1, currentLevel + 2), MELEE_HIT_RATE);
    }

    /**
     * What happens when a melee weapon is used.
     */
    @Override
    public void useWeapon() { }

    /**
     * Gets the ammo count.
     *
     * @return the ammo count as an int
     */
    @Override
    public int getAmmoCount() {
        return 0;
    }

    @Override
    public void addAmmo(int newAmmo) { }

    /**
     * Gets the string representation of this MeleeWeapon.
     *
     * @return the string representation of this MeleeWeapon as a String
     */
    @Override
    public String toString() {
        return "MeleeWeapon";
    }
}
