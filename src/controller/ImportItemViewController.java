package controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import manager.ItemManager;
import manager.StockManager;
import manager.SupplierManager;
import model.Item;
import model.Stock;
import model.StockDetail;
import model.Supplier;
import model.tableModel.ShipmentTM;
import util.DateTimeUtil;
import util.ValidationUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class ImportItemViewController {

    public TableView<ShipmentTM> tblShipment;
    public TableColumn<ShipmentTM,String> colItemCode;
    public TableColumn<ShipmentTM,String> colName;
    public TableColumn<ShipmentTM,String> colUnitPrice;
    public TableColumn<ShipmentTM,String> colQuantity;
    public TableColumn<ShipmentTM,String> colPrice;

    public JFXButton btnPlaceShipment;
    public JFXButton btnClearRecord;
    public JFXButton btnAddToCart;

    public Label lblTotal;
    public Label lblBatchNo;

    public ComboBox<String> cmbSupplier;
    public ComboBox<String> cmbItem;

    public TextField txtSupId;
    public TextField txtSupName;
    public TextField txtSupAddress;
    public TextField txtSupContact;
    public TextField txtItemCode;
    public TextField txtItemName;
    public TextField txtItemDescription;
    public TextField txtItemPreservedTime;
    public TextField txtItemUnitPrice;
    public TextField txtQuantity;

    LinkedHashMap<TextField, Pattern> validationList=new LinkedHashMap<>();

    Pattern idPattern=Pattern.compile("^(S-)[0-9]{3}$");
    Pattern namePattern=Pattern.compile("^[A-Z][a-z]*([ ][A-Z][a-z]*)*$");
    Pattern addressPattern=Pattern.compile("^[A-Z][A-z0-9,/ ]*$");
    Pattern contactPattern=Pattern.compile("^(07)[01245678]([-][0-9]{7})$");
    Pattern codePattern=Pattern.compile("^(I-)[0-9]{5}$");
    Pattern descPattern=Pattern.compile("^[A-Z][A-z0-9, ]*$");
    Pattern timePeriodPattern=Pattern.compile("^[0-9]{1,3}[ ](weeks|months|years)$");
    Pattern pricePattern=Pattern.compile("^[1-9][0-9]{0,6}([.][0-9]{2})?$");
    Pattern quantityPattern=Pattern.compile("^[1-9][0-9]*$");

    ObservableList<ShipmentTM> obList= FXCollections.observableArrayList();
    private ShipmentTM selectedRecord;

    public void initialize(){
        storeValidations();
        setBatchNumber();
        calculateTotal();

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        cmbSupplier.getItems().addAll(new SupplierManager().getAllSupplierIds());
        cmbItem.getItems().addAll(new ItemManager().getAllItemCodes());

        cmbSupplier.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setSupplierData(newValue);
        });
        cmbItem.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setItemData(newValue);
        });

        tblShipment.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedRecord=newValue;
        });

        listenFieldChange(validationList);

    }

    private void storeValidations() {
        validationList.put(txtSupId,idPattern);
        validationList.put(txtSupName,namePattern);
        validationList.put(txtSupAddress,addressPattern);
        validationList.put(txtSupContact,contactPattern);
        validationList.put(txtItemCode,codePattern);
        validationList.put(txtItemName,namePattern);
        validationList.put(txtItemDescription,descPattern);
        validationList.put(txtItemPreservedTime,timePeriodPattern);
        validationList.put(txtItemUnitPrice,pricePattern);
        validationList.put(txtQuantity,quantityPattern);
    }

    private void listenFieldChange(LinkedHashMap<TextField,Pattern> list) {
        for(TextField key: list.keySet()) {
            key.textProperty().addListener((observable,oldValue,newValue)->{
                ValidationUtil.validate(key,list);
            });
        }
    }

    private void calculateTotal() {
        double total=0;
        for(ShipmentTM record:obList){
            total+=record.getPrice();
        }
        lblTotal.setText("Rs. "+String.format("%.2f",total));
    }

    private void setSupplierData(String supplierId) {
        Supplier supplier=new SupplierManager().getSupplier(supplierId);
        txtSupId.setText(supplier.getId());
        txtSupName.setText(supplier.getName());
        txtSupAddress.setText(supplier.getAddress());
        txtSupContact.setText(supplier.getContact());
    }
    private void setItemData(String medicineCode) {
        Item item=new ItemManager().getItem(medicineCode);
        txtItemCode.setText(item.getCode());
        txtItemName.setText(item.getName());
        txtItemDescription.setText(item.getDescription());
        txtItemPreservedTime.setText(item.getPreservedTime());
        txtItemUnitPrice.setText(String.format("%.2f",item.getUnitPrice()));
    }

    private void setBatchNumber() {
        lblBatchNo.setText(new StockManager().getBatchNumber());
    }

    private boolean isFormNotFilled(LinkedHashMap<TextField,Pattern> validationList) {
        for(TextField key: validationList.keySet()){
            if(!key.getStyle().equals("-fx-border-color: #028f02")){
                return true;
            }
        }
        return false;
    }

    public void clearRecordOnAction(ActionEvent actionEvent) {
        if(obList.isEmpty()){
            new Alert(Alert.AlertType.WARNING, "Shipment is Empty.").show();
        }else if(selectedRecord==null){
            new Alert(Alert.AlertType.WARNING, "Please select a row.").show();
        }else{
            for(int i=0; i<obList.size(); i++) {
                if(selectedRecord.getItemCode().equals(obList.get(i).getItemCode())) {
                    obList.remove(i);
                }
            }
            tblShipment.setItems(obList);
            calculateTotal();
        }
    }

    public void addToShipmentOnAction(ActionEvent actionEvent) {
        if(isFormNotFilled(validationList)){
            new Alert(Alert.AlertType.WARNING, "Fields are not filled properly...").show();
        }else {
            String expiryDate= DateTimeUtil.dateAheadCurrentDate(txtItemPreservedTime.getText());
            double price=Double.parseDouble(txtQuantity.getText()) * Double.parseDouble(txtItemUnitPrice.getText());
            ShipmentTM shipment=new ShipmentTM(
                    txtItemCode.getText(),
                    txtItemName.getText(),
                    Double.parseDouble(txtItemUnitPrice.getText()),
                    Integer.parseInt(txtQuantity.getText()),
                    price
            );
            if(isExists(shipment)){
                int index=0;
                for(int i=0; i<obList.size(); i++) {
                    if(shipment.getItemCode().equals(obList.get(i).getItemCode())) {
                        index=i;
                    }
                }
                ShipmentTM temp=obList.get(index);
                ShipmentTM newShipment=new ShipmentTM(
                        temp.getItemCode(),
                        temp.getName(),
                        temp.getUnitPrice(),
                        temp.getQuantity()+shipment.getQuantity(),
                        temp.getPrice()+shipment.getPrice()
                );
                obList.remove(index);
                obList.add(newShipment);
            }else {
                obList.add(shipment);
            }
            tblShipment.setItems(obList);
            calculateTotal();
            txtQuantity.clear();
        }
    }

    private boolean isExists(ShipmentTM shipment) {
        for(ShipmentTM shipmentTM : obList){
            if (shipment.getItemCode().equals(shipmentTM.getItemCode())) {
                return true;
            }
        }
        return false;
    }

    private void clearAllFields() {
        for(TextField field : validationList.keySet()){
            field.clear();
        }
    }

    public void placeShipmentOnAction(ActionEvent actionEvent) {
        ArrayList<StockDetail> detailList=new ArrayList<>();
        for(ShipmentTM temp:obList){
            detailList.add(new StockDetail(
                            lblBatchNo.getText(),
                            temp.getItemCode(),
                            temp.getUnitPrice(),
                            temp.getQuantity(),
                            temp.getPrice()
                    )
            );
        }
        Stock stock = new Stock(
                lblBatchNo.getText(),
                txtSupId.getText(),
                DateTimeUtil.currentDate(1),
                DateTimeUtil.currentTime(1),
                Double.parseDouble(lblTotal.getText().split(" ")[1]),
                detailList
        );
        if (new StockManager().placeShipment(stock)) {
            new Alert(Alert.AlertType.CONFIRMATION, "Shipment placed...").show();
            setBatchNumber();
            clearAllFields();
            obList.clear();
            tblShipment.setItems(obList);
        } else {
            new Alert(Alert.AlertType.WARNING, "Try Again...").show();
        }
    }

    }

