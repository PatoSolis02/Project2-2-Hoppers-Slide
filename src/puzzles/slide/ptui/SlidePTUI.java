/**
 * @Author: Trevor Kamen
 * @Username: tlk1160
 * @Class: CSCI.142
 * @Filename: SlidePTUI.java
 * @Assignment: Project02-2
 * @Language: Java18
 * @Description: Slide UI console interaction
 */

package puzzles.slide.ptui;

import puzzles.common.Observer;
import puzzles.slide.model.SlideModel;

import java.io.IOException;
import java.util.Scanner;

/**
 * Slide PTUI for terminal interface
 *
 * @author Trevor Kamen
 */
public class SlidePTUI implements Observer<SlideModel, String> {

    /** The puzzle model to handle configuration input */
    private SlideModel model;

    /**
     * Game initializer (essentially constructor)
     * @param filename File path for initial configuration
     * @pre Game model exists
     * @post Model updated to first configuration and game loaded
     */
    public void init(String filename) throws IOException {
        this.model = new SlideModel(filename);
        this.model.addObserver(this);
        System.out.println("Loaded: " + filename);
        System.out.println(model);
        displayHelp();
    }

    /**
     * Displays updated SlideModel with alert message
     * @param model Current game model state
     * @param data Game update message
     * @pre Game model exists
     * @post Displays update
     */
    @Override
    public void update(SlideModel model, String data) {
        System.out.println(data);
        System.out.println(model);
    }

    /**
     * Displays PTUI help message
     * @pre None
     * @post Print help messages
     */
    private void displayHelp() {
        System.out.println( "h(int)              -- hint next move" );
        System.out.println( "l(oad) filename     -- load new puzzle file" );
        System.out.println( "s(elect) r c        -- select cell at r, c" );
        System.out.println( "q(uit)              -- quit the game" );
        System.out.println( "r(eset)             -- reset the current game" );
    }

    /**
     * Runs model commands for PTUI
     * @pre Game model exists
     * @post Model updated with specified action
     */
    public void run() {
        Scanner in = new Scanner( System.in );
        while (true) {
            System.out.print( "> " );
            String line = in.nextLine();
            String[] words = line.split( "\\s+" );
            if (words.length > 0) {
                if (words[0].startsWith( "q" )) {
                    break;
                } else if (words[0].startsWith("s")) {
                    this.model.select(Integer.parseInt(words[1]), Integer.parseInt(words[2]));
                } else if (words[0].startsWith("r")) {
                    this.model.reset();
                    System.out.println("Puzzle reset!");
                    System.out.println(this.model);
                } else if (words[0].startsWith("l")) {
                    this.model.load(words[1]);
                } else if (words[0].startsWith("h")) {
                    this.model.hint();
                }
                else {
                    displayHelp();
                }
            }
        }
    }

    /**
     * PTUI Main
     * @pre Necessary PTUI fields exist
     * @post PTUI launched
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java JamPTUI filename");
        } else {
            try {
                SlidePTUI ptui = new SlidePTUI();
                ptui.init(args[0]);
                ptui.run();
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }
}
