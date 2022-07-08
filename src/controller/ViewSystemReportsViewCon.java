package controller;

import com.jfoenix.controls.JFXButton;
import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import manager.ItemManager;
import model.tableModel.IncomeTM;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import util.JasperUtil;

import java.sql.Connection;
import java.util.ArrayList;

public class ViewSystemReportsViewCon {

    public TableView<IncomeTM> tblIncomeDetailList;
    public TableColumn<IncomeTM,String> colDate;
    public TableColumn<IncomeTM,String> colTime;
    public TableColumn<IncomeTM,String> colIncome;

    public Label lblTotal;

    public JFXButton btnItemDetails;
    public JFXButton btnItemStocks;
    public JFXButton btnItemOrders;

    ObservableList<IncomeTM> obList= FXCollections.observableArrayList();

    public void initialize(){
        viewAllIncomes();
        calculateTotalIncome();

        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colIncome.setCellValueFactory(new PropertyValueFactory<>("income"));
    }

    private void calculateTotalIncome() {
        double total=0;
        for(IncomeTM incomeTM: obList) {
            total+=incomeTM.getIncome();
        }
        lblTotal.setText("Rs. "+String.format("%.2f",total));
    }

    private void viewAllIncomes() {
        ArrayList<IncomeTM> incomeList=new ItemManager().getAllIncomes();
        obList.addAll(incomeList);
        tblIncomeDetailList.setItems(obList);
    }

    public void viewItemDetailsOnAction(ActionEvent actionEvent) {
        new JasperUtil().loadReport("Item");
    }

    public void viewItemStocksOnAction(ActionEvent actionEvent) {
        new JasperUtil().loadReport("Stock");
    }

    public void viewItemOrdersOnAction(ActionEvent actionEvent) {
        new JasperUtil().loadReport("Order");
    }
}
