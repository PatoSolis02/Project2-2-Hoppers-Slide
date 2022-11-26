package puzzles.slide.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import puzzles.common.Observer;
import puzzles.slide.model.SlideModel;

public class SlideGUI extends Application implements Observer<SlideModel, String> {
    private SlideModel model;

    /** The size of all icons, in square dimension */
    private final static int ICON_SIZE = 75;
    /** the font size for labels and buttons */
    private final static int BUTTON_FONT_SIZE = 20;
    private final static int FONT_SIZE = 12;
    private final static int NUMBER_FONT_SIZE = 24;
    /** Colored buttons */
    private final static String EVEN_COLOR = "#ADD8E6";
    private final static String ODD_COLOR = "#FED8B1";
    private final static String EMPTY_COLOR = "#FFFFFF";

    private Stage stage;

    @Override
    public void init() {
        // get the file name from the command line
        String filename = getParameters().getRaw().get(0);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        Button button1 = new Button();
        button1.setStyle(
                "-fx-font-family: Arial;" +
                "-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                        "-fx-background-color: " + ODD_COLOR + ";" +
                        "-fx-font-weight: bold;");
        button1.setText(String.valueOf(1));
        button1.setMinSize(ICON_SIZE, ICON_SIZE);
        button1.setMaxSize(ICON_SIZE, ICON_SIZE);
        Scene scene = new Scene(button1);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void update(SlideModel model, String data) {

    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
