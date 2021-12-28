package project;
import javafx.scene.layout.GridPane;

import java.util.Random;
import static java.lang.Math.sqrt;

public class BoundedMap extends AbstractWorldMap implements IWorldMap{

    public BoundedMap(int height, int width,int moveEnergy, int plantEnergy, int startEnergy, double jungleRatio,int initialNumberOfAnimals) {
        this.height = height;
        this.width = width;
        this.widthJungle = (int) Math.floor(width*jungleRatio);
        this.heightJungle = (int) Math.floor(height*jungleRatio);
        this.upperRight = new Vector2d(width-1,height-1);
        this.lowerLeft = new Vector2d(0,0);
        this.lowerLeftJungle = new Vector2d((int) Math.floor((float)(width-widthJungle)/2),(int) Math.floor((float)(height-heightJungle)/2));
        this.upperRightJungle = new Vector2d(lowerLeftJungle.x+widthJungle,lowerLeftJungle.y+heightJungle);
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.startEnergy = startEnergy;
        this.widthJungle = (int) Math.floor(width*jungleRatio);
        this.heightJungle = (int) Math.floor(height*jungleRatio);
        this.jungleRatio = jungleRatio;
        this.initialNumberOfAnimals = initialNumberOfAnimals;

        initializeMap();
    }

    @Override
    public Vector2d canMoveTo(Vector2d oldPosition, Vector2d newPosition) {
        if ((0 <= newPosition.x) && (newPosition.x <= width - 1) && (0 <= newPosition.y) && (newPosition.y <= height - 1)) return newPosition;
        return oldPosition;
    }


    @Override
    public void positionChanged() {
    }

    @Override
    public void positionChanged(GridPane grid, AbstractWorldMap map) {

    }
}