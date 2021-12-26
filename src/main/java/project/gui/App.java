package project.gui;
import project.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;

public class App extends Application implements IPositionChangeObserver {
    private AbstractWorldMap map;
    private SimulationEngine engine;
    private final GridPane grid = new GridPane();
    Vector2d[] positions;
    GuiElementBox elementCreator = new GuiElementBox();

    @Override
    public void init() {
        try{
            this.map = new BoundedMap(10,10,2,6,15,0.5,10);
            this.positions = new Vector2d[]{new Vector2d(2, 2), new Vector2d(3, 4)};
            this.engine = new SimulationEngine(this.map, 300);
            this.engine.addObserver(this);
        }

        catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
            System.exit(-1);
        }
    }

    public void initializeGrid(){
        this.grid.setGridLinesVisible(false);
        this.grid.setGridLinesVisible(true);
        this.grid.getRowConstraints().clear();
        this.grid.getColumnConstraints().clear();

        int width = map.getWidth();
        int height = map.getHeight();

        for (int i = 1; i <= width+1; i++) {
            grid.getColumnConstraints().add(new ColumnConstraints(70));

            Label xAxis = new Label( String.format("%d", map.getLowerLeft().x+i-1));
            GridPane.setHalignment(xAxis, HPos.CENTER);
            grid.add(xAxis, i, 0, 1, 1);
        }

        for (int i = 1; i <= height + 1; i++) {
            grid.getRowConstraints().add(new RowConstraints(70));

            Label yAxis = new Label(String.format("%d", map.getUpperRight().y - i + 1));
            GridPane.setHalignment(yAxis, HPos.CENTER);
            grid.add(yAxis, 0, i, 1, 1);
        }

        for (int i=1; i<= height+1; i++) {
            for (int j=1; j<= width+1; j++) {
                Vector2d currentPosition = new Vector2d(map.getLowerLeft().x+j-1, map.getUpperRight().y-i+1);
                if (map.isOccupied(currentPosition)) {
                    VBox element = elementCreator.setImages(map.objectAt(currentPosition));
                    GridPane.setHalignment(element, HPos.CENTER);
                    grid.add(element, j, i,1,1);
                }
            }
        }

        Label label = new Label("y\\x");
        grid.add(label, 0, 0, 1, 1);
        grid.getColumnConstraints().add(new ColumnConstraints(70));
        grid.getRowConstraints().add(new RowConstraints(70));
        GridPane.setHalignment(label, HPos.CENTER);
    }


    public HBox createHBoxInterface() {
        TextField directionInput = new TextField();
        Button startButton = new Button("Start");
        HBox hBoxInterface = new HBox(directionInput, startButton);
        hBoxInterface.setAlignment(Pos.CENTER);

        startButton.setOnAction(click -> {
//            ArrayList<MoveDirection> directions = OptionsParser.parse(directionInput.getText().split(" "));
//            this.engine.setDirections(directions);
            Thread engineThread  = new Thread(this.engine);
            engineThread.start();
        });

        return hBoxInterface;
    }

    @Override
    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition) {

    }

    @Override
    public void positionChanged() {
        Platform.runLater(() -> {
            this.grid.getChildren().clear();
            initializeGrid();
        });
    }

    @Override
    public void start(Stage primaryStage) {
        this.grid.setGridLinesVisible(true);
        HBox hBoxInterface = createHBoxInterface();
        initializeGrid();
        VBox vBoxInterface = new VBox(hBoxInterface, this.grid);
        vBoxInterface.setAlignment(Pos.CENTER);
        primaryStage.setScene(new Scene(vBoxInterface));
        primaryStage.show();
    }
}