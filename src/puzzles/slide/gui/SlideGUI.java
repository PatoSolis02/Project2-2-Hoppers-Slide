/**
 * @Author: Trevor Kamen
 * @Username: tlk1160
 * @Class: CSCI.142
 * @Filename: SlideGUI.java
 * @Assignment: Project02-2
 * @Language: Java18
 * @Description: Slide GUI interaction through JavaFX
 */

package puzzles.slide.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import puzzles.common.Observer;
import puzzles.slide.model.SlideModel;

import java.io.File;
import java.io.IOException;

/**
 * Slide GUI for graphical interface
 *
 * @author Trevor Kamen
 */
public class SlideGUI extends Application implements Observer<SlideModel, String> {

    /** The puzzle model to handle configuration input */
    private SlideModel model;

    /** The size of all icons, in square dimension */
    private final static int ICON_SIZE = 75;

    /** The font size for labels and buttons */
    private final static int BUTTON_FONT_SIZE = 20;

    /** Font size for update messages */
    private final static int FONT_SIZE = 12;

    /** Even tile color */
    private final static String EVEN_COLOR = "#ADD8E6";

    /** Odd tile color */
    private final static String ODD_COLOR = "#FED8B1";

    /** Empty tile color */
    private final static String EMPTY_COLOR = "#FFFFFF";

    /** Tile border color and style */
    private final Border buttonBorder = new Border(new BorderStroke(
            Color.LIGHTGOLDENRODYELLOW, BorderStrokeStyle.DASHED, new CornerRadii(2), BorderStroke.DEFAULT_WIDTHS));

    /** Status font style settings */
    private static final Font specialFont = Font.font("Ink Free", FontWeight.BOLD, FONT_SIZE);

    /** Game tile array of buttons */
    private Button[][] gameBoard;

    /** Row dimension of current configuration */
    private int NUM_ROWS;

    /** Column dimension of current configuration */
    private int NUM_COLS;

    /** Update message text */
    private final Text message = new Text();

    /** Main boarder pain for GUI layout */
    private BorderPane mainPane;

    /** Status area buttons HBox to center */
    private HBox centerStatusArea;

    /** Boolean to determine if puzzle is solved and disable select areas */
    private boolean statusSelect = false;

    /** General GUI staging area */
    private Stage stage;

    /**
     * Game initializer (essentially constructor)
     * @pre Game model exists
     * @post Model updated to first configuration and game loaded
     */
    @Override
    public void init() throws IOException {
        // get the file name from the command line
        String filename = getParameters().getRaw().get(0);
        this.model = new SlideModel(filename);
        this.NUM_ROWS = this.model.getCurrentConfig().getRow();
        this.NUM_COLS = this.model.getCurrentConfig().getColumn();
        this.model.addObserver(this);
    }

    /**
     * Starts GUI load
     * @param stage Gui staging area
     * @pre Game model & GUI definitions exist
     * @post Model updated to first configuration, game loaded, GUI displayed
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        this.mainPane = new BorderPane();
        GridPane slideGrid = makeBoard();
        BorderPane statusPane = makeStatusPane();

        BorderPane messageArea = new BorderPane();
        this.message.setFill(Color.DARKOLIVEGREEN);
        this.message.setFont(specialFont);
        messageArea.setCenter(this.message);
        this.mainPane.setTop(messageArea);
        this.mainPane.setCenter(slideGrid);
        this.mainPane.setBottom(statusPane);

        Scene scene = new Scene(this.mainPane);
        this.stage.setScene(scene);
        this.stage.setTitle("Slide GUI");
        this.stage.setResizable(false);
        this.stage.show();
    }

    /**
     * Constructs GUI tiles for current game configuration
     * @pre Game model exists
     * @post GUI updated to configuration. Button action set to model select
     */
    private GridPane makeBoard() {
        GridPane slideGrid = new GridPane();
        this.gameBoard = new Button[NUM_ROWS][NUM_COLS];
        for (int r = 0; r < NUM_ROWS; ++r) {
            for (int c = 0; c < NUM_COLS; ++c) {
                Button button = new Button();
                int value = this.model.getCurrentConfig().getGrid(r, c);
                int finalRow = r;
                int finalCol = c;
                if (value == 0) {
                    button.setStyle(
                            "-fx-font-family: Arial;" +
                                    "-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                                    "-fx-background-color: " + EMPTY_COLOR + ";" +
                                    "-fx-font-weight: bold;");
                } else if (value%2 == 0) {
                    button.setStyle(
                            "-fx-font-family: Arial;" +
                                    "-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                                    "-fx-background-color: " + EVEN_COLOR + ";" +
                                    "-fx-font-weight: bold;");
                    button.setText(String.valueOf(value));
                } else {
                    button.setStyle(
                            "-fx-font-family: Arial;" +
                                    "-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                                    "-fx-background-color: " + ODD_COLOR + ";" +
                                    "-fx-font-weight: bold;");
                    button.setText(String.valueOf(value));
                }
                button.setMinSize(ICON_SIZE, ICON_SIZE);
                button.setMaxSize(ICON_SIZE, ICON_SIZE);
                button.setOnAction(event -> this.model.select(finalRow, finalCol));
                button.setBorder(buttonBorder);
                this.gameBoard[r][c] = button;
                slideGrid.add(button, c, r);
            }
        }
        return slideGrid;
    }

