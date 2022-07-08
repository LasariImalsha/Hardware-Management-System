package controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import manager.ItemManager;
import manager.StockManager;
import manager.SupplierManager;
import model.Stock;
import model.tableModel.IncomeTM;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public class AdminDashboardController {
    public AnchorPane context;
    public JFXButton btnLogout;
    public AnchorPane adminView;
    public Label lblDate;
    public Label lblTime;
    public Label lblYear;
    public Label lblAnnualIncome;
    public Label lblMonth;
    public Label lblMonthlyIncome;
    public Label lblDay;
    public Label lblDailyIncome;
    public PieChart pieChart;

    public void initialize(){
        loadDateAndTime();
        loadPieChart();
        setIncomeLabels();
    }

    private void setIncomeLabels() {
        double annualIncome=0;
        double monthlyIncome=0;
        double dailyIncome=0;
        ArrayList<IncomeTM> incomeList=new ItemManager().getAllIncomes();
        for(IncomeTM tm: incomeList){
            String[] dateArray=tm.getDate().split("-");
            if(new SimpleDateFormat("yyyy").format(new Date()).equals(dateArray[0])){
                annualIncome+=tm.getIncome();
                if(new SimpleDateFormat("MM").format(new Date()).equals(dateArray[1])){
                    monthlyIncome+=tm.getIncome();
                    if(new SimpleDateFormat("dd").format(new Date()).equals(dateArray[2])){
                        dailyIncome+=tm.getIncome();
                    }
                }
            }
        }
        lblAnnualIncome.setText("Rs. "+String.format("%.2f",annualIncome));
        lblMonthlyIncome.setText("Rs. "+String.format("%.2f",monthlyIncome));
        lblDailyIncome.setText("Rs. "+String.format("%.2f",dailyIncome));
        lblYear.setText(new SimpleDateFormat("yyyy").format(new Date()));
        lblMonth.setText(new SimpleDateFormat("MMMM yyyy").format(new Date()));
        lblDay.setText(new SimpleDateFormat("dd MMM yyyy").format(new Date()));
    }

    private void loadPieChart() {
        ObservableList<PieChart.Data> list= FXCollections.observableArrayList();
        ArrayList<Stock> stockList=new StockManager().getAllStocks();
        ArrayList<String> supplierList=new SupplierManager().getAllSupplierIds();
        for(String supplier:supplierList) {
            double total=0;
            for(Stock stock:stockList) {
                if(stock.getSupplierId().equals(supplier)){
                    total+=stock.getCost();
                }
            }
            list.add(new PieChart.Data(supplier,total));
        }

        list.forEach(data -> data.nameProperty().bind(
                Bindings.concat(data.getName()," : Rs.",data.pieValueProperty())
        ));

        pieChart.getData().addAll(list);
        //performanceContext.getChildren().add(pieChart);
    }



    private void loadDateAndTime() {
        lblDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            lblTime.setText(currentTime.getHour() + ":" +
                    currentTime.getMinute() + ":" +
                    currentTime.getSecond());
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    public void manageEmployeeOnAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/ManageEmployeesView.fxml"));
        context.getChildren().add(parent);
    }

    public void manageItemOnAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/ManageItemView.fxml"));
        context.getChildren().add(parent);
    }

    public void viewReportsOnAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/ViewSystemReportsView.fxml"));
        context.getChildren().add(parent);
    }

    public void viewLoginDetailsOnAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/ViewLoginDetailsView.fxml"));
        context.getChildren().add(parent);
    }

    public void viewLoginPageOnAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/LoginForm.fxml"));
        adminView.getChildren().add(parent);
    }
}
