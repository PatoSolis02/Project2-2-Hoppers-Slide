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

public class HoppersModel {
    /** the collection of observers of this model */
    private final List<Observer<HoppersModel, String>> observers = new LinkedList<>();

    public enum Cell{
        VALID,
        INVALID,
        OCCUPIED
    }

    public enum Status{
        NOT_OVER,
        WON,
        LOST
    }

    /** the current configuration */
    private HoppersConfig currentConfig;
    private Status status;


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

    public HoppersModel(String filename) throws IOException {
        this.currentConfig = new HoppersConfig(filename);
    }

    public Status getStatus() {return status;}

    public void hint(){

        ArrayList<Configuration> solution = Solver.solver(this.currentConfig);
        if(solution.isEmpty()){
            alertObservers("There is no solution!");
        } else {
            this.currentConfig = (HoppersConfig) solution.get(1);
            alertObservers("Successful hint!");
        }
    }

    public void load(String filename){
        try{
            this.currentConfig = new HoppersConfig(filename);
            alertObservers("New file loaded!");
        } catch (FileNotFoundException noFile){
            alertObservers("File could not be read.");
        }
    }

    public void select(int row, int col){

    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();

        String[][] currGrid = currentConfig.getGrid();

        builder.append(' ');
        for(int c = 0; c < currentConfig.getCOL(); c++){
            builder.append(" " + c + " ");
        }
        builder.append('\n');

        builder.append("--".repeat(Math.max(0, currentConfig.getCOL())));
        builder.append('\n');

        for(int r = 0; r < currentConfig.getROW(); r++){
            builder.append(r + "|");
            for(int c = 0; c < currentConfig.getCOL(); c++){
                builder.append(" " + currGrid[r][c] + " ");
            }
            builder.append("\n");
        }


        return builder.toString();
    }

}
