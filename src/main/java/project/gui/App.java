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

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Menu menu = new Menu();
        menu.start(primaryStage);
    }
}