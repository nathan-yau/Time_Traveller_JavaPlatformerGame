package ca.bcit.comp2522.termproject.pix.model.Enemy;


import ca.bcit.comp2522.termproject.pix.MainApplication;
import ca.bcit.comp2522.termproject.pix.model.AttackEffect.AttackEffect;
import ca.bcit.comp2522.termproject.pix.model.AttackEffect.MeleeEffect;
import ca.bcit.comp2522.termproject.pix.model.ObjectType;
import ca.bcit.comp2522.termproject.pix.model.player.Action;
import ca.bcit.comp2522.termproject.pix.model.player.Direction;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Animation;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.concurrent.CompletableFuture;

public class Minion extends Enemy {
    private Direction direction;
    private Action action;
    private final double leftmostWalkingRange;
    private final double initialXPosition;
    private final double rightmostWalkingRange;
    private SequentialTransition movingAction;
    private Timeline attackingAnimation;
    private Timeline walkingAnimation;
    private Timeline hurtingAnimation;
    private String imagePath;
    private ParallelTransition currentAnimation;
    private Direction walkingDirection;

    public Minion(int x, int y, int w, int h, EnemyType name, int currentLevel, String imageName) {
        super(x, y, w, h, ObjectType.MINION, name, currentLevel, imageName, 2);
        this.direction = Direction.BACKWARD;
        this.action = Action.WALKING;
        this.leftmostWalkingRange = x - 100 + this.getFitWidth()/2;
        this.initialXPosition = x;
        this.rightmostWalkingRange = x + 100;
        this.imagePath = String.format("%s/%s",this.getFolderPath(), direction.name());
        this.initializeMoving();
        this.initializeAttackingAnimation();
        this.initializeWalkingAnimation();
        this.initializeHurtingAnimation();
        this.startWalking();
    }

    private void startWalking() {
        currentAnimation = new ParallelTransition(
                this.movingAction,
                this.walkingAnimation
        );

        currentAnimation.setCycleCount(Timeline.INDEFINITE);
        currentAnimation.play();
    }

    private void initializeHurtingAnimation() {
        final int[] hurtImageFrame = {0};
        hurtingAnimation = new Timeline(
                new KeyFrame(Duration.millis(100), event -> {
                    final String sequenceIdle = String.format("%s/Hurt_%d.png", imagePath, hurtImageFrame[0] % 6);
                    this.setImage(new Image(String.valueOf(MainApplication.class.getResource(sequenceIdle))));
                    hurtImageFrame[0]++;
                    this.attackingAnimation.stop();
                    this.action = Action.HURTING;
                })
        );
        hurtingAnimation.setOnFinished(event -> {
            this.direction = walkingDirection;
            this.imagePath = String.format("%s/%s",this.getFolderPath(), direction.name());
            currentAnimation.play();
            this.setAttackEnable(true);
        });
        hurtingAnimation.setCycleCount(5);
    }

    private void initializeWalkingAnimation() {
        final int[] walkingImageFrame = {0};
        walkingAnimation = new Timeline(
                new KeyFrame(Duration.millis(100), event -> {
                    final String sequenceIdle = String.format("%s/Walk_%d.png", imagePath, walkingImageFrame[0] % 18);
                    this.setImage(new Image(String.valueOf(MainApplication.class.getResource(sequenceIdle))));
                    this.action = Action.WALKING;
                    walkingImageFrame[0]++;
                })
        );
        walkingAnimation.setCycleCount(Animation.INDEFINITE);
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void setDirection(Direction direction) {
            this.direction = direction;
            this.imagePath = String.format("%s/%s",this.getFolderPath(), direction.name());
    }

    private void initializeAttackingAnimation() {
        final int[] attackImageFrame = {0};
        attackingAnimation = new Timeline(
                new KeyFrame(Duration.millis(100), event -> {
                    final String sequenceIdle = String.format("%s/Attack_%d.png", imagePath, attackImageFrame[0] % 10);
                    this.setImage(new Image(String.valueOf(MainApplication.class.getResource(sequenceIdle))));
                    this.action = Action.MELEE_ATTACK;
                    attackImageFrame[0]++;
                })
        );
        attackingAnimation.setOnFinished(event -> {
            setAttackEnable(true);
            this.direction = walkingDirection;
            this.imagePath = String.format("%s/%s",this.getFolderPath(), direction.name());
            currentAnimation.play();
        });
        attackingAnimation.setCycleCount(9);
    }

    private void initializeMoving() {
        KeyFrame movingToLeft = new KeyFrame(Duration.seconds(1),
                new KeyValue(this.translateXProperty(), leftmostWalkingRange));
        KeyFrame movingToRight = new KeyFrame(Duration.seconds(2),
                new KeyValue(this.translateXProperty(), rightmostWalkingRange));
        KeyFrame movingBackToMiddleFast = new KeyFrame(Duration.seconds(1),
                new KeyValue(this.translateXProperty(), initialXPosition));
        KeyFrame movingBackToMiddleSlow = new KeyFrame(Duration.seconds(2),
                new KeyValue(this.translateXProperty(), initialXPosition));

        Timeline movingToLeftTimeline = new Timeline(movingToLeft);
        Timeline movingToRightTimeline = new Timeline(movingToRight);
        Timeline movingBackToMiddleFastTimeLine = new Timeline(movingBackToMiddleFast);
        Timeline movingBackToMiddleSlowTimeLine = new Timeline(movingBackToMiddleSlow);

        movingToRightTimeline.setOnFinished(event -> {
            this.direction = Direction.BACKWARD;
            this.walkingDirection = Direction.BACKWARD;
            this.imagePath = String.format("%s/%s",this.getFolderPath(), direction.name());
        });
        movingToLeftTimeline.setOnFinished(event -> {
            this.direction = Direction.FORWARD;
            this.walkingDirection = Direction.FORWARD;
            this.imagePath = String.format("%s/%s",this.getFolderPath(), direction.name());
        });

        movingAction = new SequentialTransition(movingToLeftTimeline, movingBackToMiddleFastTimeLine,
                movingToRightTimeline, movingBackToMiddleSlowTimeLine);
        movingAction.setCycleCount(Timeline.INDEFINITE);
    }

    @Override
    public void getHurt() {
        currentAnimation.pause();
        hurtingAnimation.play();
    }

    private void stopAllAliveAnimation() {
        if (currentAnimation!=null) {
            currentAnimation.stop();
        }
        if (hurtingAnimation!=null) {
            hurtingAnimation.stop();
        }
        if (attackingAnimation!=null) {
            attackingAnimation.stop();
        }
    }

    @Override
    public CompletableFuture<Boolean> startDying() {
        this.stopAllAliveAnimation();
        final int[] dyingFrame = {0};
        CompletableFuture<Boolean> completionFuture = new CompletableFuture<>();
        Timeline dyingAnimation = new Timeline(
                new KeyFrame(Duration.millis(50), event -> {
                    final String sequenceIdle = String.format("%s/Dying_%d.png", imagePath, dyingFrame[0] % 15);
                    this.setImage(new Image(String.valueOf(MainApplication.class.getResource(sequenceIdle))));
                    dyingFrame[0]++;
                    double newOpacity = Math.max(0.0, this.getOpacity() - 0.05);
                    this.setOpacity(newOpacity);
                    this.action = Action.DYING;
                })
        );
        dyingAnimation.setOnFinished(event -> {
            this.setVisible(false);
            completionFuture.complete(true);
        });
        dyingAnimation.setCycleCount(14);
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
        return 0;
    }

}
