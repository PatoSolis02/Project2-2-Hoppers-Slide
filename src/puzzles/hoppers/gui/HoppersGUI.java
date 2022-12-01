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

    // for demonstration purposes
    private Image redFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"red_frog.png"));
    private Image greenFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR + "green_frog.png"));
    private Image lilyPad = new Image(getClass().getResourceAsStream(RESOURCES_DIR + "lily_pad.png"));
    private Image water = new Image(getClass().getResourceAsStream(RESOURCES_DIR + "water.png"));

    private HoppersModel model;
    private Button[][] gameBoard;
    private Label message;
    private BorderPane buttons;


    public void init() throws IOException {
        String filename = getParameters().getRaw().get(0);
        this.model = new HoppersModel(filename);
        message = new Label("Loaded: " + filename);
        model.addObserver(this);
    }


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

    public BorderPane makeButtonBorderPane(){
        BorderPane buttonBorderPane = new BorderPane();

        FileChooser fileChooser = new FileChooser();

        Button loadButton = new Button();
        loadButton.setText("Load");

        Button resetButton = new Button();
        resetButton.setText("Reset");
        resetButton.setOnAction(event -> model.reset());

        Button hintButton = new Button();
        hintButton.setText("Hint");
        hintButton.setOnAction(event -> model.hint());

        buttonBorderPane.setLeft(loadButton);
        buttonBorderPane.setCenter(resetButton);
        buttonBorderPane.setRight(hintButton);

        return buttonBorderPane;
    }

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane mainBorderPane = new BorderPane();

        GridPane makeGridPane = makeGridPane();
        buttons = makeButtonBorderPane();
        Button loadButton = (Button) buttons.getLeft();
        loadButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        FileChooser fileChooser = new FileChooser();

                        FileChooser.ExtensionFilter extentionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                        fileChooser.getExtensionFilters().add(extentionFilter);

                        String userDirectoryString = System.getProperty("user.home");
                        File userDirectory = new File(userDirectoryString);
                        if(!userDirectory.canRead()) {
                            userDirectory = new File("c:/");
                        }
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

        mainBorderPane.setTop(message);
        mainBorderPane.setCenter(makeGridPane);
        mainBorderPane.setBottom(buttons);

        Scene scene = new Scene(mainBorderPane);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void update(HoppersModel hoppersModel, String msg) {

        message.setText(msg);
        model = hoppersModel;

        for(int row = 0; row < model.getCurrentConfig().getROW(); row++){
            for(int col = 0; col < model.getCurrentConfig().getCOL(); col++){
                if(model.getCurrentConfig().getGrid(row, col).equals("G")){
                    gameBoard[row][col].setGraphic(new ImageView(greenFrog));
                } else if(model.getCurrentConfig().getGrid(row, col).equals("R")){
                    gameBoard[row][col].setGraphic(new ImageView(redFrog));
                } else if(model.getCurrentConfig().getGrid(row,col).equals(".")){
                    gameBoard[row][col].setGraphic(new ImageView(lilyPad));
                } else {
                    gameBoard[row][col].setGraphic(new ImageView(water));
                }
            }
        }

        if(model.getStatus() == HoppersModel.Status.WON) {
            for (int r = 0; r < model.getCurrentConfig().getROW(); r++) {
                for (int c = 0; c < model.getCurrentConfig().getCOL(); c++) {
                    gameBoard[r][c].setDisable(true);
                }
            }
            buttons.getLeft().setDisable(true);
            buttons.getCenter().setDisable(true);
            buttons.getRight().setDisable(true);
        } else {
            for (int r = 0; r < model.getCurrentConfig().getROW(); r++) {
                for (int c = 0; c < model.getCurrentConfig().getCOL(); c++) {
                    gameBoard[r][c].setDisable(false);
                }
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            Application.launch(args);
        }
    }
}
