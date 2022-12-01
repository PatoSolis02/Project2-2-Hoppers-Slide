/**
 * @Author: Trevor Kamen
 * @Username: tlk1160
 * @Class: CSCI.142
 * @Filename: SlideConfig.java
 * @Assignment: Project02-2
 * @Language: Java18
 * @Description: Slide Configuration Puzzle
 *              - Determine the shortest route to order ascending puzzle
 *              - Empty ends in bottom right corner
 */

package puzzles.slide.model;

import puzzles.common.solver.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Slide puzzle rules using Configuration
 *
 * @author Trevor Kamen
 */
public class SlideConfig implements Configuration {

    /** Character indicating empty cell as read from initial configuration */
    private final static String EMPTY_CELL = ".";

    /** Row size */
    private static int row;

    /** Column size */
    private static int column;

    /** Puzzle grid of values */
    private final int[][] grid;

    /** Row location for empty value */
    private int emptyRow;

    /** Column location for empty value */
    private int emptyColumn;

    /**
     * Slide constructor
     * @param filename String filename to scan values in from
     * @pre Values provided, fields exist
     * @post Necessary fields initialized
     */
    public SlideConfig(String filename) throws FileNotFoundException {
        Scanner f = new Scanner(new File(filename));
        String temp;
        row = f.nextInt();
        column = f.nextInt();
        this.grid = new int[row][column];
        for (int r = 0; r<row; r++) {
            for (int c = 0; c<column; c++){
                temp = f.next();
                if (temp.equals(EMPTY_CELL)) {
                    this.emptyRow = r;
                    this.emptyColumn = c;
                    this.grid[r][c] = 0;
                } else {
                    this.grid[r][c] = Integer.parseInt(temp);
                }
            }
        }
        f.close();
    }

    /**
     * SlideConfig copy constructor
     * @param copy Copy of slide config to from which to assign the current string
     * @pre Prior constructor and field exists
     * @post Main field updated with determined value
     */
    public SlideConfig(SlideConfig copy) {
        this.emptyRow = copy.emptyRow;
        this.emptyColumn = copy.emptyColumn;
        this.grid = new int[row][column];
        for (int r=0; r<row; r++) {
            System.arraycopy(copy.grid[r], 0, this.grid[r], 0, column);
        }
    }

    /**
     * Determines if value is a solution to the puzzle
     * @pre Configuration exists
     * @post Solution truth determined
     * @return True if current string equal end goal
     */
    @Override
    public boolean isSolution() {
        int tracker = 1; //so it does not conflict with empty=0
        for (int r=0; r<row; r++) {
            for (int c=0; c<column; c++) {
                if (r==row-1 && c==column-1) { //break so no outofBounds occurs
                    break;
                }
                if (tracker != this.grid[r][c]) {
                    return false;
                } else {
                    tracker++;
                }
            }
        }
        return true;
    }

    /**
     * Dictates puzzle rules
     * @pre Copy constructor, configuration exists
     * @post Next path determined
     * @return neighbours - path forward
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        Collection<Configuration> neighbours = new LinkedHashSet<>();
        int north, south, east, west;
        if (this.emptyRow-1 >= 0) { //create north configuration
            SlideConfig tempConfig = new SlideConfig(this);
            north = this.grid[this.emptyRow-1][this.emptyColumn];
            tempConfig.grid[this.emptyRow-1][this.emptyColumn] = 0;
            tempConfig.grid[this.emptyRow][this.emptyColumn] = north;
            tempConfig.emptyRow = this.emptyRow-1;
            neighbours.add(tempConfig);
        } if (this.emptyRow +1 < row) { //create south configuration
            SlideConfig tempConfig = new SlideConfig(this);
            south = this.grid[this.emptyRow+1][this.emptyColumn];
            tempConfig.grid[this.emptyRow+1][this.emptyColumn] = 0;
            tempConfig.grid[this.emptyRow][this.emptyColumn] = south;
            tempConfig.emptyRow = this.emptyRow+1;
            neighbours.add(tempConfig);
        } if (this.emptyColumn-1 >= 0) { //create east configuration
            SlideConfig tempConfig = new SlideConfig(this);
            east = this.grid[this.emptyRow][this.emptyColumn-1];
            tempConfig.grid[this.emptyRow][this.emptyColumn-1] = 0;
            tempConfig.grid[this.emptyRow][this.emptyColumn] = east;
            tempConfig.emptyColumn = this.emptyColumn-1;
            neighbours.add(tempConfig);
        } if (this.emptyColumn+1 < column) { //create west configuration
            SlideConfig tempConfig = new SlideConfig(this);
            west = this.grid[this.emptyRow][this.emptyColumn+1];
            tempConfig.grid[this.emptyRow][this.emptyColumn+1] = 0;
            tempConfig.grid[this.emptyRow][this.emptyColumn] = west;
            tempConfig.emptyColumn = this.emptyColumn+1;
            neighbours.add(tempConfig);
        }
        return neighbours;
    }

    /**
     * Determines if fields are equal
     * @param other object to compare
     * @pre Configuration exists
     * @post Equality determined
     * @return True if current configuration is equal, False otherwise
     */
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof SlideConfig slideConfig) {
            result = Arrays.deepEquals(grid, slideConfig.grid);
        }
        return result;
    }

    /**
     * Creates hashcode from configuration field
     * @pre Required field exists
     * @post Hashcode created
     * @return Hashcode of current configuration
     */
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(grid);
    }

    /**
     * Forms string representation for puzzle
     * @pre Current configuration field exists
     * @post String format of field returned
     * @return Current string representation
     */
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (int r = 0; r<row; r++) {
            for (int c = 0; c<column; c++) {
                int current = this.grid[r][c];
                if (current < 10) {
                    output.append(" ");
                }
                if (current == 0) {
                    output.append(EMPTY_CELL);
                } else {
                    output.append(current);
                }
                output.append(" ");
            }
            output.append("\n");
        }
        return output.toString();
    }

    public boolean isOutofBounds(int r, int c) {
        boolean invalid = false;
        if (!(((r >= 0) && (r < row))
                || ((c >= 0) && (c < column)))) {
            return true;
        }
        return invalid;
    }

    public boolean isSelectionEmpty(int r, int c) {
        return r == this.emptyRow && c == this.emptyColumn;
    }

    public boolean isSecondSelectValid(int firstRow, int firstColumn, int secondRow, int secondColumn) {
        boolean valid = false;
        int changeInRow = secondRow - firstRow;
        int changeInCol = secondColumn - firstColumn;
        if (this.grid[secondRow][secondColumn] == 0) {
            if (changeInRow == -1 || changeInRow == 1 || changeInCol == -1 || changeInCol == 1) {
                valid = true;
            }
        }
        return valid;
    }

    public void makeMove(int firstRow, int firstCol, int secondRow, int secondCol) {
        int tempSave = this.grid[firstRow][firstCol];
        grid[firstRow][firstCol] = grid[secondRow][secondCol];
        grid[secondRow][secondCol] = tempSave;
        this.emptyRow = firstRow;
        this.emptyColumn = firstCol;
    }

    public static int getRow() {
        return row;
    }

    public static int getColumn() {
        return column;
    }

    public int getGrid(int r, int c) {
        return grid[r][c];
    }

    public String getEmptyCellChar() {
        return EMPTY_CELL;
    }
}
