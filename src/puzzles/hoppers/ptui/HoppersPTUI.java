package puzzles.hoppers.ptui;

import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersModel;

public class HoppersPTUI implements Observer<HoppersModel, String> {
    private HoppersModel model;


    public HoppersPTUI(String filename){
        this.model = new HoppersModel(filename);
        initializeView();
    }

    @Override
    public void update(HoppersModel model, String msg) {
    }

    private void initializeView(){this.model.addObserver(this);}

    public void run(){

    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            HoppersPTUI ptui = new HoppersPTUI(args[0]);
            ptui.run();
        }
    }
}
