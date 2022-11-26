package puzzles.hoppers.model;

import puzzles.common.solver.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

// TODO: implement your HoppersConfig for the common solver

public class HoppersConfig implements Configuration {

    public final static char EMPTY_CELL = '.';

    public final static char INVALID_CELL = '*';

    private static int ROW;

    private static int COL;

    private String grid[][];

    private HashMap<String, Array> hoppers;

    public HoppersConfig(String filename) throws FileNotFoundException{
        Scanner f = new Scanner(new File(filename));

        ROW = f.nextInt();
        COL = f.nextInt();

        grid = new String[ROW][COL];
        for(int r = 0; r < ROW; r++){
            for(int c = 0; c < COL; c++){
                grid[r][c] = f.next();
            }
        }
    }

    @Override
    public boolean isSolution() {
        return false;
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        return null;
    }

    @Override
    public String toString(){

        for(int r = 0; r < ROW; r++){
            for(int c = 0; c < COL; c++){
                System.out.print(grid[r][c] + " ");
            }
            System.out.println("");
        }
        return "";
    }
}
