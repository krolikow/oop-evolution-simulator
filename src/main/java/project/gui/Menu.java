package project.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Menu extends Application {
    private final TextField height = new TextField("10");
    private final TextField width = new TextField("10");
    private final TextField startEnergy = new TextField("30");
    private final TextField moveEnergy = new TextField("3");
    private final TextField plantEnergy = new TextField("8");
    private final TextField jungleRatio = new TextField("0.5");
    private final TextField initialNumberOfAnimals = new TextField("40");

    private final Label heightLabel = new Label("Height of map:  ");
    private final Label widthLabel = new Label("Width of map:  ");
    private final Label startEnergyLabel = new Label("Start energy:  ");
    private final Label moveEnergyLabel = new Label("Move energy:  ");
    private final Label plantEnergyLabel = new Label("Plant energy:  ");
    private final Label jungleRatioLabel = new Label("Jungle ratio:  ");
    private final Label initialNumberOfAnimalsLabel = new Label("Initial number of animals:  ");


    private VBox menu;

    @Override
    public void start(Stage primaryStage) throws Exception {
        menu = new VBox(10);
        Label mainLabel = new Label("Enter initial params:");
        mainLabel.setFont(new Font("Arial",30 ));

        HBox heightHBox = new HBox(10);
        heightHBox.getChildren().addAll(heightLabel,height);
        heightHBox.setAlignment(Pos.CENTER);

        HBox widthHBox = new HBox(10);
        widthHBox.getChildren().addAll(widthLabel,width);
        widthHBox.setAlignment(Pos.CENTER);

        HBox startEnergyHBox = new HBox(10);
        startEnergyHBox.getChildren().addAll(startEnergyLabel,startEnergy);
        startEnergyHBox.setAlignment(Pos.CENTER);

        HBox moveEnergyHBox = new HBox(10);
        moveEnergyHBox.getChildren().addAll(moveEnergyLabel,moveEnergy);
        moveEnergyHBox.setAlignment(Pos.CENTER);

        HBox plantEnergyHBox = new HBox(10);
        plantEnergyHBox.getChildren().addAll(plantEnergyLabel,plantEnergy);
        plantEnergyHBox.setAlignment(Pos.CENTER);

        HBox jungleRatioHBox = new HBox(10);
        jungleRatioHBox.getChildren().addAll(jungleRatioLabel,jungleRatio);
        jungleRatioHBox.setAlignment(Pos.CENTER);

        HBox initialNumberOfAnimalsHBox = new HBox(10);
        initialNumberOfAnimalsHBox.getChildren().addAll(initialNumberOfAnimalsLabel,initialNumberOfAnimals);
        initialNumberOfAnimalsHBox.setAlignment(Pos.CENTER);


        Scene scene = new Scene(menu, 500, 500);
        Stage myStage = new Stage();
        myStage.setTitle("Evolution Simulator");
        myStage.setScene(scene);
        myStage.show();

        Button submitButton = new Button("Submit");
        submitButton.setOnAction((e) -> {
            Simulation game = new Simulation(Integer.parseInt(width.getText()), Integer.parseInt(height.getText()),
                    Integer.parseInt(startEnergy.getText()), Integer.parseInt(moveEnergy.getText()),
                    Integer.parseInt(plantEnergy.getText()), Double.parseDouble(jungleRatio.getText()),
                    Integer.parseInt(initialNumberOfAnimals.getText()));

            try {
                game.start(primaryStage);
                myStage.hide();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        menu.getChildren().addAll(mainLabel, heightHBox, widthHBox, startEnergyHBox, moveEnergyHBox, plantEnergyHBox,
                jungleRatioHBox, initialNumberOfAnimalsHBox, submitButton);
        menu.setAlignment(Pos.CENTER);
    }

}
