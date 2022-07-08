package controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import manager.CustomerManager;
import manager.ItemManager;
import manager.OrderManager;
import model.Customer;
import model.Item;
import model.Order;
import model.OrderDetail;
import model.tableModel.CartTM;
import util.DateTimeUtil;
import util.ValidationUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class PlaceOrderViewController {
    public TableView<CartTM> tblCart;
    public TableColumn<CartTM, String> colItemCode;
    public TableColumn<CartTM, String> colName;
    public TableColumn<CartTM, String> colUnitPrice;
    public TableColumn<CartTM, String> colQuantity;
    public TableColumn<CartTM, String> colPrice;

    public JFXButton btnClearRecord;
    public JFXButton btnAddToCart;
    public JFXButton btnPlaceOrder;

    public Label lblTotal;
    public Label lblOrderId;

    public ComboBox<String> cmbCustomer;
    public ComboBox<String> cmbItem;

    public TextField txtCustId;
    public TextField txtCustName;
    public TextField txtCustAddress;
    public TextField txtCustContact;
    public TextField txtItemCode;
    public TextField txtIteamName;
    public TextField txtDescription;
    public TextField txtQtyOnHand;
    public TextField txtItemUnitPrice;
    public TextField txtQuantity;


    LinkedHashMap<TextField, Pattern> validationList = new LinkedHashMap<>();
    ObservableList<CartTM> obList = FXCollections.observableArrayList();
    private CartTM selectedRecord;

    Pattern idPattern = Pattern.compile("^(C-)[0-9]{5}$");
    Pattern namePattern = Pattern.compile("^[A-Z][a-z]*([ ][A-Z][a-z]*)*$");
    Pattern addressPattern = Pattern.compile("^[A-Z][A-z0-9,/ ]*$");
    Pattern contactPattern = Pattern.compile("^(07)[01245678]([-][0-9]{7})$");
    Pattern codePattern = Pattern.compile("^(I-)[0-9]{5}$");
    Pattern descPattern = Pattern.compile("^[A-Z][A-z0-9, ]*$");
    Pattern pricePattern = Pattern.compile("^[1-9][0-9]{0,6}([.][0-9]{2})?$");
    Pattern quantityPattern = Pattern.compile("^[1-9][0-9]*$");

    public void initialize() {
        setOrderId();
        calculateTotal();
        storeValidations();

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        cmbCustomer.getItems().addAll(new CustomerManager().getAllCustomerIds());
        cmbItem.getItems().addAll(new ItemManager().getAllItemCodes());

        cmbCustomer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setCustomerData(newValue);
        });
        cmbItem.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setItemData(newValue);
        });

        tblCart.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedRecord = newValue;
        });

        listenFieldChange(validationList);
        listenDetailChange(txtQuantity);

    }

    private void listenFieldChange(LinkedHashMap<TextField,Pattern> list) {
        for(TextField key: list.keySet()) {
            key.textProperty().addListener((observable,oldValue,newValue)->{
                ValidationUtil.validate(key,list);
            });
        }
    }

    private void storeValidations() {
        validationList.put(txtCustId,idPattern);
        validationList.put(txtCustName,namePattern);
        validationList.put(txtCustAddress,addressPattern);
        validationList.put(txtCustContact,contactPattern);
        validationList.put(txtItemCode,codePattern);
        validationList.put(txtIteamName,namePattern);
        validationList.put(txtDescription,descPattern);
        validationList.put(txtQtyOnHand,quantityPattern);
        validationList.put(txtItemUnitPrice,pricePattern);
        validationList.put(txtQuantity,quantityPattern);
    }
    private void listenDetailChange(TextField field) {
        field.textProperty().addListener((observable,oldValue,newValue)->{
            Item item=new ItemManager().getItem(txtItemCode.getText());
            if(item==null) {
                new Alert(Alert.AlertType.WARNING, "No item selected...").show();
            }else if(!field.getText().equals("") && Integer.parseInt(txtQtyOnHand.getText())<Integer.parseInt(field.getText())){
                field.getParent().setStyle("-fx-border-color: #DB0F0F");
                AnchorPane parent=(AnchorPane) field.getParent();
                Label tag=(Label)parent.getChildren().get(2);
                tag.setStyle("-fx-text-fill: #DB0F0F");
                new Alert(Alert.AlertType.WARNING, "Insufficient quantity...").show();
            }
        });
    }

    private void calculateTotal() {
        double total=0;
        for(CartTM record:obList){
            total+=record.getPrice();
        }
        lblTotal.setText("Rs. "+String.format("%.2f",total));
    }

    private void setOrderId() {
        lblOrderId.setText(new OrderManager().getOrderId());
    }

    private void setCustomerData(String customerId) {
        Customer customer=new CustomerManager().getCustomer(customerId);
        txtCustId.setText(customer.getId());
        txtCustName.setText(customer.getName());
        txtCustAddress.setText(customer.getAddress());
        txtCustContact.setText(customer.getContact());
    }

    private void setItemData(String itemCode) {
        Item item=new ItemManager().getItem(itemCode);
        txtItemCode.setText(item.getCode());
        txtIteamName.setText(item.getName());
        txtDescription.setText(item.getDescription());
        setQuantityOnHand(item);
        txtItemUnitPrice.setText(String.format("%.2f",item.getUnitSalePrice()));
        txtQuantity.clear();
    }

    private void setQuantityOnHand(Item item) {
        int newQty=item.getQtyOnHand();
        for(CartTM tm: obList){
            if(item.getCode().equals(tm.getItemCode())){
                newQty-=tm.getQuantity();
            }
        }
        txtQtyOnHand.setText(Integer.toString(newQty));
    }

    private boolean isExists(CartTM cart) {
        for(CartTM cartTM : obList){
            if (cart.getItemCode().equals(cartTM.getItemCode())) {
                return true;
            }
        }
        return false;
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
            new Alert(Alert.AlertType.WARNING, "Cart is Empty.").show();
        }else if(selectedRecord==null){
            new Alert(Alert.AlertType.WARNING, "Please select a row.").show();
        }else{
            Item medicine=new ItemManager().getItem(selectedRecord.getItemCode());
            for(int i=0; i<obList.size(); i++) {
                if(selectedRecord.getItemCode().equals(obList.get(i).getItemCode())) {
                    obList.remove(i);
                }
            }
            tblCart.setItems(obList);
            if(txtItemCode.getText().equals(medicine.getCode())){
                setQuantityOnHand(medicine);
            }
            calculateTotal();
        }

    }

    public void addToShipmentOnAction(ActionEvent actionEvent) {
        double price=Double.parseDouble(txtQuantity.getText()) * Double.parseDouble(txtItemUnitPrice.getText());
        CartTM cart=new CartTM(
                txtItemCode.getText(),
                txtIteamName.getText(),
                Double.parseDouble(txtItemUnitPrice.getText()),
                Integer.parseInt(txtQuantity.getText()),
                price
        );
        if(isExists(cart)){
            int index=0;
            for(int i=0; i<obList.size(); i++) {
                if(cart.getItemCode().equals(obList.get(i).getItemCode())) {
                    index=i;
                }
            }
            CartTM temp=obList.get(index);
            CartTM newCart=new CartTM(
                    temp.getItemCode(),
                    temp.getName(),
                    temp.getUnitPrice(),
                    temp.getQuantity()+cart.getQuantity(),
                    temp.getPrice()+cart.getPrice()
            );
            obList.remove(index);
            obList.add(newCart);
        }else {
            obList.add(cart);
        }
        tblCart.setItems(obList);
        calculateTotal();
        Item medicine=new ItemManager().getItem(txtItemCode.getText());
        setQuantityOnHand(medicine);
        txtQuantity.clear();
    }

    public void placeOrderOnAction(ActionEvent actionEvent) {
        ArrayList<OrderDetail> detailList=new ArrayList<>();
        for(CartTM temp:obList){
            detailList.add(new OrderDetail(
                            lblOrderId.getText(),
                            temp.getItemCode(),
                            temp.getUnitPrice(),
                            temp.getQuantity(),
                            temp.getPrice()
                    )
            );
        }
        Order order=new Order(
                lblOrderId.getText(),
                txtCustId.getText(),
                DateTimeUtil.currentDate(1),
                DateTimeUtil.currentTime(1),
                Double.parseDouble(lblTotal.getText().split(" ")[1]),
                detailList
        );
        if (new OrderManager().placeOrder(order)){
            new Alert(Alert.AlertType.CONFIRMATION, "Order placed...").show();
            setOrderId();
            obList.clear();
            tblCart.setItems(obList);
        }else{
            new Alert(Alert.AlertType.WARNING, "Try Again...").show();
        }
    }
}

