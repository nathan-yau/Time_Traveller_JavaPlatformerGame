package ca.bcit.comp2522.termproject.pix.model.weapon;

import javafx.animation.PauseTransition;
import javafx.util.Duration;

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
    private static final double COOL_DOWN_TIME = 1.5;
    private final WeaponType weaponType;
    private final int damage;
    private final double hitRate;
    private WeaponState weaponState;

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
        this.weaponState = WeaponState.AVAILABLE;
    }

    // What happens when a weapon is used.
    protected abstract void onUse();

    /**
     * Uses the weapon.
     *
     * @return the damage as an int
     */
    public int useWeapon() {
        this.setWeaponState(WeaponState.IN_USE);
        onUse();

        PauseTransition pause = new PauseTransition(Duration.seconds(COOL_DOWN_TIME));
        pause.setOnFinished(event -> {
            if (this.getWeaponState() == WeaponState.IN_USE) {
                this.setWeaponState(WeaponState.AVAILABLE);
            }
        });

        pause.play();
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
    public WeaponState getWeaponState() {
        return this.weaponState;
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
     * @param weaponState the weapon's current state as a WeaponState
     */
    public void setWeaponState(final WeaponState weaponState) {
        this.weaponState = weaponState;
    }
}
