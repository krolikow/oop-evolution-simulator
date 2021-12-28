
package project;

import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import project.statictics.StatPanel;

import static java.lang.System.out;

import java.util.*;

public class SimulationEngine implements IEngine, Runnable {
    private final AbstractWorldMap map;
    private final List<Animal> animals = new ArrayList<>();
    private final List<IPositionChangeObserver> observers = new ArrayList<>();
    int moveDelay, initialNumberOfAnimals;
    boolean isON;
    int days = 0;
    private StatPanel statPanel;
    private GridPane grid;


    public SimulationEngine(AbstractWorldMap map, int moveDelay, boolean isON, StatPanel statPanel, GridPane grid) {
        this.map = map;
        this.moveDelay = moveDelay;
        this.isON = isON;
        this.statPanel = statPanel;
        this.grid = grid;
    }


    public void switchON() {
        this.isON = !this.isON;
    }

    public void setIsON(){
        this.isON = true;
    }

    public int getAllAnimalsNumber(AbstractWorldMap map){
        int result = 0;
        for (Vector2d position: map.animals.keySet()){
            if(map.animals.get(position)!=null){
                result+=map.animals.get(position).size();
            }
        }
        StatPanel.animalNumber.put(days,result);
        return result;
    }

    public int getAllPlantsNumber(AbstractWorldMap map){
        StatPanel.plantNumber.put(days,map.grass.size());
        return map.grass.size();
    }

    public double getAverageEnergyLevel(AbstractWorldMap map){
        int energySum = 0;
        for (HashMap.Entry<Vector2d, LinkedList<Animal>> entry: map.animals.entrySet()){
            if(entry.getValue()!=null){
                for(Animal animal: entry.getValue()){
                    energySum+=animal.getEnergy();
                }

            }
        }
        int allAnimals = this.getAllAnimalsNumber(map);
        StatPanel.averageEnergyLevel.put(days, energySum/allAnimals);
        return (double)energySum/allAnimals;
    }

    public double getAverageLifeSpan(AbstractWorldMap map){
        int lifeSpanSum = map.lifeSpanSum;
        int deadAnimalsAmount = map.deadAnimalsAmount;

        if(deadAnimalsAmount>0){
            StatPanel.averageLifeSpan.put(days, lifeSpanSum/deadAnimalsAmount);
        }
        else{
            StatPanel.averageLifeSpan.put(days, 0);
        }
        return (double)lifeSpanSum/deadAnimalsAmount;
    }

    public double getAverageChildrenAmount(AbstractWorldMap map){
        int childrenAmount = 0;
        int livingAnimals = this.getAllAnimalsNumber(map);
        for (HashMap.Entry<Vector2d, LinkedList<Animal>> entry: map.animals.entrySet()){
            if(entry.getValue()!=null){
                for(Animal animal: entry.getValue()){
                    childrenAmount+=animal.getChildrenAmount();
                }
            }
        }
        StatPanel.averageChildrenAmount.put(days, childrenAmount /livingAnimals);
        return childrenAmount/livingAnimals;
    }

    public ArrayList<Integer> getGenotypeDominant(AbstractWorldMap map){
        // from https://stackoverflow.com/questions/5911174/finding-key-associated-with-max-value-in-a-java-map
        return map.allGenotypes.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
    }


    public void run() {
        for (IPositionChangeObserver animalMoveObserver : this.observers)
            animalMoveObserver.positionChanged(this.grid,this.map);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            System.out.println("Error has occured: " + ex);
        }

        while (true) {
            if (this.isON) {
                map.removeDeadBodies();
                map.moving();
                for (IPositionChangeObserver animalMoveObserver : this.observers)
                    animalMoveObserver.positionChanged(this.grid,this.map);

                map.eating();
                map.reproduction();
                map.addNewPlants();
                map.newDay();
                try {
                    Thread.sleep(moveDelay);
                } catch (InterruptedException e) {
                    System.out.println("Simulation interrupted");
                }
                days++;
                System.out.println(days);

                System.out.println(this.getAllAnimalsNumber(map));
                System.out.println(this.getAllPlantsNumber(map));
                System.out.println(this.getAverageChildrenAmount(map));
                System.out.println(this.getAverageEnergyLevel(map));
                System.out.println(this.getAverageLifeSpan(map));

//                if (days%10==0)
                Platform.runLater(()->{
                    statPanel.prepareData();
                });

//                System.out.println(this.getGenotypeDominant(map));

//                System.out.println(this.map);
                System.out.println();
            }
        }
    }

    public void addObserver(IPositionChangeObserver observer) {
        this.observers.add(observer);
    }
}






