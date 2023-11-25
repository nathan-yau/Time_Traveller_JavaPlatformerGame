package ca.bcit.comp2522.termproject.pix.model.AttackEffect;

import ca.bcit.comp2522.termproject.pix.model.GameObject;
import ca.bcit.comp2522.termproject.pix.model.ObjectType;

import java.util.concurrent.CompletableFuture;

public abstract class AttackEffect extends GameObject<EffectType> {

    public AttackEffect(double x, double y, double w, double h, EffectType name, String imageName) {
        super(x, y, w, h, ObjectType.EFFECT, name,
                String.format("Effect/%s/%s_0.png", name.name(), imageName));
    }

    public abstract CompletableFuture<Boolean> startEffect() ;
}
