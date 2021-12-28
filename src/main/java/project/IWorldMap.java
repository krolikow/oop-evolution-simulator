
package project;
import java.util.Map;
public interface IWorldMap {

    Vector2d canMoveTo(Vector2d oldPosition,Vector2d newPosition);

    boolean placeAnimal(Animal animal);

    boolean isOccupied(Vector2d position);

    Object objectAt(Vector2d position);

}
