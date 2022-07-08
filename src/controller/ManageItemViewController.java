package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import database.DataSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import manager.ItemManager;
import model.Customer;
import model.Item;
import util.CrudUtil;
import util.ValidationUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class ManageItemViewController {

    public TableView<Item> tblItemList;
    public TableColumn<Item, String> colItemCode;
    public TableColumn<Item, String> colName;
    public TableColumn<Item, String> colDescription;
    public TableColumn<Item, String> colPreservedTime;
    public TableColumn<Item, String> colUnitPrice;
    public TableColumn<Item, String> colUnitSalePrice;

    public JFXButton btnAdd;
    public JFXButton btnDelete;
    public JFXButton btnUpdate;

    public JFXTextField itemSearchBar;
    public TextField txtItemCode1;
    public TextField txtName1;
    public TextField txtDescription1;
    public TextField txtUnitPrice1;
    public TextField txtPreservedTime1;
    public TextField txtUnitSalePrice1;
    public TextField txtItemCode2;
    public TextField txtName2;
    public TextField txtDescription2;
    public TextField txtPreservedTime2;
    public TextField txtUnitPrice2;
    public TextField txtUniteSalePrice2;

    LinkedHashMap<TextField, Pattern> validationList1 = new LinkedHashMap<>();
    LinkedHashMap<TextField, Pattern> validationList2 = new LinkedHashMap<>();

    ObservableList<Item> obList = FXCollections.observableArrayList();

    Pattern codePattern = Pattern.compile("^(I-)[0-9]{5}$");
    Pattern namePattern = Pattern.compile("^[A-Z][a-z]*([ ][A-Z][a-z]*)*$");
    Pattern descPattern = Pattern.compile("^[A-Z][A-z0-9, ]*$");
    Pattern timePeriodPattern = Pattern.compile("^[0-9]{1,3}[ ](weeks|months|years)$");
    Pattern pricePattern = Pattern.compile("^[1-9][0-9]{0,6}([.][0-9]{2})?$");

    public void initialize() {
        storeValidations();
        setItemCode();
        viewAllItem();

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPreservedTime.setCellValueFactory(new PropertyValueFactory<>("preservedTime"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colUnitSalePrice.setCellValueFactory(new PropertyValueFactory<>("unitSalePrice"));

        tblItemList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    setItemData(newValue) ;
                }
        );

        listenFieldChange(validationList1);
        listenFieldChange(validationList2);

        txtItemCode2.setOnKeyPressed( event -> {
            if( event.getCode() == KeyCode.ENTER ) {
                setItemData( new ItemManager().getItem(txtItemCode2.getText()));
            }
        } );


    }

    private void setItemData(Item item) {
        if(item!=null) {
            txtItemCode2.setText(item.getCode());
            txtName2.setText(item.getName());
            txtDescription2.setText(item.getDescription());
            txtPreservedTime2.setText(item.getPreservedTime());
            txtUnitPrice2.setText(String.format("%.2f", item.getUnitPrice()));
            txtUniteSalePrice2.setText(String.format("%.2f", item.getUnitSalePrice()));
        }
    }

    private void listenFieldChange(LinkedHashMap<TextField, Pattern> list) {
        for (TextField key : list.keySet()) {
            key.textProperty().addListener((observable, oldValue, newValue) -> {
                ValidationUtil.validate(key, list);
            });
        }
    }

    private boolean isFormNotFilled(LinkedHashMap<TextField, Pattern> validationList) {
        for (TextField key : validationList.keySet()) {
            if (!key.getStyle().equals("-fx-border-color: #028f02")) {
                return true;
            }
        }
        return false;
    }

    private void storeValidations() {
        validationList1.put(txtItemCode1, codePattern);
        validationList1.put(txtName1, namePattern);
        validationList1.put(txtDescription1, descPattern);
        validationList1.put(txtPreservedTime1, timePeriodPattern);
        validationList1.put(txtUnitPrice1, pricePattern);
        validationList1.put(txtUnitSalePrice1, pricePattern);
        validationList2.put(txtItemCode2, codePattern);
        validationList2.put(txtName2, namePattern);
        validationList2.put(txtDescription2, descPattern);
        validationList2.put(txtPreservedTime2, timePeriodPattern);
        validationList2.put(txtUnitPrice2, pricePattern);
        validationList2.put(txtUniteSalePrice2, pricePattern);
    }

    private void viewAllItem() {
        obList.clear();
        ArrayList<Item> itemList = new ItemManager().getAllItem();
        obList.addAll(itemList);
        tblItemList.setItems(obList);
    }

    private void setItemCode() {
        txtItemCode1.setText(new ItemManager().getItemCode());
    }

    private void clearFields(LinkedHashMap<TextField, Pattern> list) {
        for (TextField key : list.keySet()) {
            key.clear();
        }
    }

    public void addItemOnAction(ActionEvent actionEvent) {
        if (isFormNotFilled(validationList1)) {
            new Alert(Alert.AlertType.WARNING, "Fields are not filled properly...").show();
        } else {
            Item item = new Item(
                    txtItemCode1.getText(),
                    txtName1.getText(),
                    txtDescription1.getText(),
                    txtPreservedTime1.getText(),
                    0,
                    Double.parseDouble(txtUnitPrice1.getText()),
                    Double.parseDouble(txtUnitSalePrice1.getText())
            );
            if (new ItemManager().addItem(item)) {
                new Alert(Alert.AlertType.CONFIRMATION, "Saved...").show();
                clearFields(validationList1);
                viewAllItem();
                setItemCode();
            } else {
                new Alert(Alert.AlertType.WARNING, "Try again...").show();
            }
        }
    }

    public void deleteItemOnAction(ActionEvent actionEvent) {
        if (isFormNotFilled(validationList2)) {
            new Alert(Alert.AlertType.WARNING, "Fields are not filled properly...").show();
        } else {
            String medicineCode = txtItemCode2.getText();
            if (new ItemManager().deleteItem(medicineCode)) {
                new Alert(Alert.AlertType.CONFIRMATION, "Deleted...").show();
                clearFields(validationList2);
                viewAllItem();
            } else {
                new Alert(Alert.AlertType.WARNING, "Try again...").show();
            }
        }
    }

    public void updateItemOnAction(ActionEvent actionEvent) {
        if (isFormNotFilled(validationList2)) {
            new Alert(Alert.AlertType.WARNING, "Fields are not filled properly...").show();
        } else {
            Item item = new Item(
                    txtItemCode2.getText(),
                    txtName2.getText(),
                    txtDescription2.getText(),
                    txtPreservedTime2.getText(),
                    0,
                    Double.parseDouble(txtUnitPrice2.getText()),
                    Double.parseDouble(txtUniteSalePrice2.getText())
            );
            if (new ItemManager().updateItem(item)) {
                new Alert(Alert.AlertType.CONFIRMATION, "Updated...").show();
                clearFields(validationList2);
                viewAllItem();
            } else {
                new Alert(Alert.AlertType.WARNING, "Try again...").show();
            }
        }
    }

}