package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import database.DataSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import manager.ItemManager;
import manager.OrderManager;
import model.*;
import util.CrudUtil;
import util.ValidationUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class ManageOrdersViewController {

    public TableView<Order> tblOrderList;
    public TableColumn<Order, String> colOrderId1;
    public TableColumn<Order, String> colCustId;
    public TableColumn<Order, String> colOrderDate;
    public TableColumn<Order, String> colTime;
    public TableColumn<Order, Double> colCost;

    public TableView<OrderDetail> tblDetailList;
    public TableColumn<OrderDetail, String> colOrderId2;
    public TableColumn<OrderDetail, String> colItemCode;
    public TableColumn<OrderDetail, Double> colUnitPrice;
    public TableColumn<OrderDetail, Double> colPrice;
    public TableColumn<OrderDetail, Integer> colQuantity;

    public TextField txtOrderId;
    public TextField txtCustId;
    public TextField txtOrderDate;
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

    ObservableList<Order> obListOrder = FXCollections.observableArrayList();
    ObservableList<OrderDetail> obListDetail = FXCollections.observableArrayList();
    OrderDetail lastDetail;

    Pattern orderPattern = Pattern.compile("^(O-)[0-9]{6}$");
    Pattern idPattern = Pattern.compile("^(C-)[0-9]{5}$");
    Pattern datePattern = Pattern.compile("^[0-9]{4}([-][0-9]{2}){2}$");
    Pattern timePattern = Pattern.compile("^[0-9]{2}([:][0-9]{2}){2}[ ](AM|PM)$");
    Pattern pricePattern = Pattern.compile("^[1-9][0-9]{0,6}([.][0-9]{2})?$");
    Pattern codePattern = Pattern.compile("^(I-)[0-9]{5}$");
    Pattern quantityPattern = Pattern.compile("^[1-9][0-9]*$");

    public void initialize() {
        storeValidations();
        viewAllOrders();

        colOrderId1.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colCustId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));

        colOrderId2.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        tblOrderList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            viewAllDetails(newValue);
            setOrderData(newValue);
        });

        tblDetailList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setOrderDetailData(newValue);
            lastDetail = newValue;
        });

        listenFieldChange(validationList1);
        listenFieldChange(validationList2);

        listenOrderChange(txtOrderDate);
        listenOrderChange(txtTime);

        listenDetailChange(txtItemCode);
        listenDetailChange(txtQuantity);
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
            if (txtOrderId.getParent().getStyle().equals("-fx-border-color: #028f02") && lastDetail != null) {
                if (txtItemCode.getParent().getStyle().equals("-fx-border-color: #028f02")) {
                    Item item = new ItemManager().getItem(txtItemCode.getText());
                    if (item == null) {
                        txtItemCode.getParent().setStyle("-fx-border-color: #DB0F0F");
                        AnchorPane parent = (AnchorPane) txtItemCode.getParent();
                        Label tag = (Label) parent.getChildren().get(2);
                        tag.setStyle("-fx-text-fill: #DB0F0F");
                    } else {
                        boolean isExists = false;
                        ArrayList<OrderDetail> detailList = new OrderManager().getAllOrderDetails(txtOrderId.getText());
                        for (OrderDetail od : detailList) {
                            if (!lastDetail.getItemCode().equals(item.getCode()) && od.getItemCode().equals(item.getCode())) {
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
                            }
                        } else {
                            if (txtOrderDate.getParent().getStyle().equals("-fx-border-color: #028f02") && txtTime.getParent().getStyle().equals("-fx-border-color: #028f02")) {
                                PriceDetail pd = new ItemManager().getPriceDetail(item, txtOrderDate.getText(), txtTime.getText());
                                double unitPrice = pd.getUnitSalePrice();
                                txtUnitPrice.setText(String.format("%.2f", unitPrice));
                                if (quantityPattern.matcher(txtQuantity.getText()).matches()) {
                                    setPriceFields(txtOrderId.getText(), unitPrice);
                                    OrderDetail detail = new OrderManager().getOrderDetail(txtOrderId.getText(), item.getCode());
                                    int qtyOnHand = item.getQtyOnHand();
                                    int qtyRqrd = Integer.parseInt(txtQuantity.getText());
                                    int qtyTaken = 0;
                                    if (detail != null) {
                                        qtyTaken += detail.getQuantity();
                                    }
                                    if (qtyRqrd > (qtyOnHand + qtyTaken)) {
                                        txtQuantity.getParent().setStyle("-fx-border-color: #DB0F0F");
                                        AnchorPane parent = (AnchorPane) txtQuantity.getParent();
                                        Label tag = (Label) parent.getChildren().get(2);
                                        tag.setStyle("-fx-text-fill: #DB0F0F");
                                        new Alert(Alert.AlertType.WARNING, "Insufficient quantity...").show();
                                    } else {
                                        txtQuantity.getParent().setStyle("-fx-border-color: #028f02");
                                        AnchorPane parent = (AnchorPane) txtQuantity.getParent();
                                        Label tag = (Label) parent.getChildren().get(2);
                                        tag.setStyle("-fx-text-fill: #028f02");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private void listenOrderChange(TextField field) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (txtOrderId.getParent().getStyle().equals("-fx-border-color: #028f02") && txtOrderDate.getParent().getStyle().equals("-fx-border-color: #028f02") && txtTime.getParent().getStyle().equals("-fx-border-color: #028f02")) {
                ArrayList<OrderDetail> detailList = new OrderManager().getAllOrderDetails(txtOrderId.getText());
                double cost = 0;
                for (OrderDetail od : detailList) {
                    int qty = od.getQuantity();
                    Item m = new ItemManager().getItem(od.getItemCode());
                    PriceDetail pd = new ItemManager().getPriceDetail(m, txtOrderDate.getText(), txtTime.getText());
                    cost += qty * pd.getUnitSalePrice();
                }
                txtCost.setText(String.format("%.2f", cost));
                if (txtItemCode.getParent().getStyle().equals("-fx-border-color: #028f02")) {
                    Item item = new ItemManager().getItem(txtItemCode.getText());
                    if (item != null) {
                        PriceDetail pd = new ItemManager().getPriceDetail(item, txtOrderDate.getText(), txtTime.getText());
                        double unitPrice = pd.getUnitSalePrice();
                        txtUnitPrice.setText(String.format("%.2f", unitPrice));
                        if (txtQuantity.getParent().getStyle().equals("-fx-border-color: #028f02")) {
                            setPriceFields(txtOrderId.getText(), unitPrice);
                        }
                    }
                }
            }
        });
    }

    private void storeValidations() {
        validationList1.put(txtOrderId, orderPattern);
        validationList1.put(txtCustId, idPattern);
        validationList1.put(txtOrderDate, datePattern);
        validationList1.put(txtTime, timePattern);
        validationList1.put(txtCost, pricePattern);
        validationList2.put(txtItemCode, codePattern);
        validationList2.put(txtUnitPrice, pricePattern);
        validationList2.put(txtQuantity, quantityPattern);
        validationList2.put(txtPrice, pricePattern);
    }

    private boolean isFormNotFilled(LinkedHashMap<TextField, Pattern> validationList) {
        for (TextField key : validationList.keySet()) {
            if (!key.getStyle().equals("-fx-border-color: #028f02")) {
                return true;
            }
        }
        return false;
    }

    private boolean isAllFieldsEmpty(LinkedHashMap<TextField, Pattern> list) {
        for (TextField key : list.keySet()) {
            if (key.getParent().getStyle().equals("-fx-border-color: #028f02") || key.getParent().getStyle().equals("-fx-border-color: #DB0F0F")) {
                return false;
            }
        }
        return true;
    }


    private void setPriceFields(String orderId, double unitPrice) {
        ArrayList<OrderDetail> detailList = new OrderManager().getAllOrderDetails(orderId);
        int qty = Integer.parseInt(txtQuantity.getText());
        double price = qty * unitPrice;
        txtPrice.setText(String.format("%.2f", price));
        double cost = 0;
        for (OrderDetail od : detailList) {
            int odQty = od.getQuantity();
            Item m = new ItemManager().getItem(od.getItemCode());
            PriceDetail odPD = new ItemManager().getPriceDetail(m, txtOrderDate.getText(), txtTime.getText());
            cost += odQty * odPD.getUnitSalePrice();
        }
        int lastQty = lastDetail.getQuantity();
        Item lastMed = new ItemManager().getItem(lastDetail.getItemCode());
        PriceDetail lastPD = new ItemManager().getPriceDetail(lastMed, txtOrderDate.getText(), txtTime.getText());
        double lastPrice = lastQty * lastPD.getUnitSalePrice();
        double finalCost = cost - lastPrice + price;
        txtCost.setText(String.format("%.2f", finalCost));
    }

    private void setOrderDetailData(OrderDetail orderDetail) {
        if (orderDetail != null) {
            txtItemCode.setText(orderDetail.getItemCode());
            Item item = new ItemManager().getItem(orderDetail.getItemCode());
            txtUnitPrice.setText(String.format("%.2f", orderDetail.getUnitPrice()));
            int qty = orderDetail.getQuantity();
            txtQuantity.setText(Integer.toString(qty));
            txtPrice.setText(String.format("%.2f", orderDetail.getPrice()));
            if (txtOrderDate.getParent().getStyle().equals("-fx-border-color: #028f02") && txtTime.getParent().getStyle().equals("-fx-border-color: #028f02")) {
                PriceDetail pd = new ItemManager().getPriceDetail(item, txtOrderDate.getText(), txtTime.getText());
                double unitPrice = pd.getUnitSalePrice();
                txtUnitPrice.setText(String.format("%.2f", unitPrice));
                double price = qty * unitPrice;
                txtPrice.setText(String.format("%.2f", price));
            }
        }
    }

    private void setOrderData(Order order) {
        if (order != null) {
            txtOrderId.setText(order.getOrderId());
            txtCustId.setText(order.getCustomerId());
            txtOrderDate.setText(order.getOrderDate());
            txtTime.setText(order.getTime());
            txtCost.setText(String.format("%.2f", order.getCost()));
        }
    }

    private void clearAllDetails() {
        obListDetail.clear();
        obListDetail.addAll(new ArrayList<>());
        tblDetailList.setItems(obListDetail);
    }

    private void viewAllDetails(Order order) {
        if (order != null && obListDetail != null) {
            obListDetail.clear();
            ArrayList<OrderDetail> detailList = order.getDetailList();
            obListDetail.addAll(detailList);
            tblDetailList.setItems(obListDetail);
        }
    }

    private void viewAllOrders() {
        obListOrder.clear();
        ArrayList<Order> orders = new OrderManager().getAllOrders();
        obListOrder.addAll(orders);
        tblOrderList.setItems(obListOrder);
    }

    private void clearAllFields() {
        for (TextField key : validationList1.keySet()) {
            key.clear();
        }
        for (TextField key : validationList2.keySet()) {
            key.clear();
        }
    }

    public void updateOnAction(ActionEvent actionEvent) {
        if (isFormNotFilled(validationList1)) {
            new Alert(Alert.AlertType.WARNING, "Not selected any Order..").show();
        } else {
            ArrayList<OrderDetail> detailList = new OrderManager().getAllOrderDetails(txtOrderId.getText());
            for (OrderDetail detail : detailList) {
                Item medicine = new ItemManager().getItem(detail.getItemCode());
                int qty = detail.getQuantity();
                PriceDetail pd = new ItemManager().getPriceDetail(medicine, txtOrderDate.getText(), txtTime.getText());
                double unitPrice = pd.getUnitSalePrice();
                detail.setUnitPrice(unitPrice);
                detail.setPrice(qty * unitPrice);
            }

            Order order = new Order(
                    txtOrderId.getText(),
                    txtCustId.getText(),
                    txtOrderDate.getText(),
                    txtTime.getText(),
                    Double.parseDouble(txtCost.getText()),
                    detailList
            );
            if (new OrderManager().updateOrder(order)) {
                new Alert(Alert.AlertType.CONFIRMATION, "Updated...").show();
                viewAllOrders();
                clearAllDetails();
                clearAllFields();
            } else {
                new Alert(Alert.AlertType.WARNING, "Try again...").show();
            }

            OrderDetail newDetail = new OrderDetail(
                    txtOrderId.getText(),
                    txtItemCode.getText(),
                    Double.parseDouble(txtUnitPrice.getText()),
                    Integer.parseInt(txtQuantity.getText()),
                    Double.parseDouble(txtPrice.getText())
            );
            System.out.println(txtUnitPrice.getText());
            OrderDetail oldDetail = null;
            for (OrderDetail od : detailList) {
                if (lastDetail.getItemCode().equals(od.getItemCode())) {
                    oldDetail = od;
                }
            }
            if (new OrderManager().updateOrderDetail(order, oldDetail, newDetail)) {
                new Alert(Alert.AlertType.CONFIRMATION, "Updated...").show();
                viewAllOrders();
                clearAllDetails();
                clearAllFields();
            } else {
                new Alert(Alert.AlertType.WARNING, "Try again...").show();
            }
        }
    }
    public void searchOnAction(ActionEvent actionEvent) {
        try {
            ResultSet result = CrudUtil.execute("SELECT * FROM Order WHERE id=?", txtOrderId.getText());
            if (result.next()) {
                txtCustId.setText(result.getString(2));
                txtOrderDate.setText(result.getString(3));
                txtTime.setText(result.getString(4));
                txtCost.setText(String.valueOf(result.getDouble(5)));
                txtItemCode.setText(result.getString(6));
                txtUnitPrice.setText(String.valueOf(result.getDouble(7)));
                txtQuantity.setText(result.getString(8));
                txtPrice.setText(String.valueOf(result.getDouble(9)));
            } else {
                new Alert(Alert.AlertType.WARNING, "Empty Result").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void deleteOnAction(ActionEvent actionEvent) {
        if (isFormNotFilled(validationList1)) {
            new Alert(Alert.AlertType.WARNING, "Not selected any Stock..").show();
        } else {
            Order order = new OrderManager().getOrder(txtOrderId.getText());
            if (new OrderManager().deleteOrder(order)) {
                new Alert(Alert.AlertType.CONFIRMATION, "Deleted...").show();
                viewAllOrders();
                clearAllDetails();
                clearAllFields();
            } else {
                new Alert(Alert.AlertType.WARNING, "Try again...").show();
            }
            boolean isExists = false;
            ArrayList<OrderDetail> detailList = new OrderManager().getAllOrderDetails(order.getOrderId());
            for (OrderDetail od : detailList) {
                if (od.getItemCode().equals(txtItemCode.getText())) {
                    isExists = true;
                    break;
                }
            }
            if (!isExists) {
                new Alert(Alert.AlertType.WARNING, "Item doesn't exist in the list...").show();
            } else {
                OrderDetail detail = new OrderManager().getOrderDetail(order.getOrderId(), txtItemCode.getText());
                if (new OrderManager().deleteOrderDetail(detail)) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Deleted...").show();
                    viewAllOrders();
                    clearAllDetails();
                    clearAllFields();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Error..").show();
                }

            }

        }
    }
}
