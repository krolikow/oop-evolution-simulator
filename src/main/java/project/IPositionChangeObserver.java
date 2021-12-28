package project;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public interface IPositionChangeObserver {
    void positionChanged(Animal animal , Vector2d oldPosition, Vector2d newPosition);
    void positionChanged(GridPane grid, AbstractWorldMap map, SimulationEngine engine, VBox statistics);
}

