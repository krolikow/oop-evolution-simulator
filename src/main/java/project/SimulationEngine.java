
package project;

import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import project.gui.App;
import project.statictics.StatisticsPanel;

import java.util.*;

public class SimulationEngine implements IEngine, Runnable {
    private final AbstractWorldMap map;
    private final List<Animal> animals = new ArrayList<>();
    private final List<IPositionChangeObserver> observers = new ArrayList<>();
    int moveDelay, initialNumberOfAnimals;
    boolean isON;
    int days = 0;
    private StatisticsPanel statisticsPanel;
    private GridPane grid;
    private VBox statistics;


    public SimulationEngine(AbstractWorldMap map, int moveDelay, boolean isON, StatisticsPanel statPanel, GridPane grid, VBox statistics) {
        this.map = map;
        this.moveDelay = moveDelay;
        this.isON = isON;
        this.statisticsPanel = statPanel;
        this.grid = grid;
        this.statistics = statistics;
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
        StatisticsPanel.animalNumber.put(days,result);
        return result;
    }

    public int getAllPlantsNumber(AbstractWorldMap map){
        StatisticsPanel.plantNumber.put(days,map.grass.size());
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
        StatisticsPanel.averageEnergyLevel.put(days, energySum/allAnimals);
        return (double)energySum/allAnimals;
    }

    public double getAverageLifeSpan(AbstractWorldMap map){
        int lifeSpanSum = map.lifeSpanSum;
        int deadAnimalsAmount = map.deadAnimalsAmount;
        if(deadAnimalsAmount>0){
            StatisticsPanel.averageLifeSpan.put(days, lifeSpanSum/deadAnimalsAmount);
            return lifeSpanSum/deadAnimalsAmount;
        }
        else{
            StatisticsPanel.averageLifeSpan.put(days, 0);
            return 0;
        }
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
        StatisticsPanel.averageChildrenAmount.put(days, childrenAmount /livingAnimals);

        return childrenAmount/livingAnimals;
    }

    public int getDays(){
        return this.days;
    }

    public String getGenotypeDominant(AbstractWorldMap map){
        ArrayList<Integer> mostCommonGenotype = (ArrayList<Integer>) map.allGenotypes.keySet().toArray()[0];
        int frequency = -1;
        for (Map.Entry<ArrayList<Integer>,Integer> entity: map.allGenotypes.entrySet()){
            if(entity.getValue() > frequency){
                mostCommonGenotype = entity.getKey();
                frequency = entity.getValue();
            }
        }
        return mostCommonGenotype.toString();
    }


    public void run() {
        for (IPositionChangeObserver animalMoveObserver : this.observers)
            animalMoveObserver.positionChanged(this.grid,this.map,this,this.statistics);
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
                    animalMoveObserver.positionChanged(this.grid,this.map,this,this.statistics);

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
                System.out.println(this.getGenotypeDominant(map));

//                if (days%3==0)
                Platform.runLater(()->{
                    statisticsPanel.prepareData();

                });

                System.out.println();
            }
        }
    }

    public void addObserver(IPositionChangeObserver observer) {
        this.observers.add(observer);
    }
}






