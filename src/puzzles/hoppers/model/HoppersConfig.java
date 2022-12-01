package puzzles.hoppers.model;

import puzzles.common.solver.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Represents a single configuration in the Hoppers puzzle.
 *
 * @author Patricio Solis
 */
public class HoppersConfig implements Configuration {

    /** the string for a valid jump spot */
    private static String VALID_CELL = ".";
    /** the number of rows in puzzle */
    private static int ROW;
    /** the number of columns in puzzle */
    private static int COL;
    /** grid to be populated */
    private String grid[][];
    /** the row of hopper to be deleted */
    private int rowDelete;
    /** the col of hopper to be deleted */
    private int colDelete;

    /**
     * Constructor. Stores the number of rows and columns in
     * puzzle and populates the grid.
     *
     * @param filename String, the name of file to be read for puzzle
     * @throws FileNotFoundException
     */
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

    /**
     * The copy constructor.
     *
     * @param copy HoppersConfig, the configuration to be copied
     * @param currRow int, the row of hopper that is being moved
     * @param currCol int, the column of hopper that is being moved
     * @param delHRow int, the row of hopper that is being deleted
     * @param delCRow int, the column of hopper that is being deleted
     * @param moveRow int, the row where the hopper (at currRow and currCol) is being moved
     * @param moveCol int, the column where the hopper (at currRow and currCol) is being moved
     */
    public HoppersConfig(HoppersConfig copy, int currRow, int currCol, int delHRow, int delCRow, int moveRow, int moveCol){

        String colorHopper = copy.grid[currRow][currCol]; // remembers the color of hopper being moved

        grid = new String[ROW][COL];
        for(int r = 0; r < ROW; r++){
            for(int c = 0; c < COL; c++){
                if(r == currRow && c == currCol){ // when true means the hopper left this space, so it is now VALID_CELL
                    grid[r][c] = VALID_CELL;
                }
                else if(r == delHRow && c == delCRow){ // when true means the hopper is deleted, so it is now VALID_CELL
                    grid[r][c] = VALID_CELL;
                }
                else if(r == moveRow && c == moveCol){ // when true means the hopper is moved to this spot on grid[][]
                    grid[r][c] = colorHopper;
                } else {
                    grid[r][c] = copy.grid[r][c]; // copies the grid of the configuration to be copied
                }
            }
        }

    }

    /**
     * Gets the number of columns in current puzzle configuration
     *
     * @return int, the total number of columns
     */
    public int getCOL(){return COL;}

    /**
     * Gets the number of rows in current puzzle configuration
     *
     * @return int, the total number of rows
     */
    public int getROW(){return ROW;}

    /**
     * Gets the String at the grid given the specific row and column
     *
     * @param row int, the specific row
     * @param col int, the specific column
     * @return String, the string at specific point in grid
     */
    public String getGrid(int row, int col){return grid[row][col];}


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

    /**
     * Checks if either the given row or column are out
     * of bounds given the size of the grid
     *
     * @param row int, the row to be checked
     * @param col int, the column to be checked
     * @return boolean, true if row and col in bounds, false otherwise
     */
    public boolean isOutOfBounds(int row, int col){
        return row < 0 || col < 0 || row >= ROW || col >= COL;
    }

    /**
     * Checks if the row and column of given selection is a hopper.
     *
     * @param row int, row of grid to be checked
     * @param col int, column of grid to be checked
     * @return boolean, true if selected row and col are a hopper, false otherwise
     */
    public boolean isValidFirstSelection(int row, int col){
        return grid[row][col].equals("G") || grid[row][col].equals("R");
    }

    public boolean isValidSecondSelection(int firstRow, int firstCol, int secondRow, int secondCol) {

        int checkChangeRow = secondRow - firstRow;
        int checkChangeCol = secondCol - firstCol;
        boolean valid = false;

        if (checkChangeRow == -4 && !isOutOfBounds(firstRow - 2, firstCol)){
            if(grid[firstRow - 2][firstCol].equals("G")) {
                valid = true;
                rowDelete = firstRow - 2;
                colDelete = firstCol;
            }
        } else if (checkChangeRow == -2 && checkChangeCol == 2 && !isOutOfBounds(firstRow - 1, firstCol + 1)){
                if(grid[firstRow - 1][firstCol + 1].equals("G")) {
                    valid = true;
                    rowDelete = firstRow - 1;
                    colDelete = firstCol + 1;
                }
        } else if (checkChangeCol == 4 && !isOutOfBounds(firstRow, firstCol + 2)){
               if(grid[firstRow][firstCol + 2].equals("G")) {
                   valid = true;
                   rowDelete = firstRow;
                   colDelete = firstCol + 2;
               }
        } else if (checkChangeRow == 2 && checkChangeCol == 2 && !isOutOfBounds(firstRow + 1, firstCol + 1)){
                if(grid[firstRow + 1][firstCol + 1].equals("G")) {
                    valid = true;
                    rowDelete = firstRow + 1;
                    colDelete = firstCol + 1;
                }
        } else if (checkChangeRow == 4 && !isOutOfBounds(firstRow + 2, firstCol)){
            if (grid[firstRow + 2][firstCol].equals("G")) {
                valid = true;
                rowDelete = firstRow + 2;
                colDelete = firstCol;
            }
        } else if(checkChangeRow == 2 && checkChangeCol == -2 && !isOutOfBounds(firstRow + 1, firstCol - 1)){
                if(grid[firstRow + 1][firstCol - 1].equals("G")) {
                    valid = true;
                    rowDelete = firstRow + 1;
                    colDelete = firstCol - 1;
                }
        } else if(checkChangeRow == -2 && checkChangeCol == -2 && !isOutOfBounds(firstRow - 1, firstCol - 1)){
                if(grid[firstRow - 1][firstCol - 1].equals("G")) {
                    valid = true;
                    rowDelete = firstRow - 1;
                    colDelete = firstCol - 1;
                }
        } else if(!isOutOfBounds(firstRow, firstCol - 2)){
                if(grid[firstRow][firstCol - 2].equals("G")) {
                    valid = true;
                    rowDelete = firstRow;
                    colDelete = firstCol - 2;
                }
        }
        return valid;
    }

    public void makeMove(int firstRow, int firstCol, int secondRow, int secondCol){
        String colorHopper = grid[firstRow][firstCol];
        grid[firstRow][firstCol] = ".";
        grid[secondRow][secondCol] = colorHopper;
        grid[rowDelete][colDelete] = ".";
    }

}
