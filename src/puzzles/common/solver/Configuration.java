package puzzles.common.solver;

import java.util.Collection;

/**
 * The representation of a single configuration for a puzzle.
 * The Backtracker depends on these routines in order to
 * solve a puzzle.  Therefore, all puzzles must implement this
 * interface.
 *
 * @author Patricio Solis
 */
public interface Configuration {

    /**
     * Is the current configuration a solution?
     * @return true if solution; false otherwise
     */
    boolean isSolution();

    /**
     * Get the collection of successors from the current one.
     *
     * @return All successors, valid and invalid
     */
    Collection<Configuration> getNeighbors();

    /**
     * Checks if the current configuration is equal to other configuration
     *
     * @param other Configuration to be checked if it has already been made
     * @return boolean true if equal, false otherwise
     */
    boolean equals(Object other);

    /**
     * Makes a unique hashCode.
     *
     * @return int the unique hashCode for the configuration
     */
    int hashCode();

    /**
     * Returns a string representation of the board, suitable for printing out.
     *
     * @return the string representation
     */
    String toString();
}
