package puzzles.crossing;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * The main Crossing class
 *
 * @author Patricio Solis
 */
public class Crossing {
    /**
     * The main program.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Crossing pups wolves"));
        } else {
            int pups = Integer.parseInt(args[0]); // gets the number of pups from command line args
            int wolves = Integer.parseInt(args[1]); // gets the number of wolves from command line args

            // displays the total number of pups and wolves in puzzle
            System.out.println("Pups: " + pups + ", Wolves: " + wolves);

            // initial configuration
            CrossingConfig initConfig = new CrossingConfig(pups, wolves);

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
