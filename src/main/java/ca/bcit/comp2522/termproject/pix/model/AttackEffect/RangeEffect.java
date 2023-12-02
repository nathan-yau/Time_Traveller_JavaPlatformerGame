package ca.bcit.comp2522.termproject.pix.model.AttackEffect;

import ca.bcit.comp2522.termproject.pix.MainApplication;
import ca.bcit.comp2522.termproject.pix.model.player.Direction;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.concurrent.CompletableFuture;

/**
 * Represents a range attack effect.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public class RangeEffect extends AttackEffect {
    private final String imageName;
    private final double initialXPosition;
    private Timeline movement;
    private Timeline magicAnimation;
    private final double hitRange;
    private CompletableFuture<Boolean> movementCompletion;
    private final String direction;
    private ParallelTransition currentAnimation;
    private Timeline onHitAnimation;

    /**
     * Constructs a RangeEffect.
     *
     * @param x the x-coordinate of the effect as a double
     * @param y the y-coordinate of the effect as a double
     * @param w the width of the effect as a double
     * @param h the height of the effect as a double
     * @param imageName the name of the image as a String
     * @param range the range of the effect as a double
     * @param playerDirection the direction of the player as a Direction
     */
    public RangeEffect(final double x, final double y, final double w, final double h, final String imageName,
                       final double range, final Direction playerDirection) {
        super(x, y, w, h, EffectType.RANGE_ATTACK, "Empty");
        this.imageName = imageName;
        this.initialXPosition = x;
        this.hitRange = range;
        this.initialMovement();
        this.initialMagicAnimation();
        this.direction = playerDirection.name();
        this.currentAnimation = null;
        this.onHitAnimation = null;
    }

    /**
     * Initializes the magic animation.
     *
     */
    public void initialMagicAnimation() {
        final int[] magicImageFrame = {0};
        final int duration = 100;
        final int magicImageFrameCount = 11;
        magicAnimation = new Timeline(
                new KeyFrame(Duration.millis(duration), event -> {
                    final String sequenceIdle = String.format("Effect/Range_attack/%s/%s_%d.png", direction, imageName,
                            magicImageFrame[0]);
                    this.setImage(new Image(String.valueOf(MainApplication.class.getResource(sequenceIdle))));
                    magicImageFrame[0] = (magicImageFrame[0] + 1) % (magicImageFrameCount + 1);
                })
        );
        magicAnimation.setCycleCount(magicImageFrameCount);
    }


    /**
     * Initializes the initial movement animation for the magic attack.
     *
     */
    public void initialMovement() {
        movementCompletion = new CompletableFuture<>();
        KeyFrame movingToLeft = new KeyFrame(Duration.seconds(1),
                new KeyValue(this.translateXProperty(), initialXPosition + hitRange));

        movement = new Timeline(movingToLeft);

        movement.setCycleCount(1);
        movement.setOnFinished(event -> movementCompletion.complete(true));
    }

    /**
     * Starts the effect.
     *
     * @return a CompletableFuture of a boolean
     */
    @Override
    public CompletableFuture<Boolean> startInitialEffect() {
        currentAnimation = new ParallelTransition(
                this.movement,
                this.magicAnimation
        );

        currentAnimation.setCycleCount(1);
        currentAnimation.play();
        return movementCompletion;
    }

    /**
     * Starts the on hit effect.
     *
     * @return a CompletableFuture of a boolean
     */
    @Override
    public CompletableFuture<Boolean> startOnHitEffect() {
        final int duration = 100;
        final int delayDuration = 300;
        final int totalFrames = 10;
        final int[] hitFrame = {0};
        CompletableFuture<Boolean> completionFuture = new CompletableFuture<>();
        onHitAnimation = new Timeline(
                new KeyFrame(Duration.millis(duration), event -> {
                    final String sequenceIdle = String.format("Effect/%s/%s_%d.png",
                            this.getSubtype().name(), "Explosion", hitFrame[0]);
                    this.setImage(new Image(String.valueOf(MainApplication.class.getResource(sequenceIdle))));
                    hitFrame[0] = (hitFrame[0] + 1) % (totalFrames);
                })
        );
        onHitAnimation.setCycleCount(totalFrames);
        onHitAnimation.setOnFinished(event -> completionFuture.complete(true));

        onHitAnimation.setDelay(Duration.millis(delayDuration));
        onHitAnimation.play();

        return completionFuture;
    }

    /**
     * Stops the initial effect.
     *
     */
    @Override
    public void stopInitialEffect() {
        final int delayMillis = 300;
        Duration delayDuration = Duration.millis(delayMillis);
        PauseTransition delay = new PauseTransition(delayDuration);
        delay.setOnFinished(event -> {
            if (currentAnimation != null) {
                currentAnimation.stop();
            }
        });
        delay.play();
    }

}
