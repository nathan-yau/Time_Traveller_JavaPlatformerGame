package ca.bcit.comp2522.termproject.pix.model.AttackEffect;

import ca.bcit.comp2522.termproject.pix.model.ObjectType;

/**
 * Represents an attack effect.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public abstract class AttackEffect extends ObjectEffect implements AttackInterface {

    /**
     * Constructs an AttackEffect.
     *
     * @param x the x-coordinate of the effect as a double
     * @param y the y-coordinate of the effect as a double
     * @param w the width of the effect as a double
     * @param h the height of the effect as a double
     * @param name the type of the effect as an EffectType
     * @param imageName the name of the image as a String
     */
    public AttackEffect(final double x, final double y, final double w, final double h,
                        final EffectType name, final String imageName) {
        super(x, y, w, h, ObjectType.EFFECT, name,
                String.format("Effect/%s/%s_0.png", name.name(), imageName));
    }
}
