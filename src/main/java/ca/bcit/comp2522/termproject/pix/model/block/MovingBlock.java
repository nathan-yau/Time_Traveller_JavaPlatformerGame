package ca.bcit.comp2522.termproject.pix.model.block;

import ca.bcit.comp2522.termproject.pix.AnimatedObjects;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;

/**
 * A block that moves up and down.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public class MovingBlock extends StandardBlock implements Serializable {
    // The duration of the block movement in seconds.
    private static final int MOVING_BLOCK_DURATION = 4;

    // The distance the block moves in points.
    private static final int MOVING_BLOCK_DISTANCE = -150;

    private transient SequentialTransition movingAnimation;

    /**
     * Constructs a MovingBlock.
     *
     * @param x the x-coordinate of the moving block as an int
     * @param y the y-coordinate of the moving block as an int
     * @param w the width of the moving block as an int
     * @param h the height of the moving block as an int
     * @param currentLevel the current game level as an int
     * @param imageName the name of the image as a String
     */
    public MovingBlock(final int x, final int y, final int w, final int h,
                       final int currentLevel, final String imageName) {
        super(x, y, w, h, BlockType.MOVING_BLOCK, currentLevel, imageName);
    }

    /*
     * Loads and sets up an existing MovingBlock.
     */
    @Serial
    private void readObject(final ObjectInputStream inputStream) throws ClassNotFoundException, IOException {
        inputStream.defaultReadObject();
        this.initializeMoving();
    }

    private void initializeMoving() {
        KeyFrame moveDown = new KeyFrame(Duration.seconds(MOVING_BLOCK_DURATION),
                new KeyValue(this.translateYProperty(), this.getTranslateY() + MOVING_BLOCK_DISTANCE));
        KeyFrame moveUp = new KeyFrame(Duration.seconds(MOVING_BLOCK_DURATION),
                new KeyValue(this.translateYProperty(), this.getTranslateY()));

        Timeline downTimeline = new Timeline(moveDown);
        Timeline upTimeline = new Timeline(moveUp);

        movingAnimation = new SequentialTransition(downTimeline, upTimeline);
        movingAnimation.setCycleCount(Timeline.INDEFINITE);
        movingAnimation.play();
    }

    /**
     * Terminates the animation.
     */
    @Override
    public void terminateAnimation() {
        super.terminateAnimation();
        movingAnimation = AnimatedObjects.releaseSequentialTransition(movingAnimation);
    }
}
