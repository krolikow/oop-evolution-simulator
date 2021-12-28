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
import project.statictics.StatisticsPanel;

public class App extends Application implements IPositionChangeObserver {
    private AbstractWorldMap boundedMap;
    private AbstractWorldMap unboundedMap;
    private SimulationEngine engineBMap;
    private SimulationEngine engineUBMap;
    private final GridPane gridB = new GridPane();
    private final GridPane gridUB = new GridPane();
    private final GuiElementBox elementCreator = new GuiElementBox();
    private StatisticsPanel statisticsPanelBMap;
    private StatisticsPanel statisticsPanelUBMap;
    private final VBox chartBoxBMap = new VBox();
    private final VBox chartBoxUBMap = new VBox();
    private Menu menu;
    private final VBox statisticsBoxBoundedMap = new VBox();
    private final VBox statisticsBoxUnboundedMap = new VBox();

    @Override
    public void init() {

        try{
            this.menu = new Menu();
            this.statisticsPanelBMap = new StatisticsPanel();
            this.statisticsPanelUBMap = new StatisticsPanel();
            this.boundedMap = new BoundedMap(10,10,7,10 ,2,0.5,10);
            this.unboundedMap = new UnBoundedMap(10,10,7,10 ,2,0.5,10);
            this.engineBMap = new SimulationEngine(this.boundedMap, 300,true, statisticsPanelBMap,gridB,statisticsBoxBoundedMap);
            this.engineBMap.addObserver(this);
            this.engineUBMap = new SimulationEngine(this.unboundedMap, 300,true, statisticsPanelUBMap,gridUB,statisticsBoxUnboundedMap);
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

    public void updateStatistics(SimulationEngine engine,AbstractWorldMap map,VBox box){
        Label daysLabel = new Label("Epochs number: " + engine.getDays());
        Label animalNumberLabel = new Label("Animals number: " + engine.getAllAnimalsNumber(map));
        Label plantNumberLabel = new Label("Plant number: " + engine.getAllPlantsNumber(map));
        Label averageChildrenAmountNumberLabel = new Label("Average children amount: " + engine.getAverageChildrenAmount(map));
        Label averageLifeSpanNumberLabel = new Label("Average life span: " + engine.getAverageLifeSpan(map));
        Label averageEnergyLevelNumberLabel = new Label("Average energy level: " + engine.getAverageEnergyLevel(map));
        Label genotypeDominantLabel = new Label("Genotype dominant: " + engine.getGenotypeDominant(map));

        box.getChildren().addAll(daysLabel,animalNumberLabel,plantNumberLabel,averageChildrenAmountNumberLabel,
                averageLifeSpanNumberLabel,averageEnergyLevelNumberLabel,genotypeDominantLabel);
        box.setAlignment(Pos.CENTER);
    }

    @Override
    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition) {}


    @Override
    public void positionChanged(GridPane grid, AbstractWorldMap map, SimulationEngine engine, VBox statistics) {
        Platform.runLater(() -> {
            grid.getChildren().clear();
            statistics.getChildren().clear();
            initializeGrid(grid,map);
            updateStatistics(engine,map,statistics);

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

        HBox hboxB = new HBox(gridB,chartBoxBMap,hBoxInterfaceB);
        updateStatistics(engineBMap,boundedMap,statisticsBoxBoundedMap);
        hboxB.getChildren().addAll(statisticsBoxBoundedMap);

        HBox hboxUB = new HBox(gridUB,chartBoxUBMap,hBoxInterfaceUB);
        updateStatistics(engineUBMap,unboundedMap,statisticsBoxUnboundedMap);
        hboxUB.getChildren().addAll(statisticsBoxUnboundedMap);

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

        VBox mainVBox = new VBox(hboxB,hboxUB);
        mainVBox.setAlignment(Pos.CENTER);
        mainVBox.setSpacing(20);

        stopButtonBMap.setOnAction(click ->{
            engineBMap.switchON();
        });

        stopButtonUBMap.setOnAction(click ->{
            engineUBMap.switchON();
        });


        primaryStage.setScene(new Scene(mainVBox));
        primaryStage.setTitle("Evolution Simulator");
//        primaryStage.setFullScreen(true);
        primaryStage.show();
    }
}