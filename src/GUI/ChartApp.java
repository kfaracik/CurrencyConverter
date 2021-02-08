package GUI;

import data.jsonParer.NbpCurrency;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.time.LocalDate;

public class ChartApp {

    NbpCurrency nbpData = new NbpCurrency();

    public void start(Stage stage, String code) throws Exception {

        initUI(stage, code);
    }

    private void initUI(Stage stage, String code) throws Exception {
        LocalDate curDate = LocalDate.now();
        LocalDate beginDate = curDate.plusMonths(-3);
        nbpData.updateHistData(beginDate.toString(), curDate.toString());
        stage.setOpacity(0.95);

        var root = new HBox();
        var scene = new Scene(root, 1050, 650);

        var xAxis = new CategoryAxis();
        xAxis.setLabel("Time");

        var yAxis = new NumberAxis();
        yAxis.setLabel(code);

        var areaChart = new AreaChart<>(xAxis, yAxis);
        areaChart.setTitle("Currency rate (base = PLN)");

        var data = new XYChart.Series<String, Number>();

        for (NbpCurrency.Root item : nbpData.item) {
            for(int j=0; j<item.rates.size(); j++) {
                if(code.equals(item.rates.get(j).code)) {
                    data.getData().add(new XYChart.Data<>(item.effectiveDate, item.rates.get(j).bid));
                }
            }
        }

        areaChart.getData().add(data);
        areaChart.setLegendVisible(false);
        areaChart.setMinSize(1000,500);

        root.getChildren().add(areaChart);

        stage.setTitle("AreaChart");
        stage.setScene(scene);
        stage.show();
    }
}
