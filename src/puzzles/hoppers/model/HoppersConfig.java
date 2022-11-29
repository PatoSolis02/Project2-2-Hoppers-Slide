package puzzles.hoppers.model;

import puzzles.common.solver.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

// TODO: implement your HoppersConfig for the common solver

public class HoppersConfig implements Configuration {

    private static String VALID_CELL = ".";

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

    public HoppersConfig(HoppersConfig copy, int currRow, int currCol, int delHRow, int delCRow, int moveRow, int moveCol){

        String colorHopper = copy.grid[currRow][currCol];

        grid = new String[ROW][COL];
        for(int r = 0; r < ROW; r++){
            for(int c = 0; c < COL; c++){
                if(r == currRow && c == currCol){
                    grid[r][c] = VALID_CELL;
                }
                else if(r == delHRow && c == delCRow){
                    grid[r][c] = VALID_CELL;
                }
                else if(r == moveRow && c == moveCol){
                    grid[r][c] = colorHopper;
                } else {
                    grid[r][c] = copy.grid[r][c];
                }
            }
        }

    }

    @Override
    public boolean isSolution() {

        boolean flag = false;

        for(int r = 0; r < ROW; r++){
            for(int c = 0; c < COL; c++){
                if(grid[r][c].equals("G")){
                    return false;
                }
                if(grid[r][c].equals("R")){
                    flag = true;
                }
            }
        }

        return flag;
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        List<Configuration> successors = new LinkedList<>();
        for(int r = 0; r < ROW; r++) {
            for (int c = 0; c < COL; c++) {
                if (grid[r][c].equals("G") || grid[r][c].equals("R")) {
                    if (r % 2 == 0) {
                        if(r + 2 < ROW && r + 4 < ROW ) {
                            if (grid[r + 2][c].equals("G") && grid[r + 4][c].equals(VALID_CELL)) {
                                successors.add(new HoppersConfig(this, r, c, r + 2, c, r + 4, c));
                            }
                        }
                        if(r - 2 >= 0 && r - 4 >= 0) {
                            if (grid[r - 2][c].equals("G") && grid[r - 4][c].equals(VALID_CELL)) {
                                successors.add(new HoppersConfig(this, r, c, r - 2, c, r - 4, c));
                            }
                        }
                        if(c - 2 >= 0 && c - 4 >= 0) {
                            if (grid[r][c - 2].equals("G") && grid[r][c - 4].equals(VALID_CELL)) {
                                successors.add(new HoppersConfig(this, r, c, r, c - 2, r, c - 4));
                            }
                        }
                        if(c + 2 < COL && c + 4 < COL) {
                            if (grid[r][c + 2].equals("G") && grid[r][c + 4].equals(VALID_CELL)) {
                                successors.add(new HoppersConfig(this, r, c, r, c + 2, r, c + 4));
                            }
                        }
                    }
                    if(r + 1 < ROW && c + 1 < COL && r + 2 < ROW && c + 2 < COL) {
                        if (grid[r + 1][c + 1].equals("G") && grid[r + 2][c + 2].equals(VALID_CELL)) {
                            successors.add(new HoppersConfig(this, r, c, r + 1, c + 1, r + 2, c + 2));
                        }
                    }
                    if(r + 1 < ROW && c - 1 >= 0 &&  r + 2 < ROW && c - 2 >= 0) {
                        if (grid[r + 1][c - 1].equals("G") && grid[r + 2][c - 2].equals(VALID_CELL)) {
                            successors.add(new HoppersConfig(this, r, c, r + 1, c - 1, r + 2, c - 2));
                        }
                    }
                    if(r - 1 >= 0 && c + 1 < COL && r - 2 >= 0 && c + 2 < COL) {
                        if (grid[r - 1][c + 1].equals("G") && grid[r - 2][c + 2].equals(VALID_CELL)) {
                            successors.add(new HoppersConfig(this, r, c, r - 1, c + 1, r - 2, c + 2));
                        }
                    }
                    if(r - 1 >= 0 && c - 1 >= 0 && r - 2 >= 0 && c - 2 >= 0) {
                        if (grid[r - 1][c - 1].equals("G") && grid[r - 2][c - 2].equals(VALID_CELL)) {
                            successors.add(new HoppersConfig(this, r, c, r - 1, c - 1, r - 2, c - 2));
                        }
                    }
                }
            }
        }
        return successors;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HoppersConfig that = (HoppersConfig) o;
        return Arrays.deepEquals(grid, that.grid);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(grid);
    }
}
