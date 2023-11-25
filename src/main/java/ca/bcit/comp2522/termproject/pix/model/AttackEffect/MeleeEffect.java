package ca.bcit.comp2522.termproject.pix.model.AttackEffect;

import ca.bcit.comp2522.termproject.pix.MainApplication;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.concurrent.CompletableFuture;

public class MeleeEffect extends AttackEffect {
    private final String imageName;
    private Timeline attackAnimation;

    public MeleeEffect(double x, double y, double w, double h, String imageName) {
        super(x, y, w, h, EffectType.MELEE_ATTACK, imageName);
        this.imageName = imageName;
        this.attackAnimation = null;
    }

    @Override
    public CompletableFuture<Boolean> startEffect() {
        final int[] meleeFrame = {0};
        CompletableFuture<Boolean> completionFuture = new CompletableFuture<>();
        attackAnimation = new Timeline(
                new KeyFrame(Duration.millis(100), event -> {
                    final String sequenceIdle = String.format("Effect/%s/%s_%d.png",
                            this.getSubtype().name(), this.imageName, meleeFrame[0] % 10);
                    System.out.println(sequenceIdle);
                    this.setImage(new Image(String.valueOf(MainApplication.class.getResource(sequenceIdle))));
                    meleeFrame[0]++;
                })
        );
        attackAnimation.setOnFinished(event -> {
            this.setVisible(false);
            completionFuture.complete(true);
        });
        attackAnimation.setCycleCount(11);
        attackAnimation.play();

        return completionFuture;
    }
}
