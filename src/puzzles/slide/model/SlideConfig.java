package puzzles.slide.model;

import puzzles.common.solver.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Objects;
import java.util.Scanner;

public class SlideConfig implements Configuration {
    private int row;
    private int column;
    private final String[][] grid;

    public SlideConfig(String filename) throws FileNotFoundException {
        Scanner f = new Scanner(new File(filename));
        this.row = f.nextInt();
        this.column = f.nextInt();
        this.grid = new String[this.row][this.column];
        for (int row = 0; row<this.row; row++) {
            for (int col = 0; col<this.column; col++){
                this.grid[row][col] = f.next();
            }
        }
        f.close();
    }

    public SlideConfig(SlideConfig copy) {
        this.row = copy.row;
        this.column = copy.column;
        this.grid = new String[row][column];
        for (int row=0; row<this.row; row++) {
            System.arraycopy(copy.grid[row], 0, this.grid[row], 0, row);
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
    public int hashCode() {
        return Objects.hash(row, column);
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (int row = 0; row< this.row; this.row++) {
            for (int col = 0; col< column; column++) {
                System.out.println(grid[row][col]);
                output.append(grid[row][col]).append(" ");
            }
        }
        return output.toString();
    }
}
