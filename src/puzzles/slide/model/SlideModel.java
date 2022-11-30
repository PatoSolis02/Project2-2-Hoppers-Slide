package puzzles.slide.model;

import puzzles.common.Observer;
import puzzles.common.solver.Solver;
import puzzles.common.solver.Configuration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class SlideModel {
    /** the collection of observers of this model */
    private final List<Observer<SlideModel, String>> observers = new LinkedList<>();

    public enum Status {
        NOT_OVER,
        WON,
        LOST
    }

    /** the current configuration */
    private SlideConfig currentConfig;

    private Status status;

    private boolean firstSelect;

    private int firstSelectRow;
    private int firstSelectColumn;
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
     */
    private void alertObservers(String data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }

    public SlideModel(String filename) throws IOException {
        this.currentConfig = new SlideConfig(filename);
        this.status = Status.NOT_OVER;
        this.firstSelect = true;
        this.filename = filename;
    }

    public void hint(){
        ArrayList<Configuration> solution = Solver.solver(this.currentConfig);
        if (this.currentConfig.isSolution()) {
            alertObservers("Already solved!");
            this.status = Status.WON;
        } else {
            this.currentConfig = (SlideConfig) solution.get(1);
            alertObservers("Next step!");
        }
    }

    public void load(String filename){
        try{
            this.currentConfig = new SlideConfig(filename);
            this.filename = filename;
            alertObservers("Loaded: " + filename);
        } catch (FileNotFoundException noFile){
            alertObservers("File could not be read.");
        }
    }

    public void reset(){
        try{
            this.currentConfig = new SlideConfig(this.filename);
            alertObservers("Loaded: " + this.filename);
        } catch (FileNotFoundException noFile){
            alertObservers("File could not be read.");
        }
    }

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
            if (this.currentConfig.isSecondSelectInValid(this.firstSelectRow, this.firstSelectColumn, row, col)) {
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

    @Override
    public String toString(){
        StringBuilder output = new StringBuilder();
        output.append("    ");
        for (int c = 0; c<SlideConfig.getColumn(); c++) {
            output.append(c).append("  ");
        } output.append("\n  ").append("-".repeat(Math.max(0, (SlideConfig.getColumn() * 3) + 1))).append("\n");
        for (int r = 0; r<SlideConfig.getRow(); r++) {
            output.append(r).append(" |");
            for (int c = 0; c<SlideConfig.getColumn(); c++) {
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

    public Status getStatus() {
        return status;
    }
}
