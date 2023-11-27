package ca.bcit.comp2522.termproject.pix.model.Enemy;

import ca.bcit.comp2522.termproject.pix.MainApplication;
import ca.bcit.comp2522.termproject.pix.model.AttackEffect.AttackEffect;
import ca.bcit.comp2522.termproject.pix.model.AttackEffect.MeleeEffect;
import ca.bcit.comp2522.termproject.pix.model.GameObject;
import ca.bcit.comp2522.termproject.pix.model.ObjectType;
import ca.bcit.comp2522.termproject.pix.model.player.Action;
import ca.bcit.comp2522.termproject.pix.model.player.Direction;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Animation;
import javafx.beans.property.DoubleProperty;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.concurrent.CompletableFuture;

public class Minion extends Enemy {
    private Direction direction;
    private Action action;
    private final double leftmostWalkingRange;
    private final double upmostFlyingRange;
    private final double initialXPosition;
    private final double initialYPosition;
    private final double rightmostWalkingRange;
    private final double bottommostFlyingRange;

    private final int movingDuration;
    private final int movingFrame;
    private final int hurtingDuration;
    private final int hurtingFrame;
    private final int attackingDuration;
    private final int attackingFrame;
    private final int dyingDuration;
    private final int dyingFrame;
    private final boolean xWalker;

    private SequentialTransition movingAction;
    private Timeline attackingAnimation;
    private Timeline flyingAnimation;
    private Timeline hurtingAnimation;
    private String imagePath;
    private ParallelTransition currentAnimation;
    private Direction facingDirection;
    private Timeline walkingAnimation;
    private GameObject healthBar;

    /**
     * Constructs a Minion.
     *
     * @param x the x coordinate of the Minion as an int
     * @param y the y coordinate of the Minion as an int
     * @param width the width of the Minion as an int
     * @param height the height of the Minion as an int
     * @param name the name of the Minion as an EnemyName
     * @param movingArea the moving area of the Minion as an int
     * @param movingDuration the walking duration of the Minion as an int
     * @param movingFrame the walking frame of the Minion as an int
     * @param hurtingDuration the hurting duration of the Minion as an int
     * @param hurtingFrame the hurting frame of the Minion as an int
     * @param attackingDuration the attacking duration of the Minion as an int
     * @param attackingFrame the attacking frame of the Minion as an int
     * @param dyingDuration the dying duration of the Minion as an int
     * @param dyingFrame the dying frame of the Minion as an int
     * @param healthPoint the health point of the Minion as an int
     * @param attackPoint the attack point of the Minion as an int
     * @param xWalker the boolean value of the Minion's walking ability
     */
    public Minion(final int x, final int y, final int width, final int height, final EnemyType name,
                  final int movingArea, final int movingDuration, final int movingFrame, final int hurtingDuration,
                  final int hurtingFrame, final int attackingDuration, final int attackingFrame,
                  final int dyingDuration, final int dyingFrame, final int healthPoint, final int attackPoint,
                  final boolean xWalker) {
        super(x, y, width, height, ObjectType.MINION, name, healthPoint, attackPoint);
        this.direction = Direction.BACKWARD;
        this.action = Action.WALKING;
        this.initialXPosition = x;
        this.initialYPosition = y;
        this.upmostFlyingRange = y - movingArea;
        this.bottommostFlyingRange = y + movingArea;
        this.leftmostWalkingRange = x - movingArea + this.getFitWidth() / 2;
        this.rightmostWalkingRange = x + movingArea;
        this.movingDuration = movingDuration;
        this.movingFrame = movingFrame;
        this.hurtingDuration = hurtingDuration;
        this.hurtingFrame = hurtingFrame;
        this.attackingDuration = attackingDuration;
        this.attackingFrame = attackingFrame;
        this.dyingDuration = dyingDuration;
        this.dyingFrame = dyingFrame;
        this.xWalker = xWalker;
        this.imagePath = String.format("%s/%s", this.getFolderPath(), direction.name());
        this.initializeAttackingAnimation();
        this.initializeHurtingAnimation();
        if (xWalker) {
            this.initializeWalking();
            this.initializeWalkingAnimation();
        } else {
            this.initializeFlying();
            this.initializeFlyingAnimation();
        }
        this.startMovement();
    }

