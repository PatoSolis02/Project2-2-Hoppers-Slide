package puzzles.crossing;

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single configuration in the Crossing puzzle.
 *
 * @author Patricio Solis
 */
public class CrossingConfig implements Configuration {

    /** the number of pups on the left side of the river */
    private int pupsLeft;
    /** the number of wolves on the left side of the river */
    private int wolvesLeft;
    /** the number of pups on the right side of the river */
    private int pupsRight;
    /** the number of wolves on the right side of the river */
    private int wolvesRight;
    /** true if boat is on the left side of the river; false if on the right side */
    private boolean boatLeft;

    /**
     * Constructor for Crossing configurations
     *
     * @param pups the initial number of pups on the left side
     * @param wolves the initial number of pups on the right side
     */
    public CrossingConfig(int pups, int wolves){
        pupsLeft = pups;
        wolvesLeft = wolves;
        pupsRight = 0;
        wolvesRight = 0;
        boatLeft = true;
    }

    /**
     * Copy constructor for Crossing configurations
     *
     * @param current the current configuration that is going to be copied
     * @param movePups the number of pups to be moved from either left or right
     * @param moveWolves the number of wolves to be moved from either left or right
     */
    public CrossingConfig(CrossingConfig current, int movePups, int moveWolves){

        // Checks if the boat is on the left and if it is it moves pup and wolves
        // from the left to right else it moves them from right to left and changes
        // the side the boat is on
        if(current.boatLeft){
            pupsLeft = current.pupsLeft - movePups;
            pupsRight = current.pupsRight + movePups;
            wolvesLeft = current.wolvesLeft - moveWolves;
            wolvesRight = current.wolvesRight + moveWolves;
            boatLeft = false;
        } else {
            pupsLeft = current.pupsLeft + movePups;
            pupsRight = current.pupsRight - movePups;
            wolvesLeft = current.wolvesLeft + moveWolves;
            wolvesRight = current.wolvesRight - moveWolves;
            boatLeft = true;
        }
    }

    /**
     * Checks if the current configuration is the solution by
     * checking if the number of pups and wolves on the left is 0
     *
     * @return boolean true if pups and wolves on left are 0, false otherwise
     */
    @Override
    public boolean isSolution() {
        return pupsLeft == 0 && wolvesLeft == 0;
    }

    /**
     * Makes all the possible successor/neighbors of current configuration
     *
     * @return ArrayList<Configuration> that contains all possible neighbors
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        ArrayList<Configuration> successors = new ArrayList<>();

        // if boat is on the right the only possible CrossingConfig that could
        // lead to a solution is if one pup crosses
        if(!boatLeft){
            if(!(pupsRight - 1 < 0)) {
                successors.add(new CrossingConfig(this, 1, 0));
            }
        } else {
            if(pupsLeft - 2 < 0){ // checks if there is enough pups to move two
                successors.add(new CrossingConfig(this, 0, 1));
                successors.add(new CrossingConfig(this, 1, 0));
            } else if(pupsLeft - 1 < 0){ //checks if there is enough pups to move one
                successors.add(new CrossingConfig(this, 0, 1));
            } else if(wolvesLeft - 1 < 0){ //checks if there is any wolves to move
                successors.add(new CrossingConfig(this, 1, 0));
                successors.add(new CrossingConfig(this, 2, 0));
            } else {
                successors.add(new CrossingConfig(this, 1, 0));
                successors.add(new CrossingConfig(this, 2, 0));
                successors.add(new CrossingConfig(this, 0, 1));
            }
        }

        return successors;
    }

    /**
     * Checks if the current configuration is equal to other configuration
     * by checking if the number of pups and wolves on the left side are equal
     * and if the boat is on the same side
     *
     * @param other CrossingConfig to be checked if it has already been made
     * @return boolean true if equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        boolean equal = false;
        if(other instanceof CrossingConfig otherConfig){
            equal = pupsLeft == otherConfig.pupsLeft && wolvesLeft == otherConfig.wolvesLeft
                    && boatLeft == otherConfig.boatLeft;
        }
        return equal;
    }

    /**
     * Makes a unique hashCode by adding the number of pups and wolves
     * on the left and also adding 1 if the boat is on the left
     *
     * @return int the unique hashCode for the CrossingConfig
     */
    @Override
    public int hashCode() {
        int add = 0;
        if(boatLeft){
            add = 1;
        }
        return pupsLeft + pupsRight + add;
    }

    /**
     * Prints the state of current configuration with proper format.
     * Displays what side the boat is on and the number of pups and
     * wolves on each side of the river
     *
     * @return String that represents the current state of given configuration
     */
    @Override
    public String toString() {
        if(boatLeft){
            return "(BOAT)  left=[" + pupsLeft + ", " + wolvesLeft + "], right=["
                    + pupsRight + ", " + wolvesRight + "]";
        } else {
            return "        left=[" + pupsLeft + ", " + wolvesLeft + "], right=["
                    + pupsRight + ", " + wolvesRight + "] (BOAT)";
        }
    }
}
