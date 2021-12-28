package project.statictics;

import javafx.application.Application;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StatisticsPanel extends Application {
    public static ConcurrentHashMap<Integer, Integer> animalNumber = new ConcurrentHashMap<>(), plantNumber = new ConcurrentHashMap<>(),
            averageEnergyLevel = new ConcurrentHashMap<>(), averageLifeSpan = new ConcurrentHashMap<>(), averageChildrenAmount = new ConcurrentHashMap<>();
    private LineChart<Integer, Integer> chart;
    XYChart.Series<Integer, Integer> averageChildrenAmountChart = new XYChart.Series<>();
    XYChart.Series<Integer, Integer> animalNumberChart = new XYChart.Series<>();
    XYChart.Series<Integer, Integer> plantNumberChart = new XYChart.Series<>();
    XYChart.Series<Integer, Integer> averageEnergyLevelChart = new XYChart.Series<>();
    XYChart.Series<Integer, Integer> averageLifeSpanChart = new XYChart.Series<>();


    @Override
    public void start(Stage primaryStage) throws Exception {
        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();
        chart = new LineChart(x, y);
        prepareData();

        chart.getData().add(animalNumberChart);
        chart.getData().add(plantNumberChart);
        chart.getData().add(averageChildrenAmountChart);
        chart.getData().add(averageEnergyLevelChart);
        chart.getData().add(averageLifeSpanChart);
    }


    public void prepareData() {

        animalNumberChart.setName("Animal number");
        for (Map.Entry<Integer, Integer> entity : StatisticsPanel.animalNumber.entrySet()) {
            animalNumberChart.getData().add(new XYChart.Data<>(entity.getKey(), entity.getValue()));
            StatisticsPanel.animalNumber.remove(entity.getKey());
        }


        plantNumberChart.setName("Plant number");
        for (Map.Entry<Integer, Integer> entity : StatisticsPanel.plantNumber.entrySet()) {
            plantNumberChart.getData().add(new XYChart.Data<>(entity.getKey(), entity.getValue()));
            StatisticsPanel.plantNumber.remove(entity.getKey());
        }


        averageEnergyLevelChart.setName("Average energy level");
        for (Map.Entry<Integer, Integer> entity : StatisticsPanel.averageEnergyLevel.entrySet()) {
            averageEnergyLevelChart.getData().add(new XYChart.Data<>(entity.getKey(), entity.getValue()));
            StatisticsPanel.averageEnergyLevel.remove(entity.getKey());
        }


        averageLifeSpanChart.setName("Average life span");
        for (Map.Entry<Integer, Integer> entity : StatisticsPanel.averageLifeSpan.entrySet()) {
            averageLifeSpanChart.getData().add(new XYChart.Data<>(entity.getKey(), entity.getValue()));
            StatisticsPanel.averageLifeSpan.remove(entity.getKey());
        }


        averageChildrenAmountChart.setName("Average children amount");
        for (Map.Entry<Integer, Integer> entity : StatisticsPanel.averageChildrenAmount.entrySet()) {
            averageChildrenAmountChart.getData().add(new XYChart.Data<>(entity.getKey(), entity.getValue()));
            StatisticsPanel.averageChildrenAmount.remove(entity.getKey());
        }
    }

    public LineChart<Integer, Integer> getChart() {return this.chart;
    }
}
