package puzzles.strings;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.ArrayList;
import java.util.Optional;

/**
 * The main Strings class
 *
 * @author Patricio Solis
 */
public class Strings {
    /**
     * The main program.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Strings start finish"));
        } else {
            String start = args[0]; // gets the starting string
            String end = args[1]; // gets the desired final string

            // displays starting string and goal string in format
            System.out.println("Start: " + start + ", End: " + end);

            // initial configuration
            StringsConfig initConfig = new StringsConfig(start, end);

            // gets the configurations that lead to a solution in an ArrayList
            ArrayList<Configuration> solution = Solver.solver(initConfig);

            // checks for no solution
            if(solution.isEmpty()){
                System.out.println("No solution found!");
            } else {
                // Prints out configurations in order to get to solution in specified format
                for(int i = 0; i < solution.size(); i++){
                    System.out.println("Step " + i + ": " + solution.get(i).toString());
                }
            }

        }
    }
}
