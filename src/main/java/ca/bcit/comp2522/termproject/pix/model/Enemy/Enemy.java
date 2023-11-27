package ca.bcit.comp2522.termproject.pix.model.Enemy;

import ca.bcit.comp2522.termproject.pix.model.Combative;
import ca.bcit.comp2522.termproject.pix.model.Damageable;
import ca.bcit.comp2522.termproject.pix.model.GameObject;
import ca.bcit.comp2522.termproject.pix.model.ObjectType;
import ca.bcit.comp2522.termproject.pix.model.player.Direction;

import java.util.concurrent.CompletableFuture;

/**
 * Represents an enemy object.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public abstract class Enemy extends GameObject<EnemyType> implements Combative, Damageable {
    private int healthPoint;
    private final String folderPath;
    private boolean attackEnable;
    private final int attackPoint;

    /**
     * Constructs an Enemy.
     *
     * @param x the x-coordinate of the enemy as an int
     * @param y the y-coordinate of the enemy as an int
     * @param width the width of the enemy as an int
     * @param height the height of the enemy as an int
     * @param type the type of the enemy as an ObjectType
     * @param name the name of the enemy as an EnemyType
     * @param healthPoint the health point of the enemy as an int
     * @param attackPoint the attack point of the enemy as an int
     */
    public Enemy(final int x, final int y, final int width, final int height,
                 final ObjectType type, final EnemyType name, final int healthPoint, final int attackPoint) {
        super(x, y, width, height, type, name,
                String.format("%s/%s/%s/%s_0.png", type.name(), name.name(), Direction.BACKWARD.name(), "Idle"));
        this.attackEnable = true;
        this.healthPoint = healthPoint;
        this.folderPath = String.format("%s/%s", type.name(), name.name());
        this.attackPoint = attackPoint;
    }

    /**
     * Enables the enemy's attack ability.
     * @param enabled the boolean value of the enemy's attack ability
     *
     */
    protected final void setAttackEnable(final boolean enabled) {
        this.attackEnable = enabled;
    }

    /**
     * Gets the enemy's current attack ability.
     *
     * @return the boolean value of the enemy's attack ability
     */
    protected final boolean getAttackEnable() {
        return attackEnable;
    }

    /**
     * Gets the folder path of the enemy.
     *
     * @return the folder path of the enemy as a String
     */
    protected final String getFolderPath() {
        return folderPath;
    }

    /**
     * Gets the health point of the enemy.
     *
     * @return the health point of the enemy as an int
     */
    @Override
    public int takeDamage(final int point) {
       this.healthPoint -= point;
       return this.healthPoint;
    }

    /**
     * Performs a dying animation.
     * @return a CompletableFuture of a boolean
     */
    public abstract CompletableFuture<Boolean> startDying();
}

