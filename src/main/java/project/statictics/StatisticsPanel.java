package project.statictics;

import javafx.application.Application;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class StatisticsPanel extends Application {
    private final int animalNumber;
    private final int averageChildrenAmount;
    private final int plantNumber;
    private final int averageEnergyLevel;
    private final int averageLifeSpan;
    private LineChart<Integer, Double> chart;
    XYChart.Series<Integer, Double> averageChildrenAmountChart = new XYChart.Series<>();
    XYChart.Series<Integer, Double> animalNumberChart = new XYChart.Series<>();
    XYChart.Series<Integer, Double> plantNumberChart = new XYChart.Series<>();
    XYChart.Series<Integer, Double> averageEnergyLevelChart = new XYChart.Series<>();
    XYChart.Series<Integer, Double> averageLifeSpanChart = new XYChart.Series<>();


    public StatisticsPanel(int animalNumber, int averageChildrenAmount, int plantNumber, int averageEnergyLevel, int averageLifeSpan) {
        this.animalNumber = animalNumber;
        this.averageChildrenAmount = averageChildrenAmount;
        this.plantNumber = plantNumber;
        this.averageEnergyLevel = averageEnergyLevel;
        this.averageLifeSpan = averageLifeSpan;
    }

    @Override
    public void start(Stage primaryStage) {
        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();
        chart = new LineChart(x, y);
        prepareData(0, animalNumber, averageChildrenAmount, plantNumber, averageEnergyLevel, averageLifeSpan);

        chart.getData().add(animalNumberChart);
        chart.getData().add(plantNumberChart);
        chart.getData().add(averageChildrenAmountChart);
        chart.getData().add(averageEnergyLevelChart);
        chart.getData().add(averageLifeSpanChart);
    }

    public void prepareData(int currentEpoch, int animalNumber, int averageChildrenAmount, int plantNumber,
                            double averageEnergyLevel, double averageLifeSpan) {
        animalNumberChart.setName("Animal number");
        animalNumberChart.getData().add(new XYChart.Data<>(currentEpoch, (double) animalNumber));

        plantNumberChart.setName("Plant number");
        plantNumberChart.getData().add(new XYChart.Data<>(currentEpoch, (double) plantNumber));

        averageEnergyLevelChart.setName("Average energy level");
        averageEnergyLevelChart.getData().add(new XYChart.Data<>(currentEpoch, averageEnergyLevel));

        averageLifeSpanChart.setName("Average life span");
        averageLifeSpanChart.getData().add(new XYChart.Data<>(currentEpoch, averageLifeSpan));

        averageChildrenAmountChart.setName("Average children amount");
        averageChildrenAmountChart.getData().add(new XYChart.Data<>(currentEpoch, (double) averageChildrenAmount));

    }

    public LineChart<Integer, Double> getChart() {
        return this.chart;
    }
}
