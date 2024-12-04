package org.example.ilib.adminmenu;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerAdminStatistic implements Initializable {
    @FXML
    private PieChart BookChart;
    @FXML
    private BarChart<String, Number> UserChart;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Thêm dữ liệu cho PieChart
        BookChart.setData(FXCollections.observableArrayList(
                new PieChart.Data("Books Borrowed", 70),
                new PieChart.Data("Books Available", 30)
        ));
        BookChart.setLegendVisible(false); // Ẩn thanh ký hiệu

        // Thêm dữ liệu cho BarChart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("User Statistics");
        series.getData().add(new XYChart.Data<>("Active Users", 50));
        series.getData().add(new XYChart.Data<>("Inactive Users", 20));
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Active Books");
        series1.getData().add(new XYChart.Data<>("ActiveX Users", 50));
        series1.getData().add(new XYChart.Data<>("InactiveX Users", 20));
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Inactive Books");
        series2.getData().add(new XYChart.Data<>("ActiveY Users", 50));
        series2.getData().add(new XYChart.Data<>("InactiveY Users", 20));
        UserChart.getData().add(series);
        UserChart.getData().add(series1);
        UserChart.getData().add(series2);
        for (XYChart.Data<String, Number> data : series.getData()) {
            data.getNode().setStyle("-fx-bar-width: 10px"); // Điều chỉnh độ rộng thanh
        }
       // UserChart.setLegendVisible(false); // Ẩn thanh ký hiệu
    }
}
