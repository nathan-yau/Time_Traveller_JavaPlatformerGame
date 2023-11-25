package ca.bcit.comp2522.termproject.pix.model;

import ca.bcit.comp2522.termproject.pix.model.AttackEffect.AttackEffect;

/**
 * Represents an object that can attack.
 * This interface is used to implement the meleeAttack, rangeAttack, and getAttackDamage methods.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023
 */
public interface Combative {
    /**
     * Makes the object melee attack.
     */
    AttackEffect meleeAttack();

    /**
     * Makes the object range attack.
     */
    void rangeAttack();

    /**
     * Gets the attack damage created by the object.
     *
     * @return the attack damage created by the object
     */
    int getAttackDamage();
}

