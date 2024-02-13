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
    private final int moveDelay;
    private int magicTricks;
    private int currentEpoch = 0;
    private boolean isON, isMagic;
    private final StatisticsPanel statisticsPanel;
    private final GridPane grid;
    private final VBox statistics;
    private final StatisticsConverter statisticsConverter;
    private final List<Double> averageData = Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0);

    public SimulationEngine(AbstractWorldMap map, int moveDelay, boolean isON, StatisticsPanel statPanel, GridPane grid, VBox statistics) {
        this.map = map;
        this.moveDelay = moveDelay;
        this.isON = isON;
        this.isMagic = false;
        this.magicTricks = 3;
        this.statisticsPanel = statPanel;
        this.grid = grid;
        this.statistics = statistics;
        this.statisticsConverter = new StatisticsConverter(this.map, this);
    }

    public void switchON() {
        this.isON = !this.isON;
    }

    public void doMagicTrick() {
        if ((this.isMagic) && (this.getMagicTricksNumber() > 0) && (this.getAllAnimalsNumber() == 5)) {
            for (int i = 0; i < 5; i++) {
                this.map.place(this.map.copyAnimal());
            }
            decrementMagicTricks();
        }
    }

    // GETTERS & SETTERS

    public StatisticsConverter getStatisticsConverter() {
        return this.statisticsConverter;
    }

    public void setIsMagic() {
        this.isMagic = true;
    }

    public void decrementMagicTricks() {
        this.magicTricks -= 1;
    }

    public void setIsON() {
        this.isON = true;
    }

    public int getMagicTricksNumber() {
        return this.magicTricks;
    }

    public int getAllAnimalsNumber() {
        int result = 0;
        for (Vector2d position : map.animals.keySet()) {
            if (map.animals.get(position) != null) {
                result += map.animals.get(position).size();
            }
        }
        return result;
    }

    public int getAllPlantsNumber() {
        return map.grass.size();
    }

    public double getAverageEnergyLevel() {
        int energySum = 0;
        for (HashMap.Entry<Vector2d, LinkedList<Animal>> entry : map.animals.entrySet()) {
            if (entry.getValue() != null) {
                for (Animal animal : entry.getValue()) {
                    energySum += animal.getEnergy();
                }

            }
        }
        int allAnimals = this.getAllAnimalsNumber();
        if (allAnimals > 0) {
            return (double) energySum / allAnimals;
        } else {
            return 0;
        }

    }

    public double getAverageLifeSpan() {
        int lifeSpanSum = map.lifeSpanSum;
        int deadAnimalsAmount = map.deadAnimalsAmount;
        if (deadAnimalsAmount > 0) {
            return (double) lifeSpanSum / deadAnimalsAmount;
        } else {
            return 0;
        }
    }

    public double getAverageChildrenAmount() {
        int childrenAmount = 0;
        int livingAnimals = this.getAllAnimalsNumber();
        for (HashMap.Entry<Vector2d, LinkedList<Animal>> entry : map.animals.entrySet()) {
            if (entry.getValue() != null) {
                for (Animal animal : entry.getValue()) {
                    childrenAmount += animal.getChildrenAmount();
                }
            }
        }
        if (livingAnimals > 0) {
            return (double) childrenAmount / livingAnimals;
        } else {
            return 0;
        }
    }

    public int getCurrentEpoch() {
        return this.currentEpoch;
    }

    public String getGenotypeDominant(AbstractWorldMap map) {
        ArrayList<Integer> mostCommonGenotype = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
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
        return this.getCurrentEpoch() + ", " + this.getAllAnimalsNumber() + ", " + this.getAllPlantsNumber() + ", "
                + this.getAverageLifeSpan() + ", " + this.getAverageEnergyLevel() + ", " + this.getAverageChildrenAmount();
    }

    public String getNewAverageDataLine() {
        return "Average data: animals: " + averageData.get(0) + ", plants: " + averageData.get(1) + ", life span: " + averageData.get(2)
                + ", energy level: " + averageData.get(3) + ", children amount: " + averageData.get(4);
    }

    public void updateAverageData() {
        averageData.set(0, currentEpoch == 0 ? (double) this.getAllAnimalsNumber() : (double) this.getAllAnimalsNumber() / currentEpoch);
        averageData.set(1, currentEpoch == 0 ? (double) this.getAllPlantsNumber() : (double) this.getAllPlantsNumber() / currentEpoch);
        averageData.set(2, currentEpoch == 0 ? this.getAverageLifeSpan() : this.getAverageLifeSpan() / currentEpoch);
        averageData.set(3, currentEpoch == 0 ? this.getAverageEnergyLevel() : this.getAverageEnergyLevel() / currentEpoch);
        averageData.set(4, currentEpoch == 0 ? this.getAverageChildrenAmount() : this.getAverageChildrenAmount() / currentEpoch);

    }

    public void run() {
        for (IPositionChangeObserver animalMoveObserver : this.observers)
            animalMoveObserver.positionChanged(this.grid, this.map, this, this.statistics);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            System.out.println("Error has occured: " + ex);
        }

        while (this.isON) {
            map.removeDeadBodies();
            this.doMagicTrick();
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
            currentEpoch++;
            this.updateAverageData();
            statisticsConverter.addToStatistics(this.getNewLine());
            System.out.println(this.currentEpoch + " " + this.getNewLine());
            Platform.runLater(() -> {
                statisticsPanel.prepareData(currentEpoch, getAllAnimalsNumber(), (int)getAverageChildrenAmount(),
                        getAllPlantsNumber(), getAverageEnergyLevel(), getAverageLifeSpan());
            });
        }
    }

    public void addObserver(IPositionChangeObserver observer) {
        this.observers.add(observer);
    }
}

