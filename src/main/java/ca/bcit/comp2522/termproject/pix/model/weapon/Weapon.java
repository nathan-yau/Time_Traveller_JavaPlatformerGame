package ca.bcit.comp2522.termproject.pix.model.weapon;

import java.util.Random;

/**
 * Represents a weapon.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public abstract class Weapon {
    /** Random generator. */
    protected static final Random RANDOM_GENERATOR = new Random();
    private final WeaponType weaponType;
    private final int damage;
    private final double hitRate;
    private boolean isUsable;

    /**
     * Constructs a Weapon.
     *
     * @param weaponType the weapon type as a WeaponType
     * @param damage the damage as an int
     * @param hitRate the hit rate as a double
     */
    public Weapon(final WeaponType weaponType, final int damage, final double hitRate) {
        this.weaponType = weaponType;
        this.damage = damage;
        this.hitRate = hitRate;
        this.isUsable = true;
    }

    /**
     * Uses the weapon.
     *
     * @return the damage as an int
     */
    public int onUse() {
        if (RANDOM_GENERATOR.nextDouble() <= hitRate) {
            return damage;
        } else {
            return 0;
        }
    }

    /**
     * Gets whether weapon is usable.
     *
     * @return whether weapon is usable as a boolean
     */
    public boolean getIsUsable() {
        return this.isUsable;
    }

    /**
     * Gets the weapon type.
     *
     * @return the weapon type as a WeaponType
     */
    public WeaponType getWeaponType() {
        return weaponType;
    }

    /**
     * Sets whether weapon is usable.
     *
     * @param isUsable whether weapon is usable as a boolean
     */
    public void setIsUsable(final boolean isUsable) {
        this.isUsable = isUsable;
    }
}
