package puzzles.hoppers.gui;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersModel;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class HoppersGUI extends Application implements Observer<HoppersModel, String> {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    /** image of red hopper */
    private Image redFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"red_frog.png"));
    /** image of green hopper */
    private Image greenFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR + "green_frog.png"));
    /** image of valid jump spot */
    private Image lilyPad = new Image(getClass().getResourceAsStream(RESOURCES_DIR + "lily_pad.png"));
    /** image of invalid jump spot */
    private Image water = new Image(getClass().getResourceAsStream(RESOURCES_DIR + "water.png"));

    /** gives access to the grid in the model */
    private HoppersModel model;
    /** grid of buttons to make each one distinct and functional */
    private Button[][] gameBoard;
    /** displays the update message given by pressing a button */
    private Label message;
    /** gives access to borderPane containing load, reset, and hint buttons */
    private BorderPane buttons;
    /** the gridPane containing the buttons representing the current model */
    private GridPane gridPane;
    /** the main border pane that holds the message, grid pane, and buttons */
    private BorderPane mainBorderPane;
    /** the stage displaying the scene */
    private Stage stage;


    /**
     * Initializes the GUI
     *
     * @throws IOException
     */
    public void init() throws IOException {
        String filename = getParameters().getRaw().get(0);
        this.model = new HoppersModel(filename);
        message = new Label("Loaded: " + filename);
        model.addObserver(this);
        gridPane = makeGridPane();
    }

    /**
     * Makes a GridPane that is populated with buttons representing the
     * current model and the positions of valid jump spots being
     * represented by lily pads, positions of hoppers represented
     * by frogs (with specific color), and invalid spots represented
     * by water. Each buttons calls model.select() on action with its
     * specified row and col.
     *
     * @return GridPane filled with buttons representing the current model
     */
    public GridPane makeGridPane(){
        GridPane gridPane = new GridPane();
        gameBoard = new Button[model.getCurrentConfig().getROW()][model.getCurrentConfig().getCOL()];
        for(int row = 0; row < model.getCurrentConfig().getROW(); row++){
            for(int col = 0; col < model.getCurrentConfig().getCOL(); col++){
                Button button = new Button();
                int currCol = col;
                int currRow = row;
                if(model.getCurrentConfig().getGrid(row, col).equals("G")){
                    button.setGraphic(new ImageView(greenFrog));
                } else if(model.getCurrentConfig().getGrid(row, col).equals("R")){
                    button.setGraphic(new ImageView(redFrog));
                } else if(model.getCurrentConfig().getGrid(row,col).equals(".")){
                    button.setGraphic(new ImageView(lilyPad));
                } else {
                    button.setGraphic(new ImageView(water));
                }
                button.setOnAction(event -> model.select(currRow, currCol));
                gameBoard[row][col] = button;
                gridPane.add(button, col, row);
            }
        }
        return gridPane;
    }

    /**
     * Makes a BorderPane filled with the load, reset, and hint
     * buttons to be displayed in the main border pane.
     *
     * @return BorderPane filled with necessary buttons
     */
    public BorderPane makeButtonBorderPane(){
        BorderPane buttonBorderPane = new BorderPane();

        /* makes the button for loading and sets action to open file directory with hopper puzzles */
        Button loadButton = new Button();
        loadButton.setText("Load");
        loadButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        FileChooser fileChooser = new FileChooser();

                        FileChooser.ExtensionFilter extentionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                        fileChooser.getExtensionFilters().add(extentionFilter);

                        String userDirectoryString = "data/hoppers";
                        File userDirectory = new File(userDirectoryString);
                        fileChooser.setInitialDirectory(userDirectory);

                        File chosenFile = fileChooser.showOpenDialog(null);

                        String path;
                        if(chosenFile != null) {
                            model.load(chosenFile.getPath());
                        } else {
                            //default return value
                            path = null;
                        }

                    }
                }
        );

        /* makes the reset button that resets the puzzle on action */
        Button resetButton = new Button();
        resetButton.setText("Reset");
        resetButton.setOnAction(event -> model.reset());

        /* makes the hint button that gives a hint on action */
        Button hintButton = new Button();
        hintButton.setText("Hint");
        hintButton.setOnAction(event -> model.hint());

        /* populates the borderPane */
        buttonBorderPane.setLeft(loadButton);
        buttonBorderPane.setCenter(resetButton);
        buttonBorderPane.setRight(hintButton);

        return buttonBorderPane;
    }

    /**
     * Start the stage to display the scene
     *
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        mainBorderPane = new BorderPane();

        /* makes the grid pane */
        gridPane = makeGridPane();
        /* makes the buttons */
        buttons = makeButtonBorderPane();

        /* populates main border pane */
        mainBorderPane.setTop(message);
        mainBorderPane.setCenter(gridPane);
        mainBorderPane.setBottom(buttons);

        /* create scene and stage */
        Scene scene = new Scene(mainBorderPane);
        this.stage = stage;
        this.stage.setScene(scene);
        this.stage.show();
    }

    @Override
    public void update(HoppersModel hoppersModel, String msg) {

        /* sets the message label to action done by user */
        message.setText(msg);
        /* updates to the new model after action */
        model = hoppersModel;
        /* remakes the gridPane to match the new model */
        mainBorderPane.setCenter(makeGridPane());
        /* sets window size to same size as scene */
        stage.sizeToScene();

        /* disables all buttons if the game has been won */
        if(model.getStatus() == HoppersModel.Status.WON) {
            for (int r = 0; r < model.getCurrentConfig().getROW(); r++) {
                for (int c = 0; c < model.getCurrentConfig().getCOL(); c++) {
                    gameBoard[r][c].setDisable(true);
                }
            }
            buttons.getLeft().setDisable(true);
            buttons.getCenter().setDisable(true);
            buttons.getRight().setDisable(true);
        }
    }

    /**
     * The main routine.
     *
     * @param args command line argument filename to be loaded
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            Application.launch(args);
        }
    }
}
