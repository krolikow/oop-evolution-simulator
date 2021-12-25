import java.util.Random;
import static java.lang.Math.sqrt;

public class GrassField extends AbstractWorldMap implements IWorldMap{
    private final int grassTuftsNumber;

    public GrassField(int grassTuftsNumber, int height, int width) {
        this.grassTuftsNumber = grassTuftsNumber;
        this.height = height;
        this.width = width;
        this.upperRight = new Vector2d(width-1,height-1);
        this.lowerLeft = new Vector2d(0,0);
        sowGrass();
    }

    public void sowGrass(){
        int n = this.grassTuftsNumber;
        Random random = new Random();
        while (n!=0){
            int randomX = random.nextInt((int) sqrt(grassTuftsNumber*10));
            int randomY = random.nextInt((int) sqrt(grassTuftsNumber*10));
            Grass newGrass = new Grass(new Vector2d(randomX,randomY));

            if (!(objectAt(newGrass.getPosition()) instanceof Grass)){
                grass.put(newGrass.getPosition(), newGrass);
                n-=1;
            }
        }
    }


    @Override
    public boolean placeAnimal(Animal animal) {
        super.placeAnimal(animal);
        return true;
    }


//    @Override
//    public boolean canMoveTo(Vector2d position) {
////        if (this.isOccupied(position)) {
////            return (objectAt(position) instanceof Grass);
////        }
////        return true;
//        return super.canMoveTo(position);
//    }

//    @Override
//    public void positionChanged() {}


    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition) {
        super.positionChanged(animal,oldPosition,newPosition);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}