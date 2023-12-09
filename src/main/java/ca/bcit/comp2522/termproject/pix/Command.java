package ca.bcit.comp2522.termproject.pix;
/**
 * Represents a command.
 *
 * @author Nathan Yau
 * @author Derek Woo
 * @version 2023-12
 */
@FunctionalInterface
public interface Command {
    /**
     * Executes the command.
     */
    void execute();
}