    /**
     * Constructs GUI status pane for button commands (load, reset, hint)
     * @pre Game model exists
     * @post Buttons set to area. Necessary actions assigned
     */
    private BorderPane makeStatusPane() {
        BorderPane statusPane = new BorderPane();
        this.centerStatusArea = new HBox();

        //Load button set
        Button load = new Button();
        load.setText("Load");
        load.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();

            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extensionFilter);

            String userDirectoryString = "data/slide";
            File userDirectory = new File(userDirectoryString);
            fileChooser.setInitialDirectory(userDirectory);

            File chosenFile = fileChooser.showOpenDialog(null);

            if (chosenFile != null) {
                String path = chosenFile.getPath();
                model.load(path.substring(path.indexOf("data")));
            }
        });

        //Rest button set
        Button reset = new Button();
        reset.setText("Reset");
        reset.setOnAction(event -> {
            this.statusSelect = true;
            this.model.reset();
        });

        //Hint button set
        Button hint = new Button();
        hint.setText("Hint");
        hint.setOnAction(event -> this.model.hint());

        this.centerStatusArea.getChildren().add(load);
        this.centerStatusArea.getChildren().add(reset);
        this.centerStatusArea.getChildren().add(hint);
        this.centerStatusArea.setAlignment(Pos.CENTER);

        statusPane.setCenter(this.centerStatusArea);
        return statusPane;
    }

    /**
     * Update action for GUI interactions
     * @pre Game model exists
     * @post Necessary GUI fields updated for required conditions
     */
    @Override
    public void update(SlideModel model, String message) {
        this.message.setText(message);
        this.NUM_ROWS = this.model.getCurrentConfig().getRow();
        this.NUM_COLS = this.model.getCurrentConfig().getColumn();
        this.model = model;
        this.mainPane.setCenter(makeBoard());
        this.stage.sizeToScene();

        if (this.statusSelect) { //Renables areas for new game
            for (int r = 0; r < NUM_ROWS; r++) {
                for (int c = 0; c < NUM_COLS; c++) {
                    this.gameBoard[r][c].setDisable(false);
                }
            }
            this.centerStatusArea.getChildren().get(2).setDisable(false);
        }

        if (this.model.getStatus() == SlideModel.Status.WON) {
            this.statusSelect = false;
            for (int r = 0; r < NUM_ROWS; r++) {
                for (int c = 0; c < NUM_COLS; c++) {
                    this.gameBoard[r][c].setDisable(true);
                }
            }
            this.centerStatusArea.getChildren().get(2).setDisable(true); //Specifically disables help button
        }
    }

    /**
     * GUI Main
     * @pre Necessary GUI fields exist
     * @post GUI launched
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Enter SlidePTUI filename (.txt)");
        } else {
            Application.launch(args);
        }
    }
}
