package ca.bcit.comp2522.termproject.pix.model.block;

import ca.bcit.comp2522.termproject.pix.AnimatedObjects;
import ca.bcit.comp2522.termproject.pix.MainApplication;
import ca.bcit.comp2522.termproject.pix.model.GameObject;
import ca.bcit.comp2522.termproject.pix.model.ObjectType;
import javafx.animation.KeyFrame;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyValue;
import javafx.util.Duration;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a standard stationary game block.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public class StandardBlock extends GameObject<BlockType> implements AnimatedObjects, Serializable {
    private final int x;
    private final int y;
    private final int w;
    private final int h;
    private transient SequentialTransition fallingAnimation;
    /**
     * Constructs a StandardBlock.
     *
     * @param x the x-coordinate of the block as an int
     * @param y the y-coordinate of the block as an int
     * @param w the width of the block as an int
     * @param h the height of the block as an int
     * @param blockType the type of the block as a BlockType
     * @param imagePath path to the folder containing the image
     * @param imageName the name of the image as a String
     */
    public StandardBlock(final int x, final int y, final int w, final int h, final BlockType blockType,
                         final String imagePath, final String imageName) {
        super(x, y, w, h, ObjectType.BLOCK, blockType, String.format("%s/%s.png",
                imagePath, imageName));
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.initializeFailing();
    }
    /**
     * Constructs a StandardBlock.
     *
     * @param x the x-coordinate of the block as an int
     * @param y the y-coordinate of the block as an int
     * @param w the width of the block as an int
     * @param h the height of the block as an int
     * @param blockType the type of the block as a BlockType
     * @param currentLevel the current game level as an int
     * @param imageName the name of the image as a String
     */
    public StandardBlock(final int x, final int y, final int w, final int h, final BlockType blockType,
                         final int currentLevel, final String imageName) {
        this(x, y, w, h, blockType, String.format("%d/%s",
                currentLevel, blockType.name()), imageName);
    }

    /*
     * Loads and sets up an existing StandardBlock.
     */
    @Serial
    private void readObject(final ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        this.setTranslateX(this.x);
        this.setTranslateY(this.y);
        this.setFitWidth(this.w);
        this.setFitHeight(this.h);
        this.initializeFailing();
    }

    /*
     * Initializes the step on animation.
     */
    private void initializeFailing() {
        final int fallingDuration = 500;
        final int fadingDuration = 300;
        // Opacity animation keyframes
        KeyFrame fadeOut = new KeyFrame(Duration.millis(fadingDuration),
                new KeyValue(this.opacityProperty(), 0));

        KeyFrame fadeIn = new KeyFrame(Duration.millis(fadingDuration),
                new KeyValue(this.opacityProperty(), 1));

        // Falling animation keyframe
        KeyFrame movingBackToMiddleFast = new KeyFrame(Duration.millis(fallingDuration),
                new KeyValue(this.translateYProperty(), MainApplication.WINDOW_HEIGHT));

        // Timelines for opacity changes and falling
        Timeline opacityTimeline = new Timeline(fadeOut, fadeIn);
        opacityTimeline.setCycleCount(2);
        Timeline fallingTimeline = new Timeline(movingBackToMiddleFast);

        // SequentialTransition to play opacity changes and falling in sequence
        fallingAnimation = new SequentialTransition(opacityTimeline, fallingTimeline);
        fallingAnimation.setCycleCount(1);
    }

    /**
     * Plays the step on animation.
     * @return a CompletableFuture of a boolean
     */
    public CompletableFuture<Boolean> animate() {
        CompletableFuture<Boolean> completionFuture = new CompletableFuture<>();
        fallingAnimation.onFinishedProperty().set(e -> {
            this.setVisible(false);
            completionFuture.complete(true);
        });
        fallingAnimation.play();
        return completionFuture;
    }

    /**
     * Terminates the animation and free the memory.
     */
    @Override
    public void terminateAnimation() {
        fallingAnimation = AnimatedObjects.releaseSequentialTransition(fallingAnimation);
    }
}
