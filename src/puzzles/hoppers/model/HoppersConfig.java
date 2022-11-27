package puzzles.hoppers.model;

import puzzles.common.solver.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

// TODO: implement your HoppersConfig for the common solver

public class HoppersConfig implements Configuration {

    public final static char EMPTY_CELL = '.';

    public final static char INVALID_CELL = '*';

    private static int ROW;

    private static int COL;

    private String grid[][];


    public HoppersConfig(String filename) throws FileNotFoundException{
        Scanner f = new Scanner(new File(filename));

        ROW = f.nextInt();
        COL = f.nextInt();

        grid = new String[ROW][COL];
        for(int r = 0; r < ROW; r++){
            for(int c = 0; c < COL; c++){
                String next = f.next();
                grid[r][c] = next;
            }
        }
    }

    public HoppersConfig(){

    }

    @Override
    public boolean isSolution() {
        return false;
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        List<Configuration> successors = new LinkedList<>();
        for(int r = 0; r < ROW; r++){
            for(int c = 0; c < COL; c++){
                if(grid[r][c].equals("G") || grid[r][c].equals("R")){
                    successors.add(checkMove(r, c));
                }
            }
        }

        return successors;
    }

    public Configuration checkMove(int row, int col){
        if(row % 2 == 0){
            // check the 8 possible jumps spots
        }
        // check the 4 possible jumps spots

        return null;
    }

    public boolean isValid(){
        return false;
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
