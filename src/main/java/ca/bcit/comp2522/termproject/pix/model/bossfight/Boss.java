package ca.bcit.comp2522.termproject.pix.model.bossfight;

import ca.bcit.comp2522.termproject.pix.model.AttackEffect.AttackEffect;
import ca.bcit.comp2522.termproject.pix.model.Enemy.Enemy;
import ca.bcit.comp2522.termproject.pix.model.Enemy.EnemyType;
import ca.bcit.comp2522.termproject.pix.model.ObjectType;

import java.util.concurrent.CompletableFuture;

/**
 * Represents a boss.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public class Boss extends Enemy {

    /**
     * Constructs a Boss.
     *
     * @param initialHealthPoints the initial health points of the boss as an int
     * @param x the x-coordinate of the boss as an int
     * @param y the y-coordinate of the boss as an int
     * @param width the width of the boss as an int
     * @param height the height of the boss as an int
     * @param subType the subtype of the boss as an EnemyType
     * @param attackPoint the attack point of the boss as an int
     */
    public Boss(final int initialHealthPoints, final int x, final int y, final int width, final int height,
                final EnemyType subType, final int attackPoint) {
        super(x, y, width, height, ObjectType.BOSS, subType, initialHealthPoints, attackPoint);
    }

    /**
     * Gets the melee attack effect of the boss.
     *
     * @return the melee attack effect of the boss as an AttackEffect
     */
    @Override
    public AttackEffect meleeAttack() {
        return null;
    }

    /**
     * Gets the range attack effect of the boss.
     *
     * @return the range attack effect of the boss as an AttackEffect
     */
    @Override
    public AttackEffect rangeAttack() {
        return null;
    }

    /**
     * Gets the attack damage of the boss.
     *
     * @return an int representing the attack damage of the boss
     */
    @Override
    public int getAttackDamage() {
        return 0;
    }

    /**
     * Process when the boss gets hurt.
     */
    @Override
    public void getHurt() {

    }

    /**
     * Process when the boss dies.
     *
     * @return a CompletableFuture of a boolean representing whether the boss is dead
     */
    @Override
    public CompletableFuture<Boolean> startDying() {
        return null;
    }

    @Override
    public void terminateAnimation() {

    }
}
