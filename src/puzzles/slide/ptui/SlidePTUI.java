package puzzles.slide.ptui;

import puzzles.common.Observer;
import puzzles.slide.model.SlideConfig;
import puzzles.slide.model.SlideModel;

import java.io.IOException;
import java.util.Scanner;

public class SlidePTUI implements Observer<SlideModel, String> {
    private SlideModel model;

    public void init(String filename) throws IOException {
        this.model = new SlideModel(filename);
        this.model.addObserver(this);
        System.out.println("Loaded: " + filename);
        System.out.println(model);
        displayHelp();
    }

    @Override
    public void update(SlideModel model, String data) {
        // for demonstration purposes
        System.out.println(data);
        System.out.println(model);
    }

    private void displayHelp() {
        System.out.println( "h(int)              -- hint next move" );
        System.out.println( "l(oad) filename     -- load new puzzle file" );
        System.out.println( "s(elect) r c        -- select cell at r, c" );
        System.out.println( "q(uit)              -- quit the game" );
        System.out.println( "r(eset)             -- reset the current game" );
    }

    public void run() {
        Scanner in = new Scanner( System.in );
        while (this.model.getStatus() == SlideModel.Status.NOT_OVER) {
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

