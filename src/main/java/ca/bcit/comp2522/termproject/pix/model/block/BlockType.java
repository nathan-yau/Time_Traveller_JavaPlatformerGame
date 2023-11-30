package ca.bcit.comp2522.termproject.pix.model.block;

import ca.bcit.comp2522.termproject.pix.GameType;

/**
 * Enumerates the types of blocks.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-11
 */
public enum BlockType  implements GameType {
    SOLID_BLOCK, DECORATION_BLOCK, MOVING_BLOCK, MOVABLE_BLOCK, DISAPPEARING_BLOCK, TESTING_BLOCK, LADDERS
}
