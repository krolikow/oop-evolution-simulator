package project;
import java.util.Random;
import static java.lang.Math.sqrt;

public class BoundedMap extends AbstractWorldMap implements IWorldMap{

    public BoundedMap(int height, int width,int moveEnergy, int plantEnergy, int startEenergy, double jungleRatio,int initialNumberOfAnimals) {
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
        this.startEnergy = startEenergy;
        this.widthJungle = (int) Math.floor(width*jungleRatio);
        this.heightJungle = (int) Math.floor(height*jungleRatio);
        this.jungleRatio = jungleRatio;
        this.initialNumberOfAnimals = initialNumberOfAnimals;

        initializeMap();
    }

//    @Override
//    public boolean placeAnimal(Animal animal) {
//        super.placeAnimal(animal);
//        return true;
//    }
//
//    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition) {
//        super.positionChanged(animal,oldPosition,newPosition);
//    }

    @Override
    public void positionChanged() {
    }
//
//    @Override
//    public void positionChanged() {
//
//    }
//
//    @Override
//    public String toString() {
//        return super.toString();
//    }
}