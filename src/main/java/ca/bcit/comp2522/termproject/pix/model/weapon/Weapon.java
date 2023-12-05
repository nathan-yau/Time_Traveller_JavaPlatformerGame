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
    private boolean weaponIsAvailable;

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
        this.weaponIsAvailable = true;
    }

    /**
     * Use a weapon.
     */
    public abstract void useWeapon();

    /**
     * Get the damage done from a weapon.
     *
     * @return the damage as an int
     */
    public int getWeaponDamage() {
        if (RANDOM_GENERATOR.nextDouble() <= hitRate) {
            return damage;
        } else {
            return 0;
        }
    }

    /**
     * Gets whether weapon is usable.
     *
     * @return the weapon's current state as a WeaponState
     */
    public boolean weaponIsAvailable() {
        return this.weaponIsAvailable;
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
     * @param weaponIsAvailable whether the weapon is available as a boolean
     */
    public void setWeaponIsAvailable(final boolean weaponIsAvailable) {
        this.weaponIsAvailable = weaponIsAvailable;
    }

    /**
     * Gets the ammo count.
     * @return the ammo count as an int
     */
    public abstract int getAmmoCount();
}
