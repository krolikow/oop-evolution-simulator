package project;

import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import project.statictics.StatisticsConverter;
import project.statictics.StatisticsPanel;

import java.util.*;

public class SimulationEngine implements IEngine, Runnable {
    private final AbstractWorldMap map;
    private final List<IPositionChangeObserver> observers = new ArrayList<>();
    private int moveDelay, days = 0;
    private boolean isON;
    private final StatisticsPanel statisticsPanel;
    private final GridPane grid;
    private final VBox statistics;
    private final StatisticsConverter statisticsConverter;
    private final List<Double> averageData = Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0);


    public SimulationEngine(AbstractWorldMap map, int moveDelay, boolean isON, StatisticsPanel statPanel, GridPane grid, VBox statistics) {
        this.map = map;
        this.moveDelay = moveDelay;
        this.isON = isON;
        this.statisticsPanel = statPanel;
        this.grid = grid;
        this.statistics = statistics;
        this.statisticsConverter = new StatisticsConverter(this.map, this);
    }

    public void switchON() {
        this.isON = !this.isON;
    }

    // GETTERS & SETTERS

    public StatisticsConverter getStatisticsConverter() {
        return this.statisticsConverter;
    }

    public void setIsON() {
        this.isON = true;
    }

    public int getAllAnimalsNumber(AbstractWorldMap map) {
        int result = 0;
        for (Vector2d position : map.animals.keySet()) {
            if (map.animals.get(position) != null) {
                result += map.animals.get(position).size();
            }
        }
        StatisticsPanel.animalNumber.put(days, result);
        return result;
    }

    public int getAllPlantsNumber(AbstractWorldMap map) {
        StatisticsPanel.plantNumber.put(days, map.grass.size());
        return map.grass.size();
    }

    public double getAverageEnergyLevel(AbstractWorldMap map) {
        int energySum = 0;
        for (HashMap.Entry<Vector2d, LinkedList<Animal>> entry : map.animals.entrySet()) {
            if (entry.getValue() != null) {
                for (Animal animal : entry.getValue()) {
                    energySum += animal.getEnergy();
                }

            }
        }
        int allAnimals = this.getAllAnimalsNumber(map);
        if(allAnimals>0){
            StatisticsPanel.averageEnergyLevel.put(days, energySum / allAnimals);
            return (double) energySum / allAnimals;
        }
        else{
            StatisticsPanel.averageEnergyLevel.put(days, 0);
            return 0;
        }

    }

    public double getAverageLifeSpan(AbstractWorldMap map) {
        int lifeSpanSum = map.lifeSpanSum;
        int deadAnimalsAmount = map.deadAnimalsAmount;
        if (deadAnimalsAmount > 0) {
            StatisticsPanel.averageLifeSpan.put(days, lifeSpanSum / deadAnimalsAmount);
            return lifeSpanSum / deadAnimalsAmount;
        } else {
            StatisticsPanel.averageLifeSpan.put(days, 0);
            return 0;
        }
    }

    public double getAverageChildrenAmount(AbstractWorldMap map) {
        int childrenAmount = 0;
        int livingAnimals = this.getAllAnimalsNumber(map);
        for (HashMap.Entry<Vector2d, LinkedList<Animal>> entry : map.animals.entrySet()) {
            if (entry.getValue() != null) {
                for (Animal animal : entry.getValue()) {
                    childrenAmount += animal.getChildrenAmount();
                }
            }
        }
        if (livingAnimals > 0) {
            StatisticsPanel.averageChildrenAmount.put(days, childrenAmount / livingAnimals);
            return childrenAmount / livingAnimals;
        }
        else{
            StatisticsPanel.averageChildrenAmount.put(days, 0);
            return 0;
        }
    }

    public int getDays() {
        return this.days;
    }

    public String getGenotypeDominant(AbstractWorldMap map) {
        ArrayList<Integer> mostCommonGenotype = new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0));
        int frequency = -1;
        for (Map.Entry<ArrayList<Integer>, Integer> entity : map.allGenotypes.entrySet()) {
            if (entity.getValue() > frequency) {
                mostCommonGenotype = entity.getKey();
                frequency = entity.getValue();
            }
        }
        return mostCommonGenotype.toString();
    }

    // STATISTICS WRITING

    public String getNewLine() {
        return this.getDays() + ", " + this.getAllAnimalsNumber(this.map) + ", " + this.getAllPlantsNumber(this.map) + ", "
                + this.getAverageLifeSpan(this.map) + ", " + this.getAverageEnergyLevel(this.map) + ", " + this.getAverageChildrenAmount(this.map);
    }

    public String getNewAverageDataLine() {
        return "Average data: animals: " + averageData.get(0) + ", plants: " + averageData.get(1) + ", life span: " + averageData.get(2)
                + ", energy level: " + averageData.get(3) + ", children amount: " + averageData.get(4);
    }

    public void updateAverageData() {
        averageData.set(0, (double) this.getAllAnimalsNumber(this.map) / days);
        averageData.set(1, (double) this.getAllPlantsNumber(this.map) / days);
        averageData.set(2, this.getAverageLifeSpan(this.map) / days);
        averageData.set(3, this.getAverageEnergyLevel(this.map) / days);
        averageData.set(4, this.getAverageChildrenAmount(this.map) / days);

    }

    public void run() {
        for (IPositionChangeObserver animalMoveObserver : this.observers)
            animalMoveObserver.positionChanged(this.grid, this.map, this, this.statistics);
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
                    animalMoveObserver.positionChanged(this.grid, this.map, this, this.statistics);

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
                this.updateAverageData();
                statisticsConverter.addToStatistics(this.getNewLine());

                Platform.runLater(() -> {
                    statisticsPanel.prepareData();
                });
            }
        }
    }

    public void addObserver(IPositionChangeObserver observer) {
        this.observers.add(observer);
    }
}

