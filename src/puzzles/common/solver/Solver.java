package puzzles.common.solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * The main Solver class
 *
 * @author Patricio Solis
 */
public class Solver {

    private static int uniqueCount;
    private static int totalCount;

    /**
     * Solves the puzzle given an initial configuration using BFS
     *
     * @param start initial configuration of puzzle
     * @return ArrayList<Configuration> the list of configurations that lead to the solution in order
     */
    public static ArrayList<Configuration> solver(Configuration start){

        HashMap<Configuration, Configuration> predecessor = new HashMap<>(); // creates predecessor map
        predecessor.put(start, null); // puts initial configuration as the start of map

        Queue<Configuration> queue = new LinkedList<>(); //queue for configurations to be tested
        queue.add(start); // initial configuration first one to be tested

        int totalConfig = 1; // counts the total number of configurations made
        while(!queue.isEmpty() && !queue.peek().isSolution()){ // loops until no more configurations or solution is found
            Configuration currConfig = queue.remove();
            for(Configuration neighbor : currConfig.getNeighbors()){
                totalConfig += 1;
                if(!predecessor.containsKey(neighbor)){ // checks if configuration is unique or not
                    predecessor.put(neighbor, currConfig); // adds configuration to map if unique
                    queue.add(neighbor);
                }
            }
        }
        totalCount = totalConfig;
        uniqueCount = predecessor.size();
        if(queue.isEmpty()){ // if no solution is found then it returns an empty ArrayList
            return new ArrayList<>();
        } else { // if there is a solution it builds the list in reverse order
            ArrayList<Configuration> path = new ArrayList<>();
            path.add(0, queue.peek());
            Configuration config = predecessor.get(queue.peek());
            while(config != null){
                path.add(0, config);
                config = predecessor.get(config);
            }
            return path;
        }
    }

    /**
     * Unique Count accessor
     * @return Count of unique configurations created
     */
    public static int getUniqueCount() {
        return uniqueCount;
    }

    /**
     * Total Count accessor
     * @return Count of total configurations created
     */
    public static int getTotalCount() {
        return totalCount;
    }
}

