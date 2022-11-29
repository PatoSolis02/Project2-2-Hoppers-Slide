package puzzles.hoppers.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersConfig;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Hoppers {
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 1) {
            System.out.println("Usage: java Hoppers filename");
        }

        String fileName = args[0];
        System.out.println("File: " + fileName);

        HoppersConfig initConfig = new HoppersConfig(fileName);
        System.out.print(initConfig);

        ArrayList<Configuration> solution = Solver.solver(initConfig);

        if(solution.isEmpty()){
            System.out.println("No solution found!");
        } else {
            for(int i = 0; i < solution.size(); i++){
                System.out.println("Step " + i + ": \n" + solution.get(i));
            }
        }
    }
}
