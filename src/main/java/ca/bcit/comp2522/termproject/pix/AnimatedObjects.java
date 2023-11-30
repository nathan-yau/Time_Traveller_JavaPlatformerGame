package ca.bcit.comp2522.termproject.pix;

import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;

/**
 * Represents an object that can be animated.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023
 */
public interface AnimatedObjects {
    /**
     * Releases the timeline resources.
     * @param timeline the timeline to be released
     */
    static void releaseTimeline(Timeline timeline) {
        if (timeline != null) {
            timeline.stop();
            timeline.getKeyFrames().clear();
        }
    }

    /**
     * Releases the parallel transition resources.
     * @param transition the parallel transition to be released
     */
    static void releaseParallelTransition(ParallelTransition transition) {
        if (transition != null) {
            transition.stop();
            transition.getChildren().clear();
        }
    }

    /**
     * Releases the sequential transition resources.
     * @param transition the sequential transition to be released
     */
    static void releaseSequentialTransition(SequentialTransition transition) {
        if (transition != null) {
            transition.stop();
            transition.getChildren().clear();
        }
    }
}
