package ca.bcit.comp2522.termproject.pix.model.AttackEffect;

import ca.bcit.comp2522.termproject.pix.model.GameObject;
import ca.bcit.comp2522.termproject.pix.model.ObjectType;

import java.util.concurrent.CompletableFuture;

/**
 * Represents an object effect.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public abstract class ObjectEffect  extends GameObject<EffectType> {
    /**
     * Constructs an ObjectEffect.
     *
     * @param x the x-coordinate of the effect as a double
     * @param y the y-coordinate of the effect as a double
     * @param w the width of the effect as a double
     * @param h the height of the effect as a double
     * @param type the type of the effect as an ObjectType
     * @param subtype the subtype of the effect as an EffectType
     * @param url the name of the image as a String
     */
    public ObjectEffect(final double x, final double y, final double w, final double h,
                        final ObjectType type, final EffectType subtype, final String url) {
        super(x, y, w, h, type, subtype, url);
    }

    /**
     * Starts the initial effect.
     *
     * @return a CompletableFuture of a boolean
     */
    public abstract CompletableFuture<Boolean> startInitialEffect();
    /**
     * Stops the initial effect.
     */
    public abstract void stopInitialEffect();
}
