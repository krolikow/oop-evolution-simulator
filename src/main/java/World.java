import java.lang.*;

public class World {

    public static void main(String[] args) {
        try {
            System.out.println("Hello World!");
            AbstractWorldMap map = new BoundedMap(8,10,2,5,20,0.5,10);
//            Animal animal = new Animal(map,30,new Vector2d(2,2));
//            System.out.println(map.canMoveTo(new Vector2d(9,10)));
//            System.out.println(animal.getDirection());
//            animal.move();
//            System.out.println(animal.getDirection()+ " " + animal.getPosition());

//            Animal mamaAnimal = new Animal(map,10,new Vector2d(2,2));
//            Animal tataAnimal = new Animal(map,10,new Vector2d(3,3));
//            Animal randomAnimal = new Animal(map,10,new Vector2d(4,4));
//            Grass grassTuft = new Grass(new Vector2d(2,2));
//            map.grass.put(grassTuft.getPosition(), grassTuft);
//            map.addAnimalToAnimals(mamaAnimal, mamaAnimal.getPosition());
//            map.addAnimalToAnimals(tataAnimal, tataAnimal.getPosition());
//            map.addAnimalToAnimals(randomAnimal, randomAnimal.getPosition());
//            System.out.println(mamaAnimal.getDirection() +" "+ tataAnimal.getDirection()+" "+randomAnimal.getDirection());
//            System.out.println(mamaAnimal.getEnergy() +" "+ tataAnimal.getEnergy()+" "+randomAnimal.getEnergy());

//            map.reproduction();
//            System.out.println(map.animals);
            System.out.println(map.widthJungle);
            System.out.println(map.heightJungle);
            System.out.println(map.lowerLeftJungle);
            System.out.println(map.upperRightJungle);
//            for (int i = 0; i < 60; i++) {
//                map.removeDeadBodies();
//                System.out.println(map);
//                map.moving();
//                map.eating();
//                map.reproduction();
//                map.addNewPlants();
//            }

            System.out.println(map);

//            System.out.println(mamaAnimal.getGenotype());
//            System.out.println(tataAnimal.getGenotype());
//            System.out.println(map.animalWooHoo(new Vector2d(2,2)).getGenotype());

//            System.out.println(mamaAnimal.getEnergy());
//            System.out.println(tataAnimal.getEnergy());
//            System.out.println(randomAnimal.getEnergy());

            IEngine engine = new SimulationEngine(map,300,20);
            engine.run();
        } catch (IllegalArgumentException ex) {
            System.out.println("Error has occured: " + ex);
        }
    }
}