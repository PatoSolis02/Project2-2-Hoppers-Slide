package puzzles.strings;

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Represents a single configuration in the Strings puzzle.
 *
 * @author Patricio Solis
 */
public class StringsConfig implements Configuration {

    /** the current string of the configuration */
    private String currString;
    /** the goal ending string */
    private String endString;

    /**
     * Constructor for Strings configuration
     *
     * @param start the initial string
     * @param end the desired end string
     */
    public StringsConfig(String start, String end){
        this.currString = start;
        this.endString = end;
    }

    /**
     * Copy constructor for Strings configurations
     *
     * @param current the current configuration that is going to be copied
     * @param index the index of the currString that is going to be changed
     * @param down true if the character of currString to be changed is going down alphabetically; false otherwise
     */
    public StringsConfig(StringsConfig current, int index, boolean down) {
        this.endString = current.endString;

        char[] ch = current.currString.toCharArray(); // turn string to char array to change at certain index
        if(down){
            if(ch[index] == 'A') {
                ch[index] = 'Z';
            } else {
                ch[index] = (char) ((int)ch[index] - 1);
            }
        } else {
            if(ch[index] == 'Z'){
                ch[index] = 'A';
            } else {
                ch[index] = (char) ((int)ch[index] + 1);
            }
        }
        this.currString = String.copyValueOf(ch);
    }


    /**
     * Checks if the current configuration is the solution by
     * checking if the current String is equal to the end String
     *
     * @return true if Strings are equal; false otherwise
     */
    @Override
    public boolean isSolution() {
        return currString.equals(endString);
    }

    /**
     * Makes all the possible successor/neighbors of current configuration
     *
     * @return ArrayList<Configuration> that contains all possible neighbors
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        ArrayList<Configuration> successors = new ArrayList<>();

        // changes current String in both directions for each index of the string
        for(int i = 0; i < currString.length(); i++){
            successors.add(new StringsConfig(this, i, false));
            successors.add(new StringsConfig(this, i, true));
        }
        return successors;
    }

    /**
     * Checks if the current configuration is equal to other configuration
     * by checking if the current String is equal to end goal string
     *
     * @param other StringConfig to be checked if it has already been made
     * @return boolean true if equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        boolean equal = false;
        if(other instanceof StringsConfig otherString){
            equal = otherString.currString.equals(currString);
        }
        return equal;
    }

    /**
     * Makes a unique hashCode for each String
     *
     * @return int the unique hashCode for String
     */
    @Override
    public int hashCode() {
        return currString.hashCode();
    }

    /**
     * Prints the current configuration with proper format.
     *
     * @return String that represents the current String of configuration
     */
    @Override
    public String toString() {
        return currString;
    }
}
