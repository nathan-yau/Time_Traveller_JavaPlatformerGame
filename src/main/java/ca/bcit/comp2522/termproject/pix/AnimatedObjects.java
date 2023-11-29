package ca.bcit.comp2522.termproject.pix;

import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;

public interface AnimatedObjects {
    static void releaseTimeline(Timeline timeline) {
        if (timeline != null) {
            timeline.stop();
            timeline.getKeyFrames().clear();
        }
    }

    static void releaseParallelTransition(ParallelTransition transition) {
        if (transition != null) {
            transition.stop();
            transition.getChildren().clear();
        }
    }

    static void releaseSequentialTransition(SequentialTransition transition) {
        if (transition != null) {
            transition.stop();
            transition.getChildren().clear();
        }
    }
}
