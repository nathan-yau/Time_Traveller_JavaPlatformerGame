package ca.bcit.comp2522.termproject.pix.model.AttackEffect;

import java.util.concurrent.CompletableFuture;

/**
 * Represents an attack interface.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public interface AttackInterface {
    /**
     * Starts the on hit effect.
     *
     * @return a CompletableFuture of a boolean
     */
    CompletableFuture<Boolean> startOnHitEffect();
}
