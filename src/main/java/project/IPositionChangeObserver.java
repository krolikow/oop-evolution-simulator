package project;

import javafx.scene.layout.GridPane;

public interface IPositionChangeObserver {
    void positionChanged(Animal animal , Vector2d oldPosition, Vector2d newPosition);
    void positionChanged();
    void positionChanged(GridPane grid, AbstractWorldMap map);
}

