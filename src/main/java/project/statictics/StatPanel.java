package project.statictics;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class StatPanel extends Application {
    public static HashMap<Integer,Integer> animalNumber = new HashMap<>(), plantNumber = new HashMap<>(),
            averageEnergyLevel = new HashMap<>(), averageLifeSpan = new HashMap<>(), averageChildrenAmount = new HashMap<>();
    private LineChart<Integer,Integer> chart;
    XYChart.Series<Integer,Integer> averageChildrenAmountChart = new XYChart.Series<>();
    XYChart.Series<Integer,Integer> animalNumberChart = new XYChart.Series<>();
    XYChart.Series<Integer,Integer> plantNumberChart = new XYChart.Series<>();
    XYChart.Series<Integer,Integer> averageEnergyLevelChart = new XYChart.Series<>();
    XYChart.Series<Integer,Integer> averageLifeSpanChart = new XYChart.Series<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();
        chart = new LineChart(x,y);
        chart.setTitle("Wykres");
        XYChart.Series<Integer,Integer> seria = new XYChart.Series<>();
        seria.getData().add(new XYChart.Data<>(1,2));
        seria.getData().add(new XYChart.Data<>(3,4));
        seria.getData().add(new XYChart.Data<>(10,20));
        seria.getData().add(new XYChart.Data<>(12,24));
        seria.getData().add(new XYChart.Data<>(10,217));
        seria.getData().add(new XYChart.Data<>(14,34));
        seria.getData().add(new XYChart.Data<>(16,28));

//        chart.getData().add(seria);
        prepareData();
        Scene scene = new Scene(chart,500,500);
        Stage myStage = new Stage();
        myStage.setTitle("Statistics");

        myStage.setScene(scene);
        myStage.show();

        chart.getData().add(animalNumberChart);
        chart.getData().add(plantNumberChart);
        chart.getData().add(averageChildrenAmountChart);
        chart.getData().add(averageEnergyLevelChart);
        chart.getData().add(averageLifeSpanChart);
    }


    public void prepareData(){

        animalNumberChart.setName("Animal number");
        for (Map.Entry<Integer,Integer> enitity : StatPanel.animalNumber.entrySet()) {
            animalNumberChart.getData().add(new XYChart.Data<>(enitity.getKey(),enitity.getValue()));
            StatPanel.animalNumber.remove(enitity.getKey());
        }


        plantNumberChart.setName("Plant number");
        for (Map.Entry<Integer,Integer> enitity : StatPanel.plantNumber.entrySet()) {
            plantNumberChart.getData().add(new XYChart.Data<>(enitity.getKey(),enitity.getValue()));
            StatPanel.plantNumber.remove(enitity.getKey());
        }


        averageEnergyLevelChart.setName("average Energy Level");
        for (Map.Entry<Integer,Integer> enitity : StatPanel.averageEnergyLevel.entrySet()) {
            averageEnergyLevelChart.getData().add(new XYChart.Data<>(enitity.getKey(),enitity.getValue()));
            StatPanel.averageEnergyLevel.remove(enitity.getKey());
        }


        averageLifeSpanChart.setName("average Life Span");
        for (Map.Entry<Integer,Integer> enitity : StatPanel.averageLifeSpan.entrySet()) {
            averageLifeSpanChart.getData().add(new XYChart.Data<>(enitity.getKey(),enitity.getValue()));
            StatPanel.averageLifeSpan.remove(enitity.getKey());
        }


        averageChildrenAmountChart.setName("average Children Amount");
        for (Map.Entry<Integer,Integer> enitity : StatPanel.averageChildrenAmount.entrySet()) {
            averageChildrenAmountChart.getData().add(new XYChart.Data<>(enitity.getKey(),enitity.getValue()));
            StatPanel.averageChildrenAmount.remove(enitity.getKey());
        }


    }

}
