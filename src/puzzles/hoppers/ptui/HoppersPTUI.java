package puzzles.hoppers.ptui;

import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersModel;

import java.io.IOException;

public class HoppersPTUI implements Observer<HoppersModel, String> {
    private HoppersModel model;


    public HoppersPTUI(String filename) throws IOException {
        this.model = new HoppersModel(filename);
        initializeView();
    }

    @Override
    public void update(HoppersModel model, String msg) {
        System.out.println(msg);
        System.out.println(this.model);
    }

    private void initializeView(){this.model.addObserver(this);}

    public void run(){
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            HoppersPTUI ptui = new HoppersPTUI(args[0]);
            ptui.run();
        }
    }
}
