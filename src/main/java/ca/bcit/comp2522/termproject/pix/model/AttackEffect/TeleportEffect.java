package ca.bcit.comp2522.termproject.pix.model.AttackEffect;

import ca.bcit.comp2522.termproject.pix.MainApplication;
import ca.bcit.comp2522.termproject.pix.model.ObjectType;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.concurrent.CompletableFuture;

/**
 * Represents a teleport effect.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public class TeleportEffect extends ObjectEffect {

    private Timeline teleportAnimation;

    /**
     * Constructs an TeleportEffect.
     *
     * @param x the x-coordinate of the effect as a double
     * @param y the y-coordinate of the effect as a double
     * @param w the width of the effect as a double
     * @param h the height of the effect as a double
     * @param imageName the name of the image as a String
     */
    public TeleportEffect(final double x, final double y, final double w, final double h, final String imageName) {
        super(x, y, w, h, ObjectType.EFFECT, EffectType.TELEPORT_EFFECT,
                String.format("Effect/Teleport_Effect/%s_0.png", imageName));
    }

    /* Helper method for updating the frame. */
    private void updateFrame(final double[] frameSize) {
        final int sizeDelta = 20;
        final int positionDelta = 9;

        for (int index = 0; index < frameSize.length; index += 2) {
            if (index == 0) {
                this.setFitWidthAndHeight(frameSize[index], frameSize[index + 1]);
                frameSize[index] += sizeDelta;
                frameSize[index + 1] += sizeDelta;
            } else {
                this.setTranslateXAndY(frameSize[index], frameSize[index + 1]);
                frameSize[index] -= positionDelta;
                frameSize[index + 1] -= positionDelta;
            }
        }
    }

    /* Helper method for update Width and Height. */
    private void setFitWidthAndHeight(final double fitWidth, final double fitHeight) {
        this.setFitWidth(fitWidth);
        this.setFitHeight(fitHeight);
    }
    /* Helper method for update X and Y. */
    private void setTranslateXAndY(final double translateX, final double translateY) {
        this.setTranslateX(translateX);
        this.setTranslateY(translateY);
    }


    /**
     * Starts the initial effect.
     *
     * @return a CompletableFuture of a boolean
     */
    @Override
    public CompletableFuture<Boolean> startInitialEffect() {
        final int duration = 100;
        final int totalFrames = 6;
        final int[] frame = {0};
        final double[] frameSize = {this.getFitWidth(), this.getFitHeight(), this.getMinX(), this.getMinY()};
        CompletableFuture<Boolean> completionFuture = new CompletableFuture<>();
        teleportAnimation = new Timeline(
                new KeyFrame(Duration.millis(duration), event -> {
                    final String sequenceIdle = String.format("Effect/%s/%s_%d.png",
                            this.getSubtype().name(), "Teleport", frame[0]);
                    this.setImage(new Image(String.valueOf(MainApplication.class.getResource(sequenceIdle))));
                    frame[0] = (frame[0] + 1) % totalFrames;
                    updateFrame(frameSize);
                })
        );
        teleportAnimation.setOnFinished(event -> {
            this.setVisible(false);
            completionFuture.complete(true);
        });
        teleportAnimation.setCycleCount(totalFrames);
        teleportAnimation.play();

        return completionFuture;
    }

    /**
     * Stops the initial effect.
     */
    @Override
    public void stopInitialEffect() {
        teleportAnimation.stop();
    }
}
