package project.gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Menu menu = new Menu();
        menu.start(primaryStage);
    }
}