package ca.bcit.comp2522.termproject.pix.model.weapon;

/**
 * Represents a range weapon.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public class RangeWeapon extends Weapon {
    private static final double RANGE_HIT_RATE = 0.4;
    private static final int MIN_AMMO_COUNT = 3;
    private static final int MAX_AMMO_COUNT = 11;
    private int ammoCount;

    /**
     * Constructs a Range Weapon.
     *
     * @param currentLevel the current game level as an int
     */
    public RangeWeapon(final int currentLevel) {
        super(WeaponType.RANGE_WEAPON, RANDOM_GENERATOR.nextInt(1, currentLevel + 2), RANGE_HIT_RATE);
        this.ammoCount = RANDOM_GENERATOR.nextInt(MIN_AMMO_COUNT, MAX_AMMO_COUNT);
    }

    /**
     * Gets the ammo count.
     *
     * @return the ammo count as an int
     */
    public int getAmmoCount() {
        return this.ammoCount;
    }

    /**
     * What happens when a range weapon is used.
     */
    @Override
    public void useWeapon() {
        this.decrementAmmoCount();
        if (this.ammoCount == 0) {
            this.setWeaponIsAvailable(false);
        }
    }

    // Decrement the ammo count.
    private void decrementAmmoCount() {
        this.ammoCount--;
    }

    /**
     * Checks if an object is equal to this RangeWeapon.
     *
     * @param o the object to compare
     * @return whether the object is equal to this RangeWeapon as a boolean
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RangeWeapon that = (RangeWeapon) o;

        return ammoCount == that.ammoCount;
    }

    /**
     * Gets the hash code of this RangeWeapon.
     *
     * @return the hash code of this RangeWeapon as an int
     */
    @Override
    public int hashCode() {
        return ammoCount;
    }

    /**
     * Gets the string representation of this RangeWeapon.
     *
     * @return the string representation of this RangeWeapon as a String
     */
    @Override
    public String toString() {
        return "RangeWeapon{"
                + "ammoCount=" + ammoCount
                + '}';
    }
}
