package ca.bcit.comp2522.termproject.pix.model.AttackEffect;


import ca.bcit.comp2522.termproject.pix.MainApplication;
import ca.bcit.comp2522.termproject.pix.model.player.Direction;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.concurrent.CompletableFuture;

public class RangeEffect extends AttackEffect {
    private final String imageName;
    private final double initialXPosition;
    private Timeline movement;
    private Timeline magicAnimation;
    private final double hitRange;
    private CompletableFuture<Boolean> movementCompletion;
    private final String direction;
    private ParallelTransition currentAnimation;

    public RangeEffect(double x, double y, double w, double h, String imageName, double range, Direction playerDirection) {
        super(x, y, w, h, EffectType.RANGE_ATTACK, imageName);
        this.imageName = imageName;
        this.initialXPosition = x;
        this.hitRange = range;
        this.initialMovement();
        this.initialMagicAnimation();
        this.direction = playerDirection.name();
        this.currentAnimation = null;
    }

    public void initialMagicAnimation() {
        final int[] magicImageFrame = {0};
        magicAnimation = new Timeline(
                new KeyFrame(Duration.millis(100), event -> {
                    final String sequenceIdle = String.format("Effect/Range_attack/%s/%s_%d.png", direction, imageName, magicImageFrame[0] % 12);
                    this.setImage(new Image(String.valueOf(MainApplication.class.getResource(sequenceIdle))));
                    magicImageFrame[0]++;
                })
        );
        magicAnimation.setCycleCount(11);
    }


    public void initialMovement() {
        movementCompletion = new CompletableFuture<>();
        KeyFrame movingToLeft = new KeyFrame(Duration.seconds(1),
                new KeyValue(this.translateXProperty(), initialXPosition + hitRange));

        movement = new Timeline(movingToLeft);

        movement.setCycleCount(1);
        movement.setOnFinished(event -> {
            this.setVisible(false);
            movementCompletion.complete(true);
        });
    }

    @Override
    public CompletableFuture<Boolean> startEffect() {
        currentAnimation = new ParallelTransition(
                this.movement,
                this.magicAnimation
        );

        currentAnimation.setCycleCount(1);
        currentAnimation.play();
        return movementCompletion;
    }

}
