package project.gui;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import project.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import project.statictics.StatPanel;

public class App extends Application implements IPositionChangeObserver {
    private AbstractWorldMap boundedMap;
    private AbstractWorldMap unboundedMap;
    private SimulationEngine engineBMap;
    private SimulationEngine engineUBMap;
    private final GridPane gridB = new GridPane();
    private final GridPane gridUB = new GridPane();
    private final GuiElementBox elementCreator = new GuiElementBox();
    private StatPanel statisticsPanelBMap;
    private StatPanel statisticsPanelUBMap;
    private final VBox chartBoxBMap = new VBox();
    private final VBox chartBoxUBMap = new VBox();
    private Menu menu;

    @Override
    public void init() {

        try{
            this.menu = new Menu();
            this.statisticsPanelBMap = new StatPanel();
            this.statisticsPanelUBMap = new StatPanel();
            this.boundedMap = new BoundedMap(10,10,7,10 ,2,0.5,10);
            this.unboundedMap = new BoundedMap(10,10,7,10 ,2,0.5,10);
            this.engineBMap = new SimulationEngine(this.boundedMap, 300,true, statisticsPanelBMap,gridB);
            this.engineBMap.addObserver(this);
            this.engineUBMap = new SimulationEngine(this.unboundedMap, 300,true, statisticsPanelUBMap,gridUB);
            this.engineUBMap.addObserver(this);
        }

//        try{
//            this.map = new BoundedMap(
//                    Integer.parseInt(menu.height.getText()),
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

    public void initializeGrid(GridPane grid,AbstractWorldMap map){
        grid.setGridLinesVisible(false);
        grid.setGridLinesVisible(true);
        grid.getRowConstraints().clear();
        grid.getColumnConstraints().clear();

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
//        statPane.getChildren().add(statPanel);
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
    Button stopButtonBMap = new Button("Stop");
    Button startButtonBMap = new Button("Start");
    Button stopButtonUBMap = new Button("Stop");
    Button startButtonUBMap = new Button("Start");



    public HBox createHBoxInterface(Button startButton,Button stopButton,SimulationEngine engine) {

        HBox hBoxInterface = new HBox(startButton,stopButton);
        hBoxInterface.setAlignment(Pos.CENTER);

        startButton.setOnAction(click -> {Thread engineThread  = new Thread(engine);
            engineThread.start();
            engine.setIsON();
        });

        return hBoxInterface;
    }


    @Override
    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition) {}

    @Override
    public void positionChanged() {

    }

//    @Override
//    public void positionChanged() {
//        Platform.runLater(() -> {
//            gridB.getChildren().clear();
//            initializeGrid(gridB,unboundedMap);
//        });
//
//        Platform.runLater(() -> {
//            gridUB.getChildren().clear();
//            initializeGrid(gridUB,boundedMap);
//        });
//    }


    @Override
    public void positionChanged(GridPane grid, AbstractWorldMap map) {
        Platform.runLater(() -> {
            grid.getChildren().clear();
            initializeGrid(grid,map);
        });
    }

    @Override
    public void start(Stage primaryStage) {
        this.gridUB.setGridLinesVisible(true);
        this.gridB.setGridLinesVisible(true);

        HBox hBoxInterfaceB = createHBoxInterface(startButtonBMap,stopButtonBMap,engineBMap);
        initializeGrid(gridB,boundedMap);

        HBox hBoxInterfaceUB = createHBoxInterface(startButtonUBMap,stopButtonUBMap,engineUBMap);
        initializeGrid(gridUB,unboundedMap);

        try {
            statisticsPanelBMap.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            statisticsPanelUBMap.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            menu.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        chartBoxUBMap.getChildren().addAll(this.statisticsPanelUBMap.getChart());
        chartBoxBMap.getChildren().addAll(this.statisticsPanelBMap.getChart());
        HBox hBoxInterface = new HBox(hBoxInterfaceB,hBoxInterfaceUB, this.gridB,this.gridUB, chartBoxBMap,chartBoxUBMap);
        hBoxInterface.setAlignment(Pos.CENTER);
        stopButtonBMap.setOnAction(click ->{
            engineBMap.switchON();
        });

        stopButtonUBMap.setOnAction(click ->{
            engineUBMap.switchON();
        });


        primaryStage.setScene(new Scene(hBoxInterface));
        primaryStage.setTitle("Evolution Simulator");
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }
}