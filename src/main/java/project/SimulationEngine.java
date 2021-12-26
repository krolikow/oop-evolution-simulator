
package project;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimulationEngine implements IEngine,Runnable {
    private final AbstractWorldMap map;
    private final List<Animal> animals = new ArrayList<>();
    private final List<IPositionChangeObserver> observers = new ArrayList<>();
    int moveDelay,initialNumberOfAnimals;


    public SimulationEngine(AbstractWorldMap map, int moveDelay){
        this.map = map;
        this.moveDelay = moveDelay;
    }


//    public void run(){
//        System.out.println(this.map);
//        int animalsAmount = this.animals.size();
//        System.out.println(this.map);
//        for(int i =0; i<1000*animalsAmount;i++){
//            this.animals.get(i % animalsAmount).move();
//            if(i%animalsAmount == 0){
//                System.out.println(animals.get(0).getPosition() + " " + animals.get(1).getPosition());
//                System.out.println(this.map);
//            }
//
//        }
//        System.out.println("Done");
//    }

    public void run(){
        for (IPositionChangeObserver animalMoveObserver : this.observers)
           animalMoveObserver.positionChanged();
        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException ex){
            System.out.println("Error has occured: " + ex);
        }
//        int animalsAmount = this.animals.size();
//        int days=0;
//        while (true){
//            map.removeDeadBodies();
//            int len = map.getNumberOfAnimals();
//            for (int i = 0; i < len; i++) {
//                this.animals.get(i).move();
//                for (IPositionChangeObserver animalMoveObserver : this.observers)
//                    animalMoveObserver.positionChanged();
//                try {
//                    int moveDelay = 300;
//                    Thread.sleep(moveDelay);
//                } catch (InterruptedException e) {
//                    System.out.println("Simulation stopped");
//                }
//            }
//        int days=0;
//        while (true){
//            map.removeDeadBodies();
//            int len = map.getNumberOfAnimals();
//            for (Vector2d position: map.animals.keySet()) {
//                for(Animal animal: map.animals.get(position)){
//                    animal.move();
//                    for (IPositionChangeObserver animalMoveObserver : this.observers)
//                        animalMoveObserver.positionChanged();
//                    try {
//                        int moveDelay = 300;
//                        Thread.sleep(moveDelay);
//                    } catch (InterruptedException e) {
//                        System.out.println("Simulation stopped");
//                    }
//                }
//
//            }
        int days=0;
        while (true){
            map.removeDeadBodies();
            map.moving();
            for (IPositionChangeObserver animalMoveObserver : this.observers)
                animalMoveObserver.positionChanged();

            map.eating();
            map.reproduction();
            map.addNewPlants();
            try {
                int moveDelay = 300;
                Thread.sleep(moveDelay);
            } catch (InterruptedException e) {
                System.out.println("Simulation stopped");
            }
            days++;
            System.out.println(days);
            System.out.println(map.getNumberOfAnimals());
            System.out.println(this.map);
        }
    }

    public void addObserver(IPositionChangeObserver observer) {
        this.observers.add(observer);
    }
}