    private void startMovement() throws IllegalArgumentException {
        if (xWalker) {
            currentAnimation = new ParallelTransition(
                    this.movingAction,
                    this.walkingAnimation
            );
        } else {
            currentAnimation = new ParallelTransition(
                    this.movingAction,
                    this.flyingAnimation
            );
        }
        currentAnimation.setCycleCount(Timeline.INDEFINITE);
        currentAnimation.play();
    }

    private void initializeHurtingAnimation() {
        final int[] hurtImageFrame = {0};
        hurtingAnimation = new Timeline(
                new KeyFrame(Duration.millis(hurtingDuration), event -> {
                    this.attackingAnimation.stop();
                    this.action = Action.HURTING;
                    final String image = String.format("%s/Hurt_%d.png", imagePath, hurtImageFrame[0]);
                    this.setImage(new Image(String.valueOf(MainApplication.class.getResource(image))));
                    hurtImageFrame[0] = (hurtImageFrame[0] + 1) % (hurtingFrame + 1);
                })
        );
        hurtingAnimation.setOnFinished(event -> {
            this.direction = facingDirection;
            this.imagePath = String.format("%s/%s", this.getFolderPath(), direction.name());
            currentAnimation.play();
            this.setAttackEnable(true);
        });
        hurtingAnimation.setCycleCount(hurtingFrame);
    }

    private void initializeFlyingAnimation() {
        final int[] flyingImageFrame = {0};
        flyingAnimation = new Timeline(
                new KeyFrame(Duration.millis(movingDuration), event -> {
                    this.action = Action.FLYING;
                    final String sequenceIdle = String.format("%s/Walk_%d.png", imagePath, flyingImageFrame[0]);
                    this.setImage(new Image(String.valueOf(MainApplication.class.getResource(sequenceIdle))));
                    flyingImageFrame[0] = (flyingImageFrame[0] + 1) % (movingFrame + 1);
                })
        );
        flyingAnimation.setCycleCount(Animation.INDEFINITE);
    }

