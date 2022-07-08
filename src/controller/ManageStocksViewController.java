package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import manager.ItemManager;
import manager.StockManager;
import model.Item;
import model.PriceDetail;
import model.Stock;
import model.StockDetail;
import util.ValidationUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class ManageStocksViewController {

    public TableView<Stock> tblStockList;
    public TableColumn<Stock, String> colBatchNo;
    public TableColumn<Stock, String> colSupplierId;
    public TableColumn<Stock, String> colShipmentDate;
    public TableColumn<Stock, String> colTime;
    public TableColumn<Stock, Double> colCost;

    public TableView<StockDetail> tblDetailList;
    public TableColumn<StockDetail, String> colItemCode;
    public TableColumn<StockDetail, Double> colUnitPrice;
    public TableColumn<StockDetail, Integer> colQuantity;
    public TableColumn<StockDetail, Double> colPrice;

    public TextField txtBatchNo;
    public TextField txtSupId;
    public TextField txtShipmentDate;
    public TextField txtTime;
    public TextField txtCost;
    public TextField txtItemCode;
    public TextField txtUnitPrice;
    public TextField txtQuantity;
    public TextField txtPrice;
    public JFXTextField orderSearchBar;

    public JFXButton btnUpdate;
    public JFXButton btnDelete;

    LinkedHashMap<TextField, Pattern> validationList1 = new LinkedHashMap<>();
    LinkedHashMap<TextField, Pattern> validationList2 = new LinkedHashMap<>();

    ObservableList<Stock> obListStock = FXCollections.observableArrayList();
    ObservableList<StockDetail> obListDetail = FXCollections.observableArrayList();
    StockDetail lastDetail;

    Pattern batchPattern = Pattern.compile("^(B-)[0-9]{5}$");
    Pattern idPattern = Pattern.compile("^(S-)[0-9]{3}$");
    Pattern datePattern = Pattern.compile("^[0-9]{4}([-][0-9]{2}){2}$");
    Pattern timePattern = Pattern.compile("^[0-9]{2}([:][0-9]{2}){2}[ ](AM|PM)$");
    Pattern pricePattern = Pattern.compile("^[1-9][0-9]{0,6}([.][0-9]{2})?$");
    Pattern codePattern = Pattern.compile("^(I-)[0-9]{5}$");
    Pattern quantityPattern = Pattern.compile("^[1-9][0-9]*$");

    public void initialize() {
        viewAllStocks();
        storeValidations();

        colBatchNo.setCellValueFactory(new PropertyValueFactory<>("batchNo"));
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colShipmentDate.setCellValueFactory(new PropertyValueFactory<>("shipmentDate"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        tblStockList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            viewAllDetails(newValue);
            setStockData(newValue);
        });

        tblDetailList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setStockDetailData(newValue);
            lastDetail = newValue;
        });

        listenFieldChange(validationList1);
        listenFieldChange(validationList2);

        listenStockChange(txtShipmentDate);
        listenStockChange(txtTime);

        listenDetailChange(txtItemCode);
        listenDetailChange(txtQuantity);

    }


    private void storeValidations() {
        validationList1.put(txtBatchNo, batchPattern);
        validationList1.put(txtSupId, idPattern);
        validationList1.put(txtShipmentDate, datePattern);
        validationList1.put(txtTime, timePattern);
        validationList1.put(txtCost, pricePattern);
        validationList2.put(txtItemCode, codePattern);
        validationList2.put(txtUnitPrice, pricePattern);
        validationList2.put(txtQuantity, quantityPattern);
        validationList2.put(txtPrice, pricePattern);
    }

    private void listenStockChange(TextField field) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (txtBatchNo.getParent().getStyle().equals("-fx-border-color: #028f02") && txtShipmentDate.getParent().getStyle().equals("-fx-border-color: #028f02") && txtTime.getParent().getStyle().equals("-fx-border-color: #028f02")) {
                ArrayList<StockDetail> detailList = new StockManager().getAllStockDetails(txtBatchNo.getText());
                double cost = 0;
                for (StockDetail sd : detailList) {
                    int qty = sd.getQuantity();
                    Item i = new ItemManager().getItem(sd.getItemCode());
                    PriceDetail pd = new ItemManager().getPriceDetail(i, txtShipmentDate.getText(), txtTime.getText());
                    cost += qty * pd.getUnitPrice();
                }
                txtCost.setText(String.format("%.2f", cost));
                if (txtItemCode.getParent().getStyle().equals("-fx-border-color: #028f02")) {
                    Item item = new ItemManager().getItem(txtItemCode.getText());
                    if (item != null) {
                        PriceDetail pd = new ItemManager().getPriceDetail(item, txtShipmentDate.getText(), txtTime.getText());
                        double unitPrice = pd.getUnitPrice();
                        txtUnitPrice.setText(String.format("%.2f", unitPrice));
                        if (txtQuantity.getParent().getStyle().equals("-fx-border-color: #028f02")) {
                            setPriceFields(txtBatchNo.getText(), unitPrice);
                        }
                    }
                }
            }
        });
    }

    private void listenFieldChange(LinkedHashMap<TextField, Pattern> list) {
        for (TextField key : list.keySet()) {
            key.textProperty().addListener((observable, oldValue, newValue) -> {
                ValidationUtil.validate(key, list);
            });
        }
    }

    private void listenDetailChange(TextField field) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (txtBatchNo.getParent().getStyle().equals("-fx-border-color: #028f02") && lastDetail != null) {
                if (txtItemCode.getParent().getStyle().equals("-fx-border-color: #028f02")) {
                    Item item = new ItemManager().getItem(txtItemCode.getText());
                    if (item == null) {
                        txtItemCode.getParent().setStyle("-fx-border-color: #DB0F0F");
                        AnchorPane parent = (AnchorPane) txtItemCode.getParent();
                        Label tag = (Label) parent.getChildren().get(2);
                        tag.setStyle("-fx-text-fill: #DB0F0F");
                    } else {
                        boolean isExists = false;
                        ArrayList<StockDetail> detailList = new StockManager().getAllStockDetails(txtBatchNo.getText());
                        for (StockDetail sd : detailList) {
                            if (!lastDetail.getItemCode().equals(item.getCode()) && sd.getItemCode().equals(item.getCode())) {
                                isExists = true;
                                break;
                            }
                        }
                        if (isExists) {
                            if (!lastDetail.getItemCode().equals(item.getCode())) {
                                txtItemCode.getParent().setStyle("-fx-border-color: #DB0F0F");
                                AnchorPane parent = (AnchorPane) txtItemCode.getParent();
                                Label tag = (Label) parent.getChildren().get(2);
                                tag.setStyle("-fx-text-fill: #DB0F0F");
                                new Alert(Alert.AlertType.WARNING, "Medicine already exists in the list...").show();
                                //txtMedicineCode.setText(lastDetail.getMedicineCode());
                            }
                        } else {
                            if (txtShipmentDate.getParent().getStyle().equals("-fx-border-color: #028f02")) {
                                if (txtTime.getParent().getStyle().equals("-fx-border-color: #028f02")) {
                                    PriceDetail pd = new ItemManager().getPriceDetail(item, txtShipmentDate.getText(), txtTime.getText());
                                    double unitPrice = pd.getUnitPrice();
                                    txtUnitPrice.setText(String.format("%.2f", unitPrice));
                                    if (txtQuantity.getParent().getStyle().equals("-fx-border-color: #028f02")) {
                                        setPriceFields(txtBatchNo.getText(), unitPrice);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private void setPriceFields(String batchNo, double unitPrice) {
        ArrayList<StockDetail> detailList = new StockManager().getAllStockDetails(batchNo);
        int qty = Integer.parseInt(txtQuantity.getText());
        double price = qty * unitPrice;
        txtPrice.setText(String.format("%.2f", price));
        double cost = 0;
        for (StockDetail sd : detailList) {
            int sdQty = sd.getQuantity();
            Item i = new ItemManager().getItem(sd.getItemCode());
            PriceDetail sdPD = new ItemManager().getPriceDetail(i, txtShipmentDate.getText(), txtTime.getText());
            cost += sdQty * sdPD.getUnitPrice();
        }
        int lastQty = lastDetail.getQuantity();
        Item lastMed = new ItemManager().getItem(lastDetail.getItemCode());
        PriceDetail lastPD = new ItemManager().getPriceDetail(lastMed, txtShipmentDate.getText(), txtTime.getText());
        double lastPrice = lastQty * lastPD.getUnitPrice();
        double finalCost = cost - lastPrice + price;
        txtCost.setText(String.format("%.2f", finalCost));
    }

    private void setStockDetailData(StockDetail stockDetail) {
        if (stockDetail != null) {
            txtItemCode.setText(stockDetail.getItemCode());
            /*txtExpiryDate.setText(stockDetail.getExpiryDate());*/
            Item item = new ItemManager().getItem(stockDetail.getItemCode());
            txtUnitPrice.setText(String.format("%.2f", stockDetail.getUnitPrice()));
            int qty = stockDetail.getQuantity();
            txtQuantity.setText(Integer.toString(qty));
            txtPrice.setText(String.format("%.2f", stockDetail.getPrice()));
            if (txtShipmentDate.getParent().getStyle().equals("-fx-border-color: #028f02") && txtTime.getParent().getStyle().equals("-fx-border-color: #028f02")) {
                PriceDetail pd = new ItemManager().getPriceDetail(item, txtShipmentDate.getText(), txtTime.getText());
                double unitPrice = pd.getUnitPrice();
                txtUnitPrice.setText(String.format("%.2f", unitPrice));
                double price = qty * unitPrice;
                txtPrice.setText(String.format("%.2f", price));
            }
        }
    }

    private void setStockData(Stock stock) {
        if (stock != null) {
            txtBatchNo.setText(stock.getBatchNo());
            txtSupId.setText(stock.getSupplierId());
            txtShipmentDate.setText(stock.getShipmentDate());
            txtTime.setText(stock.getTime());
            txtCost.setText(String.format("%.2f", stock.getCost()));
        }
    }

    private void clearAllDetails() {
        obListDetail.clear();
        obListDetail.addAll(new ArrayList<>());
        tblDetailList.setItems(obListDetail);
    }

    private void viewAllDetails(Stock stock) {
        if (stock != null && obListDetail != null) {
            obListDetail.clear();
            ArrayList<StockDetail> detailList = stock.getDetailList();
            obListDetail.addAll(detailList);
            tblDetailList.setItems(obListDetail);
        }
    }

    private void viewAllStocks() {
        obListStock.clear();
        ArrayList<Stock> stocks = new StockManager().getAllStocks();
        obListStock.addAll(stocks);
        tblStockList.setItems(obListStock);
    }

    private boolean isAllFieldsEmpty(LinkedHashMap<TextField, Pattern> list) {
        for (TextField key : list.keySet()) {
            if (key.getStyle().equals("-fx-border-color: #028f02") || key.getParent().getStyle().equals("-fx-border-color: #DB0F0F")) {
                return false;
            }
        }
        return true;
    }

    private boolean isFormNotFilled(LinkedHashMap<TextField, Pattern> validationList) {
        for (TextField key : validationList.keySet()) {
            if (!key.getStyle().equals("-fx-border-color: #028f02")) {
                return true;
            }
        }
        return false;
    }

    public void updateOnAction(ActionEvent actionEvent) {
        ArrayList<StockDetail> detailList = new StockManager().getAllStockDetails(txtBatchNo.getText());
        for (StockDetail detail : detailList) {
            Item item = new ItemManager().getItem(detail.getItemCode());
            String period = item.getPreservedTime();
            String date = txtShipmentDate.getText();

            int qty = detail.getQuantity();
            PriceDetail pd = new ItemManager().getPriceDetail(item, txtShipmentDate.getText(), txtTime.getText());
            double unitPrice = pd.getUnitPrice();
            detail.setUnitPrice(unitPrice);
            detail.setPrice(qty * unitPrice);
        }
        Stock stock = new Stock(
                txtBatchNo.getText(),
                txtSupId.getText(),
                txtShipmentDate.getText(),
                txtTime.getText(),
                Double.parseDouble(txtCost.getText()),
                detailList
        );

        StockDetail newDetail = new StockDetail(
                txtBatchNo.getText(),
                txtItemCode.getText(),
                Double.parseDouble(txtUnitPrice.getText()),
                Integer.parseInt(txtQuantity.getText()),
                Double.parseDouble(txtPrice.getText())
        );
        StockDetail oldDetail = null;
        for (StockDetail sd : detailList) {
            if (lastDetail.getItemCode().equals(sd.getItemCode())) {
                oldDetail = sd;
            }
        }
        if (new StockManager().updateStockDetail(stock, oldDetail, newDetail)) {
            new Alert(Alert.AlertType.CONFIRMATION, "Updated...").show();
            viewAllStocks();
            clearAllDetails();
        } else {
            new Alert(Alert.AlertType.WARNING, "Try again...").show();
        }
    }

    public void deleteOnAction(ActionEvent actionEvent) {
        if (isFormNotFilled(validationList1)) {
            new Alert(Alert.AlertType.WARNING, "Not selected any Stock..").show();
        } else {
            Stock stock = new StockManager().getStock(txtBatchNo.getText());
            if (new StockManager().deleteStock(stock)) {
                new Alert(Alert.AlertType.CONFIRMATION, "Deleted...").show();
                viewAllStocks();
                clearAllDetails();
            } else {
                new Alert(Alert.AlertType.WARNING, "Try again...").show();
            }
            boolean isExists = false;
            ArrayList<StockDetail> detailList = new StockManager().getAllStockDetails(stock.getBatchNo());
            for (StockDetail sd : detailList) {
                if (sd.getItemCode().equals(txtItemCode.getText())) {
                    isExists = true;
                    break;
                }
            }
            if (!isExists) {
                new Alert(Alert.AlertType.WARNING, "Item doesn't exist in the list...").show();
            } else {
                StockDetail detail = new StockManager().getStockDetail(stock.getBatchNo(), txtItemCode.getText());
                if (new StockManager().deleteStockDetail(detail)) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Deleted...").show();
                    viewAllStocks();
                    clearAllDetails();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Fucked Up").show();
                }
            }
        }
    }
}

