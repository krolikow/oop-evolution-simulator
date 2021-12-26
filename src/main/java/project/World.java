package project;
import javafx.application.Application;
import project.gui.App;

import java.lang.*;

public class World {

    public static void main(String[] args) {
        try {
            Application.launch(App.class, args);
        } catch (IllegalArgumentException ex) {
            System.out.println("Error has occured: " + ex);
        }
    }
}