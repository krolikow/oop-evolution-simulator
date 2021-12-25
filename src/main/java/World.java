import java.lang.*;
import java.util.ArrayList;
import java.util.Arrays;

public class World {

    public static void main(String[] args) {
        try {
            System.out.println("Hello World!");
            AbstractWorldMap map = new GrassField(5,10,10);
//            Animal animal = new Animal(map,30,new Vector2d(2,2));
//            System.out.println(map.canMoveTo(new Vector2d(9,10)));
//            System.out.println(animal.getDirection());
//            animal.move();
//            System.out.println(animal.getDirection()+ " " + animal.getPosition());

            Animal mamaAnimal = new Animal(map,0,new Vector2d(2,2));
            Animal tataAnimal = new Animal(map,14,new Vector2d(2,2));
            Animal randomAnimal = new Animal(map,0,new Vector2d(2,2));
            Grass grassTuft = new Grass(new Vector2d(2,2));
            map.grass.put(grassTuft.getPosition(), grassTuft);
            map.addAnimalToAnimals(mamaAnimal, mamaAnimal.getPosition());
            map.addAnimalToAnimals(tataAnimal, tataAnimal.getPosition());
            map.addAnimalToAnimals(randomAnimal, randomAnimal.getPosition());
            System.out.println(mamaAnimal.getDirection() +" "+ tataAnimal.getDirection()+" "+randomAnimal.getDirection());
            System.out.println(mamaAnimal.getEnergy() +" "+ tataAnimal.getEnergy()+" "+randomAnimal.getEnergy());


//            System.out.println(mamaAnimal.getGenotype());
//            System.out.println(tataAnimal.getGenotype());
//            System.out.println(map.animalWooHoo(new Vector2d(2,2)).getGenotype());

//            System.out.println(mamaAnimal.getEnergy());
//            System.out.println(tataAnimal.getEnergy());
//            System.out.println(randomAnimal.getEnergy());

//            IEngine engine = new SimulationEngine(map,3,2,3);
//            engine.run();
        } catch (IllegalArgumentException ex) {
            System.out.println("Error has occured: " + ex);
        }
    }
}