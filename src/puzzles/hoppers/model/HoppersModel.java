package puzzles.hoppers.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * The model for the Hoppers game.
 *
 * @author Patricio Solis
 */
public class HoppersModel {
    /** the collection of observers of this model */
    private final List<Observer<HoppersModel, String>> observers = new LinkedList<>();

    /** the game status */
    public enum Status{
        NOT_OVER,
        WON
    }

    /** the current configuration */
    private HoppersConfig currentConfig;
    /** current game status */
    private Status status;
    /** true if first selection of hopper; false otherwise */
    private boolean firstSelect;
    /** the row of first selected hopper */
    private int firstSelectRow;
    /** the col of first selected hopper */
    private int firstSelectCol;
    /** the filename of most recently loaded file */
    private String filename;


    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<HoppersModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String msg) {
        for (var observer : observers) {
            observer.update(this, msg);
        }
    }

    /**
     * Initializes the model with given Hoppers puzzle file
     *
     * @param filename the hoppers file to be loaded
     * @throws IOException
     */
    public HoppersModel(String filename) throws IOException {
        this.currentConfig = new HoppersConfig(filename);
        this.filename = filename;
        this.status = Status.NOT_OVER;
        this.firstSelect = true;
    }

    /**
     * Get the current status of the game
     * @return Status; the current state of game
     */
    public Status getStatus() {return status;}

    /**
     * Get the current Hoppers configuration of model
     *
     * @return HoppersConfig; the current configuration
     */
    public HoppersConfig getCurrentConfig(){return this.currentConfig;}

    /**
     * Solves the current configuration of model and if it is solvable it
     * makes the model's currentConfiguration to the next step in the solution
     * it also updates the game state if the puzzle has been solved.
     *
     */
    public void hint(){
        ArrayList<Configuration> solution = Solver.solver(this.currentConfig);
        if(this.currentConfig.isSolution()){
            alertObservers("Solved!");
            this.status = Status.WON;
        } else if(!solution.isEmpty()) {
            this.currentConfig = (HoppersConfig) solution.get(1);
            alertObservers("Successful hint!");
        } else {
            alertObservers("Not Solvable!");
        }
    }

    /**
     * Loads the given hoppers puzzle file and makes the currentConfig
     * to the new configuration given by the file. It also changes the
     * model's saved filename to the file that was loaded. If the file
     * could not be found it catches the error.
     *
     * @param filename String; the filename to be loaded
     */
    public void load(String filename){
        try{
            this.currentConfig = new HoppersConfig(filename);
            this.filename = filename;
            alertObservers("New file loaded!");
        } catch (FileNotFoundException noFile){
            alertObservers("File could not be read.");
        }
    }

    /**
     * If it is the first selection, it checks if the selection
     * is out of bounds and if it is a valid selection of a hopper,
     * and if it is valid it alerts the observers and stores the
     * row and column of the hopper selected and tells the model
     * that the first selection has been made. On the second selection,
     * it checks if the selection is out of bounds and if it is a valid
     * second selection. If it is a valid second selection, it makes the
     * hopper move from first selection to second selection. After a move
     * has been made, it checks if the updated currentConfig is a solution
     * and if it is it changes the game status to WON.
     *
     * @param row int; the row of selection
     * @param col int; the column of selection
     */
    public void select(int row, int col){
        if(firstSelect){
            if(this.currentConfig.isOutOfBounds(row, col)){
                alertObservers("Selection (" + row + " , " + col + ") is out of bounds.");
            } else if(this.currentConfig.isValidFirstSelection(row, col)){
                alertObservers("Selected hopper at (" + row + ", " + col + ")");
                this.firstSelect = false;
                this.firstSelectRow = row;
                this.firstSelectCol = col;
            } else {
                alertObservers("Invalid selection");
            }
        } else {
            if(this.currentConfig.isOutOfBounds(row, col)){
                alertObservers("Selection (" + row + " , " + col + ") is out of bounds.");
            } else if(this.currentConfig.isValidSecondSelection(firstSelectRow, firstSelectCol, row, col)){
                this.currentConfig.makeMove(firstSelectRow, firstSelectCol, row, col);
                alertObservers("Jumped from (" + firstSelectRow + ", " + firstSelectCol + ") to (" + row + ", " + col + ")");
            } else {
                alertObservers("Can't jump from (" + firstSelectRow + ", " + firstSelectCol + ") to (" + row + ", " + col + ")");
            }
            this.firstSelect = true;
        }
        if(this.currentConfig.isSolution()){
            this.status = Status.WON;
            alertObservers("YOU WON!");
        }
    }

    /**
     * Resets the puzzle to the original state of the most
     * recently loaded hoppers file and makes sure that the
     * status of the game is NOT_OVER.
     *
     */
    public void reset(){
        try{
            this.currentConfig = new HoppersConfig(filename);
            alertObservers("Puzzle "  + filename + " reset!");
            this.status = Status.NOT_OVER;
        } catch (FileNotFoundException noFile){
            alertObservers("File could not be reset.");
        }
    }

    /**
     * Returns a string representation of the board, suitable for printing out.
     *
     * @return the string representation
     */
    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();

        builder.append("  ");
        for(int c = 0; c < currentConfig.getCOL(); c++){
            builder.append(" " + c + " ");
        }
        builder.append('\n');

        builder.append("  ");
        for(int c = 0; c < currentConfig.getCOL(); c++){
            builder.append("---");
        }
        builder.append('\n');

        for(int r = 0; r < currentConfig.getROW(); r++){
            builder.append(r + "|");
            for(int c = 0; c < currentConfig.getCOL(); c++){
                builder.append(" " + currentConfig.getGrid(r, c) + " ");
            }
            builder.append("\n");
        }


        return builder.toString();
    }

}
