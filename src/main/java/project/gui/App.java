package project.gui;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
import project.statictics.StatPanel;

public class App extends Application implements IPositionChangeObserver {
    private AbstractWorldMap map;
    private SimulationEngine engine;
    private TextField height,width,startEnergy,moveEnergy,plantEnergy,jungleRatio,initialNumberOfAnimals;
    private final GridPane grid = new GridPane();
    GuiElementBox elementCreator = new GuiElementBox();
    private StatPanel statPanel;

    @Override
    public void init() {

        try{
            statPanel = new StatPanel();
            this.map = new BoundedMap(10,10,7,10,2,0.5,10);
            this.engine = new SimulationEngine(this.map, 300,true,statPanel);
            this.engine.addObserver(this);
        }

//        try{
//            this.map = new BoundedMap(
//                    Integer.parseInt(this.height.getText()),
//                    Integer.parseInt(this.width.getText()),
//                    Integer.parseInt(this.moveEnergy.getText()),
//                    Integer.parseInt(this.plantEnergy.getText()),
//                    Integer.parseInt(this.startEnergy.getText()),
//                    Integer.parseInt(this.jungleRatio.getText()),
//                    Integer.parseInt(this.initialNumberOfAnimals.getText()));
//            this.engine = new SimulationEngine(this.map, 300,true);
//            this.engine.addObserver(this);
//        }

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
            grid.getColumnConstraints().add(new ColumnConstraints(40));

            Label xAxis = new Label( String.format("%d", map.getLowerLeft().x+i-1));
            GridPane.setHalignment(xAxis, HPos.CENTER);
            grid.add(xAxis, i, 0, 1, 1);
        }

        for (int i = 1; i <= height + 1; i++) {
            grid.getRowConstraints().add(new RowConstraints(40));
            Label yAxis = new Label(String.format("%d", map.getUpperRight().y - i + 1));
            GridPane.setHalignment(yAxis, HPos.CENTER);
            grid.add(yAxis, 0, i, 1, 1);
        }
        Pane statPane = new Pane();
//        statPane.setMaxWidth(400);
//        statPane.setMinWidth(400);
//        statPane.maxHeight(400);
//        statPane.minHeight(400);
//        statPane.setBackground(new Background(new BackgroundFill(Color.rgb(102, 204, 100), CornerRadii.EMPTY, Insets.EMPTY)));
        grid.add(statPane,0,0,5,5);
        for (int i=1; i<= height+1; i++) {
            for (int j=1; j<= width+1; j++) {
                Vector2d currentPosition = new Vector2d(map.getLowerLeft().x+j-1, map.getUpperRight().y-i+1);
                Pane pane = new Pane();
                if (map.isInJungle(currentPosition)){
                    pane.setBackground( new Background(new BackgroundFill(Color.rgb(102, 204, 0), CornerRadii.EMPTY, Insets.EMPTY)));
                }
                else{
                    pane.setBackground( new Background(new BackgroundFill(Color.rgb(204, 255, 153), CornerRadii.EMPTY, Insets.EMPTY)));
                }
                    grid.add(pane,j,i,1,1);
                if (map.isOccupied(currentPosition)) {
                    VBox element = elementCreator.setImages(map.objectAt(currentPosition));
                    GridPane.setHalignment(element, HPos.CENTER);
                    grid.add(element, j, i,1,1);
                }
            }
        }


        Label label = new Label("y\\x");
        grid.add(label, 0, 0, 1, 1);
        grid.getColumnConstraints().add(new ColumnConstraints(40));
        grid.getRowConstraints().add(new RowConstraints(40));
        GridPane.setHalignment(label, HPos.CENTER);
    }

    Button stopButton = new Button("Stop");
    public HBox createHBoxInterface() {
        this.width = new TextField();
        this.height = new TextField();
        this.startEnergy = new TextField();
        this.moveEnergy = new TextField();
        this.plantEnergy = new TextField();
        this.jungleRatio = new TextField();

        Button startButton = new Button("Start");

        /*HBox hBoxInterface = new HBox(startButton,stopButton,width,height,startEnergy,moveEnergy,plantEnergy,jungleRatio);*/
        HBox hBoxInterface = new HBox(startButton,stopButton);
        hBoxInterface.setAlignment(Pos.CENTER);

        startButton.setOnAction(click -> {
            Thread engineThread  = new Thread(this.engine);
            engineThread.start();
            engine.setIsON();
        });



        return hBoxInterface;
    }

    @Override
    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition) {}

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

        try {
            statPanel.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        stopButton.setOnAction(click ->{

            engine.switchON();
        });

        primaryStage.setScene(new Scene(vBoxInterface));
        primaryStage.show();
    }
}