package puzzles.slide.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.slide.model.SlideConfig;

import java.io.FileNotFoundException;
import java.util.List;

public class Slide {
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 1) {
            System.out.println("Usage: java Slide filename");
        } else {
            String filename = args[0];
            SlideConfig initConfig = new SlideConfig(filename);
            System.out.println(initConfig);
//            List<Configuration> path = Solver.solver(initConfig);
//            System.out.println("File: " + filename);
//            if (path.isEmpty()) {
//                System.out.println("No solution found!");
//            } else {
//                for (int i = 0; i < path.size(); i++) {
//                    System.out.println("Step " + i + ": " + path.get(i));
//                }
//            }
        }
    }
}
