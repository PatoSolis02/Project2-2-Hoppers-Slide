/**
 * @Author: Trevor Kamen
 * @Username: tlk1160
 * @Class: CSCI.142
 * @Filename: Slide.java
 * @Assignment: Project02-2
 * @Language: Java18
 * @Description: Main program for slide puzzle
 */

package puzzles.slide.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.slide.model.SlideConfig;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Call slide puzzle
 *
 * @author Trevor Kamen
 */
public class Slide {

    /**
     * Main Program for Slide Puzzle
     * @pre SlideConfig and Solver exists to determine puzzle output
     * @post Puzzle output printed to terminal screen
     */
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 1) {
            System.out.println("Usage: java Slide filename");
        } else {
            //Initial values
            String filename = args[0];
            SlideConfig initConfig = new SlideConfig(filename);

            //Print filename & the initial configuration
            System.out.println("File: " + filename);
            System.out.print(initConfig);

            //Solver called on puzzle values
            List<Configuration> path = Solver.solver(initConfig);
            System.out.println("Total configs: " + Solver.getTotalCount());
            System.out.println("Unique configs: " + Solver.getUniqueCount());
            if (path.isEmpty()) {
                System.out.println("No solution found!");
            } else {
                for (int i = 0; i < path.size(); i++) {
                    System.out.println("Step " + i + ":\n" + path.get(i));
                }
            }
        }
    }
}
