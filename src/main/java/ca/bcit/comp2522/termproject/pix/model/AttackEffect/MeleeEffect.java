package ca.bcit.comp2522.termproject.pix.model.AttackEffect;

import ca.bcit.comp2522.termproject.pix.MainApplication;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.concurrent.CompletableFuture;

/**
 * Represents a melee attack effect.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public class MeleeEffect extends AttackEffect {
    private final String imageName;
    private Timeline attackAnimation;
    private Timeline onHitAnimation;

    /**
     * Constructs a MeleeEffect.
     *
     * @param x the x-coordinate of the effect as a double
     * @param y the y-coordinate of the effect as a double
     * @param w the width of the effect as a double
     * @param h the height of the effect as a double
     * @param imageName the name of the image as a String
     */
    public MeleeEffect(final double x, final double y, final double w, final double h, final String imageName) {
        super(x, y, w, h, EffectType.MELEE_ATTACK, "Empty");
        this.imageName = imageName;
        this.attackAnimation = null;
        this.onHitAnimation  = null;
    }

    /**
     * Starts the effect.
     *
     * @return a CompletableFuture of a boolean
     */
    @Override
    public CompletableFuture<Boolean> startInitialEffect() {
        final int duration = 100;
        final int totalFrames = 4;
        final int[] meleeFrame = {0};
        CompletableFuture<Boolean> completionFuture = new CompletableFuture<>();
        attackAnimation = new Timeline(
                new KeyFrame(Duration.millis(duration), event -> {
                    final String sequenceIdle = String.format("Effect/%s/%s_%d.png",
                            this.getSubtype().name(), "Attack", meleeFrame[0]);
                    this.setImage(new Image(String.valueOf(MainApplication.class.getResource(sequenceIdle))));
                    meleeFrame[0] = (meleeFrame[0] + 1) % (totalFrames);
                })
        );
        attackAnimation.setOnFinished(event -> {
            this.setVisible(false);
            completionFuture.complete(true);
        });
        attackAnimation.setCycleCount(totalFrames);
        attackAnimation.play();

        return completionFuture;
    }
    /**
     * Stops the initial effect.
     */
    public void stopInitialEffect() {
        attackAnimation.stop();
    }

    /**
     * Starts the on hit effect.
     *
     * @return a CompletableFuture of a boolean
     */
    @Override
    public CompletableFuture<Boolean> startOnHitEffect() {
        final int duration = 100;
        final int totalFrames = 10;
        final int[] hitFrame = {0};
        CompletableFuture<Boolean> completionFuture = new CompletableFuture<>();
        onHitAnimation = new Timeline(
                new KeyFrame(Duration.millis(duration), event -> {
                    final String sequenceIdle = String.format("Effect/%s/%s_%d.png",
                            this.getSubtype().name(), this.imageName, hitFrame[0]);
                    this.setImage(new Image(String.valueOf(MainApplication.class.getResource(sequenceIdle))));
                    hitFrame[0] = (hitFrame[0] + 1) % (totalFrames);
                })
        );
        onHitAnimation.setCycleCount(totalFrames);
        onHitAnimation.setOnFinished(event -> {
            this.setVisible(false);
            completionFuture.complete(true);
        });
        onHitAnimation.play();

        return completionFuture;
    }
}
