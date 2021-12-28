package project;

import javafx.scene.layout.GridPane;

public class UnBoundedMap extends AbstractWorldMap{

    public UnBoundedMap(int height, int width,int moveEnergy, int plantEnergy, int startEnergy, double jungleRatio,int initialNumberOfAnimals) {
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

    public Vector2d teleport(Vector2d position){ // if an animal wants to get over the map boundaries
        if ((position.x>=this.width)&&(this.isYInBounds(position))) return new Vector2d(0,position.y);
        else if ((position.x<0)&&(this.isYInBounds(position))) return new Vector2d(this.width-1,position.y);
        else if ((position.y<0)&&(this.isXInBounds(position))) return new Vector2d(position.x,this.height -1);
        else if ((position.y>=this.height)&&(this.isXInBounds(position))) return new Vector2d(position.x,0);
        else if ((position.x>=this.width)&&((position.y>=this.height))) return new Vector2d(0,0);
        else if ((position.x<0)&&((position.y<0))) return new Vector2d(this.width-1,this.height-1);
        else if ((position.x<0)&&(position.y>=this.height)) return new Vector2d(this.width-1,0);
        else if ((position.x>=this.width)&&((position.y<0))) return new Vector2d(0,this.height-1);
        else return position;
    }

    @Override
    public Vector2d canMoveTo(Vector2d oldPosition, Vector2d newPosition) {
        return teleport(oldPosition);
    }

    @Override
    public void positionChanged() {
    }


    @Override
    public void positionChanged(GridPane grid, AbstractWorldMap map) {

    }
}
