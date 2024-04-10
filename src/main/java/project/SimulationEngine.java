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
        if ((this.isMagic) && (this.getMagicTricksNumber() > 0) && (this.getAnimalsNumber() == 5)) {
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

    public int getAnimalsNumber() {
        int result = 0;
        for (Vector2d position : map.animals.keySet()) {
            LinkedList<Animal> animalsAtField = map.animals.get(position);
            if (animalsAtField != null) {
                result += animalsAtField.size();
            }
        }
        return result;
    }

    public int getPlantsNumber() {
        return map.grass.size();
    }

    public double getAverageEnergyLevel(int animalsNumber) {
        if (animalsNumber == 0) return 0;
        int energySum = 0;
        List<Animal> animals = map.animalsList;
        for (var animal : animals) {
            energySum += animal.getEnergy();
        }
        return (double) energySum / animalsNumber;
    }


    public double getAverageLifeSpan() {
        int lifeSpanSum = map.lifeSpanSum;
        int deadAnimalsAmount = map.deadAnimalsAmount;
        if (deadAnimalsAmount == 0) return 0;
        return (double) lifeSpanSum / deadAnimalsAmount;
    }

    public double getAverageChildrenNumber(int animalsNumber) {
        if (animalsNumber == 0) return 0;
        int childrenAmount = 0;
        List<Animal> animals = map.animalsList;
        for (var animal : animals) {
            childrenAmount += animal.getChildrenAmount();
        }
        return (double) childrenAmount / animalsNumber;
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
        return this.getCurrentEpoch() + ", " + this.getAnimalsNumber() + ", " + this.getPlantsNumber() + ", "
                + this.getAverageLifeSpan() + ", " + this.getAverageEnergyLevel(getAnimalsNumber()) + ", " + this.getAverageChildrenNumber(getAnimalsNumber());
    }

    public String getNewAverageDataLine() {
        return "Average data: animals: " + averageData.get(0) + ", plants: " + averageData.get(1) + ", life span: " + averageData.get(2)
                + ", energy level: " + averageData.get(3) + ", children amount: " + averageData.get(4);
    }

    public void updateAverageData() {
        int animalsNumber = this.getAnimalsNumber();
        double plantsNumber = this.getPlantsNumber();
        double averageLifeSpan = this.getAverageLifeSpan();
        double averageEnergyLevel = this.getAverageEnergyLevel(animalsNumber);
        double averageChildrenNumber = this.getAverageChildrenNumber(animalsNumber);
        averageData.set(0, currentEpoch == 0 ? (double) animalsNumber : animalsNumber / currentEpoch);
        averageData.set(1, currentEpoch == 0 ? plantsNumber : plantsNumber / currentEpoch);
        averageData.set(2, currentEpoch == 0 ? averageLifeSpan : averageLifeSpan / currentEpoch);
        averageData.set(3, currentEpoch == 0 ? averageEnergyLevel : averageEnergyLevel / currentEpoch);
        averageData.set(4, currentEpoch == 0 ? averageChildrenNumber : averageChildrenNumber / currentEpoch);

    }

    public void run() {
        for (IPositionChangeObserver animalMoveObserver : this.observers)
            animalMoveObserver.positionChanged(this.grid, this.map, this, this.statistics);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            System.out.println("Error has occurred: " + ex);
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
            Platform.runLater(() -> {
                int animalsNumber = getAnimalsNumber();
                statisticsPanel.prepareData(currentEpoch, animalsNumber, (int) getAverageChildrenNumber(animalsNumber),
                        getPlantsNumber(), getAverageEnergyLevel(getAnimalsNumber()), getAverageLifeSpan());
            });
        }
    }

    public void addObserver(IPositionChangeObserver observer) {
        this.observers.add(observer);
    }
}

