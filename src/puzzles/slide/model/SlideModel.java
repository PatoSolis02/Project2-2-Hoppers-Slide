/**
 * @Author: Trevor Kamen
 * @Username: tlk1160
 * @Class: CSCI.142
 * @Filename: SlideModel.java
 * @Assignment: Project02-2
 * @Language: Java18
 * @Description: Slide model to handle UI updates over configurations
 */

package puzzles.slide.model;

import puzzles.common.Observer;
import puzzles.common.solver.Solver;
import puzzles.common.solver.Configuration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Slide model for model-view-controller method
 *
 * @author Trevor Kamen
 */
public class SlideModel {

    /** the collection of observers of this model */
    private final List<Observer<SlideModel, String>> observers = new LinkedList<>();

    /** Game Status enums to manage game over states */
    public enum Status {
        NOT_OVER,
        WON,
    }

    /** the current configuration */
    private SlideConfig currentConfig;

    /** The current game status */
    private Status status;

    /** Determines if the first coordinate has been selected */
    private boolean firstSelect;

    /** First selected row coordinate */
    private int firstSelectRow;

    /** First selected column coordinate */
    private int firstSelectColumn;

    /** Stores first provided filename for reset and later display */
    private String filename;

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<SlideModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     *
     * @param data Update message
     */
    private void alertObservers(String data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }

    /**
     * Slide Model constructor
     * @param filename The file path for which to construct a configuration
     * @pre SlideConfig exists to pull from
     * @post Fills necessary model fields
     */
    public SlideModel(String filename) throws IOException {
        this.currentConfig = new SlideConfig(filename);
        this.status = Status.NOT_OVER;
        this.firstSelect = true;
        this.filename = filename;
    }

    /**
     * Provides hint for current configuration puzzle state
     * @pre Configuration exists and solver exists
     * @post Determines the shortest path to solve for configuration and provides next step
     */
    public void hint(){
        ArrayList<Configuration> solution = Solver.solver(this.currentConfig);
        if (this.currentConfig.isSolution()) {
            alertObservers("Already solved!");
            this.status = Status.WON;
        } else if (!solution.isEmpty()) {
            this.currentConfig = (SlideConfig) solution.get(1);
            alertObservers("Next step!");
        } else {
            alertObservers("Not Solvable!");
        }
    }

    /**
     * Loads file for configuration
     * @param filename File path to load new configuration from
     * @pre Configuration & puzzle file exists
     * @post Puzzle updated
     */
    public void load(String filename){
        try{
            this.currentConfig = new SlideConfig(filename);
            this.filename = filename;
            alertObservers("Loaded: " + filename);
            this.status = Status.NOT_OVER;
        } catch (FileNotFoundException noFile){
            alertObservers("File could not be read.");
        }
    }

    /**
     * Resets current puzzle
     * @pre Configuration & puzzle file exists
     * @post Puzzle reloaded using past file
     */
    public void reset(){
        try{
            this.currentConfig = new SlideConfig(this.filename);
            alertObservers("Puzzle: " + this.filename + " Reset!");
            this.status = Status.NOT_OVER;
        } catch (FileNotFoundException noFile){
            alertObservers("File could not be read.");
        }
    }

    /**
     * Performs selection switch between two coordinate points
     * @param row Provided row coordinate
     * @param col Provided column coordinate
     * @pre Configuration exists
     * @post Puzzle updated
     */
    public void select(int row, int col) {
        if (this.firstSelect) {
            if (this.currentConfig.isOutofBounds(row, col)) {
                alertObservers("Invalid selection (" + row + ", " + col + ")");
            } else if (this.currentConfig.isSelectionEmpty(row, col)) {
                alertObservers("No number at (" + row + ", " + col + ")");
            } else {
                alertObservers("Selected (" + row + ", " + col + ")");
                this.firstSelect = false;
                this.firstSelectRow = row;
                this.firstSelectColumn = col;
            }
        } else {
            if (this.currentConfig.isOutofBounds(row, col)) {
                alertObservers("Invalid selection (" + row + ", " + col + ")");
            }
            if (!this.currentConfig.isSecondSelectValid(this.firstSelectRow, this.firstSelectColumn, row, col)) {
                alertObservers("Can't move from (" + this.firstSelectRow + ", " + this.firstSelectColumn + ") to ("
                        + row + ", " + col + ")");
            } else {
                this.currentConfig.makeMove(this.firstSelectRow, this.firstSelectColumn, row, col);
                alertObservers("Moved from (" + this.firstSelectRow + ", " + this.firstSelectColumn + ") to ("
                        + row + ", " + col + ")");
                if (this.currentConfig.isSolution()) {
                    this.status = Status.WON;
                    alertObservers("You won!");
                }
            }
            this.firstSelect = true;
        }
    }

    /**
     * Forms model string representation for puzzle
     * @pre Current configuration field exists
     * @post String format of field returned
     * @return Current string representation
     */
    @Override
    public String toString(){
        StringBuilder output = new StringBuilder();
        output.append("    ");
        for (int c = 0; c<this.currentConfig.getColumn(); c++) {
            output.append(c).append("  ");
        } output.append("\n  ").append("-".repeat(Math.max(0, (this.currentConfig.getColumn() * 3) + 1))).append("\n");
        for (int r = 0; r<this.currentConfig.getRow(); r++) {
            output.append(r).append(" |");
            for (int c = 0; c<this.currentConfig.getColumn(); c++) {
                int current = currentConfig.getGrid(r, c);
                if (current < 10) {
                    output.append(" ");
                }
                if (current == 0) {
                    output.append(currentConfig.getEmptyCellChar());
                } else {
                    output.append(current);
                }
                output.append(" ");
            }
            output.append("\n");
        }
        return output.toString();
    }

    /**
     * Current game status accessor
     * @pre Game status field exists
     * @post Current game status provided
     * @return Current game status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Provides current game configuration
     * @pre Configuration class exists
     * @post Current game configuration provided
     * @return Current game configuration
     */
    public SlideConfig getCurrentConfig() {
        return this.currentConfig;
    }
}
