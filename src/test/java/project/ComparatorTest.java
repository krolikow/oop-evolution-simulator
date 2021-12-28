package project;

import org.junit.jupiter.api.Test;

import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComparatorTest {

    @Test
    void comparatorXTest() {
        AbstractWorldMap map = new BoundedMap(10,10,2,5,10,0.5,10);

        Animal firstAnimal = new Animal(map, 4,new Vector2d(1, 2));
        Animal secondAnimal = new Animal(map,9, new Vector2d(3, 4));
        Animal thirdAnimal = new Animal(map,2, new Vector2d(2, 6));

        map.place(firstAnimal);
        map.place(secondAnimal);
        map.place(thirdAnimal);

        ArrayList<Animal> animals = new ArrayList<>();

        animals.add(firstAnimal);
        animals.add(secondAnimal);
        animals.add(thirdAnimal);

        EnergyComparator energyComparator = new EnergyComparator();
        ArrayList<Animal> expected = new ArrayList<>(Arrays.asList(secondAnimal,firstAnimal,thirdAnimal));
        animals.sort(energyComparator);
        assertEquals(expected.get(0), animals.get(0));
        assertEquals(expected.get(1), animals.get(1));
        assertEquals(expected.get(2), animals.get(2));}
}