    private void initializeWalkingAnimation() {
        final int[] walkingImageFrame = {0};
        walkingAnimation = new Timeline(
                new KeyFrame(Duration.millis(movingDuration), event -> {
                    this.action = Action.WALKING;
                    final String sequenceIdle = String.format("%s/Walk_%d.png", imagePath, walkingImageFrame[0]);
                    this.setImage(new Image(String.valueOf(MainApplication.class.getResource(sequenceIdle))));
                    walkingImageFrame[0] = (walkingImageFrame[0] + 1) % (movingFrame + 1);
                })
        );
        walkingAnimation.setCycleCount(Animation.INDEFINITE);
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void setDirection(final Direction direction) {
            this.direction = direction;
            this.imagePath = String.format("%s/%s",this.getFolderPath(), direction.name());
    }

    private void initializeAttackingAnimation() {
        final int[] attackImageFrame = {0};
        attackingAnimation = new Timeline(
                new KeyFrame(Duration.millis(attackingDuration), event -> {
                    this.action = Action.MELEE_ATTACK;
                    final String sequenceIdle = String.format("%s/Attack_%d.png", imagePath, attackImageFrame[0]);
                    this.setImage(new Image(String.valueOf(MainApplication.class.getResource(sequenceIdle))));
                    attackImageFrame[0] = (attackImageFrame[0] + 1) % (attackingFrame + 1);
                })
        );
        attackingAnimation.setOnFinished(event -> {
            setAttackEnable(true);
            this.direction = facingDirection;
            this.imagePath = String.format("%s/%s", this.getFolderPath(), direction.name());
            currentAnimation.play();
        });
        attackingAnimation.setCycleCount(attackingFrame);
    }

    private void initializeAnimation(final double startRange, final double endRange, final double initialPosition,
                                     final Direction forwardDirection, final Direction backwardDirection,
                                     final DoubleProperty axis) {
        KeyFrame beginning = new KeyFrame(Duration.seconds(1),
                new KeyValue(axis, startRange));
        KeyFrame ending = new KeyFrame(Duration.seconds(2),
                new KeyValue(axis, endRange));
        KeyFrame middleFast = new KeyFrame(Duration.seconds(1),
                new KeyValue(axis, initialPosition));
        KeyFrame middleSlow = new KeyFrame(Duration.seconds(2),
                new KeyValue(axis, initialPosition));

        Timeline beginningTimeline = new Timeline(beginning);
        Timeline endingTimeline = new Timeline(ending);
        Timeline middleFastTimeline = new Timeline(middleFast);
        Timeline middleSlowTimeline = new Timeline(middleSlow);

        endingTimeline.setOnFinished(event -> {
            this.direction = backwardDirection;
            this.facingDirection = backwardDirection;
            this.imagePath = String.format("%s/%s", this.getFolderPath(), direction.name());
        });

        beginningTimeline.setOnFinished(event -> {
            this.direction = forwardDirection;
            this.facingDirection = forwardDirection;
            this.imagePath = String.format("%s/%s", this.getFolderPath(), direction.name());
        });

        movingAction = new SequentialTransition(beginningTimeline, middleFastTimeline,
                endingTimeline, middleSlowTimeline);
        movingAction.setCycleCount(Timeline.INDEFINITE);
    }

    private void initializeFlying() {
        this.initializeAnimation(upmostFlyingRange, bottommostFlyingRange, initialYPosition, Direction.FORWARD,
                Direction.BACKWARD, this.translateYProperty());
    }


    private void initializeWalking() {
        this.initializeAnimation(leftmostWalkingRange, rightmostWalkingRange, initialXPosition, Direction.FORWARD,
                Direction.BACKWARD, this.translateXProperty());
    }

    @Override
    public void getHurt() {
        currentAnimation.pause();
        hurtingAnimation.play();
    }

    private void stopAllAliveAnimation() {
        if (currentAnimation != null) {
            currentAnimation.stop();
        }
        if (hurtingAnimation != null) {
            hurtingAnimation.stop();
        }
        if (attackingAnimation != null) {
            attackingAnimation.stop();
        }
    }

    @Override
    public CompletableFuture<Boolean> startDying() {
        this.stopAllAliveAnimation();
        final int[] dyingImageFrame = {0};
        final double opacityDelta = 0.05;
        CompletableFuture<Boolean> completionFuture = new CompletableFuture<>();
        Timeline dyingAnimation = new Timeline(
                new KeyFrame(Duration.millis(dyingDuration), event -> {
                    this.action = Action.DYING;
                    final String sequenceIdle = String.format("%s/Dying_%d.png", imagePath, dyingImageFrame[0]);
                    this.setImage(new Image(String.valueOf(MainApplication.class.getResource(sequenceIdle))));
                    double newOpacity = Math.max(0.0, this.getOpacity() - opacityDelta);
                    this.setOpacity(newOpacity);
                    dyingImageFrame[0] = (dyingImageFrame[0] + 1) % (dyingFrame + 1);
                })
        );
        dyingAnimation.setOnFinished(event -> {
            this.setVisible(false);
            completionFuture.complete(true);
        });
        dyingAnimation.setCycleCount(dyingFrame);
        dyingAnimation.play();

        return completionFuture;
    }

    @Override
    public AttackEffect meleeAttack() {
        if (this.getAttackEnable() && this.action != Action.HURTING) {
            this.setAttackEnable(false);
            this.currentAnimation.pause();
            this.attackingAnimation.play();
            double effectY = this.getBoundsInParent().getMinY();
            double effectX = this.getBoundsInParent().getMinX() - 50;
            return new MeleeEffect(effectX, effectY + 10, 50, 50, "Explosion");
        }
        return null;
    }

    @Override
    public AttackEffect rangeAttack() {
        return null;
    }

    @Override
    public int getAttackDamage() {
        return 1;
    }
}
