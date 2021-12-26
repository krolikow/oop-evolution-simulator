
package project;
import java.util.Map;
public interface IWorldMap {

    boolean canMoveTo(Vector2d position);

    boolean placeAnimal(Animal animal);

    boolean isOccupied(Vector2d position);

    Object objectAt(Vector2d position);

}
