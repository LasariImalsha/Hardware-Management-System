package controller;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.binding.MapExpression;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import manager.CustomerManager;
import manager.EmployeeManager;
import model.Customer;
import util.CrudUtil;
import util.ValidationUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class ManageCustomersViewController {
    public TableView<Customer> tblCustomerList;
    public TableColumn<Customer, String> colCustomerId;
    public TableColumn<Customer, String> colName;
    public TableColumn<Customer, String> colAddress;
    public TableColumn<Customer, String> colContact;

    public JFXButton btnAdd;
    public JFXButton btnDelete;
    public JFXButton btnUpdate;

    public JFXTextField customerSearchBar;
    public TextField txtCustId1;
    public TextField txtCustName1;
    public TextField txtCustAddress1;
    public TextField txtCustContact1;
    public TextField txtCustId2;
    public TextField txtCustName2;
    public TextField txtCustAddress2;
    public TextField txtCustContact2;

    LinkedHashMap<TextField, Pattern> validationList1 = new LinkedHashMap<>();
    LinkedHashMap<TextField, Pattern> validationList2 = new LinkedHashMap<>();

    Pattern idPattern = Pattern.compile("^(C-)[0-9]{5}$");
    Pattern namePattern = Pattern.compile("^[A-Z][a-z]*([ ][A-Z][a-z]*)*$");
    Pattern addressPattern = Pattern.compile("^[A-Z][A-z0-9,/ ]*$");
    Pattern contactPattern = Pattern.compile("^(07)[01245678]([-][0-9]{7})$");

    ObservableList<Customer> obList = FXCollections.observableArrayList();

    public void initialize() {
        storeValidations();
        setCustomerId();
        viewAllCustomers();

        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));

        tblCustomerList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    setCustomerData(newValue);
                }
        );

        listenFieldChange(validationList1);
        listenFieldChange(validationList2);

        txtCustId2.setOnKeyPressed( event -> {
            if (event.getCode() == KeyCode.ENTER) {
                setCustomerData(new CustomerManager().getCustomer(txtCustId2.getText()));
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

    private boolean isFormNotFilled(LinkedHashMap<TextField, Pattern> validationList) {
        for (TextField key : validationList.keySet()) {
            if (!key.getStyle().equals("-fx-border-color: #028f02")) {
                return true;
            }
        }
        return false;
    }

    private void storeValidations() {
        validationList1.put(txtCustId1, idPattern);
        validationList1.put(txtCustName1, namePattern);
        validationList1.put(txtCustAddress1, addressPattern);
        validationList1.put(txtCustContact1, contactPattern);
        validationList2.put(txtCustId2, idPattern);
        validationList2.put(txtCustName2, namePattern);
        validationList2.put(txtCustAddress2, addressPattern);
        validationList2.put(txtCustContact2, contactPattern);
    }

    private void setCustomerData(Customer c) {
        if (c != null) {
            txtCustId2.setText(c.getId());
            txtCustName2.setText(c.getName());
            txtCustAddress2.setText(c.getAddress());
            txtCustContact2.setText(c.getContact());
        }
    }


    private void searchCustomers(String text) {
        obList.clear();
        ArrayList<Customer> customers = new CustomerManager().searchCustomers(text);
        obList.addAll(customers);
        tblCustomerList.setItems(obList);
    }

    private void viewAllCustomers() {
        obList.clear();
        ArrayList<Customer> customers = new CustomerManager().getAllCustomers();
        obList.addAll(customers);
        tblCustomerList.setItems(obList);
    }

    private void setCustomerId() {
        txtCustId1.setText(new CustomerManager().getCustomerId());
    }

    private void clearFields(LinkedHashMap<TextField,Pattern> fieldList) {
        for (TextField key : fieldList.keySet()) {
            key.clear();
        }
    }
    public void addCustomerOnAction(ActionEvent actionEvent) {

        if (isFormNotFilled(validationList1)) {
            new Alert(Alert.AlertType.WARNING, "Fields are not filled properly...").show();
        } else {
            Customer customer = new Customer(
                    txtCustId1.getText(),
                    txtCustName1.getText(),
                    txtCustAddress1.getText(),
                    txtCustContact1.getText()
            );
            if (new CustomerManager().addCustomer(customer)) {
                new Alert(Alert.AlertType.CONFIRMATION, "Saved...").show();
                clearFields(validationList1);
                viewAllCustomers();
                setCustomerId();
            } else {
                new Alert(Alert.AlertType.WARNING, "Try again...").show();
            }
        }
    }

    public void txtSearchOnAction(ActionEvent actionEvent) {
        search();
    }

    private void search() {
        try {
            ResultSet result = CrudUtil.execute("SELECT * FROM Customer WHERE id=?", txtCustId2.getText());
            if (result.next()) {
                txtCustName2.setText(result.getString(2));
                txtCustAddress2.setText(result.getString(3));
                txtCustContact2.setText(result.getString(4));
            } else {
                new Alert(Alert.AlertType.WARNING, "Empty Result").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void deleteCustomerOnAction(ActionEvent actionEvent) {
        if (isFormNotFilled(validationList2)) {
            new Alert(Alert.AlertType.WARNING, "Fields are not filled properly...").show();
        } else {
            String customerId = txtCustId2.getText();
            if (new CustomerManager().deleteCustomer(customerId)) {
                new Alert(Alert.AlertType.CONFIRMATION, "Deleted...").show();
                clearFields(validationList2);
                viewAllCustomers();
            } else {
                new Alert(Alert.AlertType.WARNING, "Try again...").show();
            }
        }
    }

    public void updateCustomerOnAction(ActionEvent actionEvent) {
        if (isFormNotFilled(validationList2)) {
            new Alert(Alert.AlertType.WARNING, "Fields are not filled properly...").show();
        } else {
            Customer customer = new Customer(
                    txtCustId2.getText(),
                    txtCustName2.getText(),
                    txtCustAddress2.getText(),
                    txtCustContact2.getText()
            );
            if (new CustomerManager().updateCustomer(customer)) {
                new Alert(Alert.AlertType.CONFIRMATION, "Updated...").show();
                clearFields(validationList2);
                viewAllCustomers();
            } else {
                new Alert(Alert.AlertType.WARNING, "Try again...").show();
            }
        }

    }

}

