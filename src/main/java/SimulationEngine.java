import static java.lang.System.out;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimulationEngine implements IEngine,Runnable {
    private final AbstractWorldMap map;
    private final List<Animal> animals = new ArrayList<>();
    private final List<IPositionChangeObserver> observers = new ArrayList<>();
    int initialNumberOfAnimals, startEnergy, moveDelay;


    public SimulationEngine( AbstractWorldMap map, int moveDelay, int initialNumberOfAnimals,int startEnergy ){
        this.map = map;
        this.moveDelay = moveDelay;
        this.initialNumberOfAnimals = initialNumberOfAnimals;
        this.startEnergy = startEnergy;
        initializeMap();
    }

    public List<Vector2d> initializePositions(){ // non effective prob / git
        List<Vector2d> initialPositions = new ArrayList<>();
        int i =0;
        while (i<this.initialNumberOfAnimals) {
            int x = (int)Math.floor(Math.random()*(map.width+1));
            int y = (int)Math.floor(Math.random()*(map.height+1));
            Vector2d newPosition = new Vector2d(x,y);
            if (!(map.isOccupied(newPosition))){
                initialPositions.add(newPosition);
                i++;
            }
        }
        return initialPositions;
    }

    private void initializeMap(){ // dodajemy do hashmapy  / git
        List<Vector2d> initialPositions = initializePositions();
        for (Vector2d position : initialPositions){
            Animal animal = new Animal(this.map, this.startEnergy, position);
            map.placeAnimal(animal);
            this.animals.add(animal);
            animal.addObserver(this.map);
        }
    }

    public void run(){
        System.out.println(this.map);
        int animalsAmount = this.animals.size();
        for(int i =0; i<1000*animalsAmount;i++){
            this.animals.get(i % animalsAmount).move();
            System.out.println(animals.get(0).getPosition() + " " + animals.get(1).getPosition());
            System.out.println(this.map);
        }
        System.out.println("Done");
    }

//    public void run(){
//        out.println(this.map);
//        for (IPositionChangeObserver animalMoveObserver : this.observers)
//           animalMoveObserver.positionChanged();
//        try{
//            Thread.sleep(300);
//        }
//        catch (InterruptedException ex){
//            System.out.println("Error has occured: " + ex);
//        }
//        int animalsAmount = this.animals.size();
//        int len = this.animals.size();
//        for (int i = 0; i < len; i++) {
//            this.animals.get(i % animalsAmount).move(this.animals.get(i));
//            for (IPositionChangeObserver animalMoveObserver : this.observers)
//                animalMoveObserver.positionChanged();
//            try {
//                int moveDelay = 300;
//                Thread.sleep(moveDelay);
//                System.out.println(this.map);
//            } catch (InterruptedException e) {
//                System.out.println("Simulation stopped");
//            }
//        }
//    }

    public void addObserver(IPositionChangeObserver observer) {
        this.observers.add(observer);
    }
}






