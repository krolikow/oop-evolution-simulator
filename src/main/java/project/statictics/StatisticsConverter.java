package project.statictics;
import project.AbstractWorldMap;
import project.SimulationEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class StatisticsConverter {
    private final SimulationEngine engine;
    private final AbstractWorldMap map;
    List<String> statistics = new ArrayList<>();

    public StatisticsConverter(AbstractWorldMap map, SimulationEngine engine){
        this.map = map;
        this.engine = engine;
    }

    public void addToStatistics(String newLine){
        statistics.add(newLine);
    }

    public void convertToCSV(String fileName){
        File file = new File(fileName + ".csv");
        try (PrintWriter pw = new PrintWriter(file)) {
            pw.println("epoch, animalNumber ,plantNumber, averageLifeSpan, averageEnergyLevel, averageChildrenAmount");
            statistics.forEach(pw::println);
        } catch (FileNotFoundException e) {
            System.out.println("File doesn't exists");
            e.printStackTrace();
        }
    }
}
