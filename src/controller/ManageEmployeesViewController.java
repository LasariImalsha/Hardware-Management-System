package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import manager.EmployeeManager;
import model.Employee;
import model.UserDetail;
import util.CrudUtil;
import util.ValidationUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;


public class ManageEmployeesViewController {

    public TableView<Employee> tblEmployeeList;
    public TableColumn<Employee, String> colEmployeeId;
    public TableColumn<Employee, String> colName;
    public TableColumn<Employee, String> colUserId;
    public TableColumn<Employee, String> colAddress;
    public TableColumn<Employee, String> colContact;
    public TableColumn<Employee, String> colOccupation;
    public TableColumn<Employee, String> colSalary;

    public JFXButton btnAdd;
    public JFXButton btnUpdate;
    public JFXButton btnDelete;

    public JFXTextField employeeSearchBar;
    public TextField txtEmpId;
    public TextField txtEmpName;
    public TextField txtEmpAddress;
    public TextField txtEmpContact;
    public TextField txtOccupation;
    public TextField txtSalary;
    public TextField txtUserId;
    public TextField txtUserName;
    public TextField txtPassword;

    LinkedHashMap<TextField, Pattern> validationList=new LinkedHashMap<>();

    Pattern empIdPattern=Pattern.compile("^(E-)[0-9]{3}$");
    Pattern userIdPattern=Pattern.compile("^(As-|Ot-)[0-9]{3}$");
    Pattern namePattern=Pattern.compile("^[A-Z][a-z]*([ ][A-Z][a-z]*)*$");
    Pattern addressPattern=Pattern.compile("^[A-Z][A-z0-9,/ ]*$");
    Pattern contactPattern=Pattern.compile("^(07)[01245678]([-][0-9]{7})$");
    Pattern occupationPattern=Pattern.compile("^(Assistant|Other)$");
    Pattern salaryPattern=Pattern.compile("^[1-9][0-9]*([.][0-9]{2})$");
    Pattern loginPattern=Pattern.compile("^[A-z0-9!@#$&]{6,10}$");

    ObservableList<Employee> obList= FXCollections.observableArrayList();

    public void initialize() {
            storeValidations();
            setEmployeeId();
            viewAllEmployees();

            colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
            colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
            colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
            colOccupation.setCellValueFactory(new PropertyValueFactory<>("occupation"));
            colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));

            tblEmployeeList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                        setEmployeeData(newValue);
                    }
            );
            listenFieldChange(validationList);

            listenDetailChange(txtOccupation);
            listenDetailChange(txtEmpId);

        txtEmpId.setOnKeyPressed( event -> {
            if( event.getCode() == KeyCode.ENTER ) {
                setEmployeeData(new EmployeeManager().getEmployee(txtEmpId.getText()));
            }
        } );

    }

    private void listenFieldChange(LinkedHashMap<TextField,Pattern> list) {
        for(TextField key: list.keySet()) {
            key.textProperty().addListener((observable,oldValue,newValue)->{
                ValidationUtil.validate(key,list);
            });
        }
    }

    private void listenDetailChange(TextField field) {
        field.textProperty().addListener((observable,oldValue,newValue)->{
            Employee employee=new EmployeeManager().getEmployee(txtEmpId.getText());
            String role=txtOccupation.getText();
            if(field.getParent().getStyle().equals("-fx-border-color: #028f02")) {
                if(employee == null) {
                    if(field.equals(txtOccupation)) {
                        String userId = new EmployeeManager().getUserId(role);
                        txtUserId.setText(userId);
                        if(role.equals("Assistant")) {
                            txtSalary.setText("45000.00");
                        }else if (role.equals("Other")) {
                            txtSalary.setText("70000.00");
                        }
                    }else if (field.equals(txtEmpId)) {
                        String empId=field.getText();
                        String username=new EmployeeManager().getUsername();
                        txtOccupation.clear();
                        txtPassword.clear();
                        txtUserName.setText(username);
                        txtEmpId.setText(empId);
                    }
                }else{
                    if(field.equals(txtOccupation)) {
                        if(field.getText().equals(employee.getOccupation())) {
                            txtUserId.setText(employee.getUserId());
                            txtSalary.setText(String.format("%.2f", employee.getSalary()));
                        }else{
                            String userId = new EmployeeManager().getUserId(role);
                            txtUserId.setText(userId);
                            if(role.equals("Assistant")) {
                                txtSalary.setText("45000.00");
                            }else if (role.equals("Other")) {
                                txtSalary.setText("70000.00");
                            }
                        }
                    }else if(field.equals(txtEmpId)) {
                        setEmployeeData(employee);
                    }
                }
            }
        });
    }

    private void setEmployeeData(Employee e) {
        if(e!=null){
            txtEmpId.setText(e.getEmployeeId());
            txtEmpName.setText(e.getName());
            txtEmpAddress.setText(e.getAddress());
            txtEmpContact.setText(e.getContact());
            txtOccupation.setText(e.getOccupation());
            txtSalary.setText(String.format("%.2f",e.getSalary()));
            txtUserId.setText(e.getUserId());
            txtUserName.setText(e.getUserDetail().getUsername());
            txtPassword.setText(e.getUserDetail().getPassword());
        }
    }

    private boolean isFormNotFilled(LinkedHashMap<TextField,Pattern> validationList) {
        for(TextField key: validationList.keySet()){
            if(!key.getStyle().equals("-fx-border-color: #028f02")){
                return true;
            }
        }
        return false;
    }

    private void storeValidations() {
        validationList.put(txtEmpId,empIdPattern);
        validationList.put(txtEmpName,namePattern);
        validationList.put(txtEmpAddress,addressPattern);
        validationList.put(txtEmpContact,contactPattern);
        validationList.put(txtOccupation,occupationPattern);
        validationList.put(txtSalary,salaryPattern);
        validationList.put(txtUserId,userIdPattern);
        validationList.put(txtUserName,loginPattern);
        validationList.put(txtPassword,loginPattern);
    }

    private void viewAllEmployees(){
        obList.clear();
        ArrayList<Employee> employees=new EmployeeManager().getAllEmployees();
        obList.addAll(employees);
        tblEmployeeList.setItems(obList);
    }

    private void setEmployeeId() {
        txtEmpId.setText(new EmployeeManager().getEmployeeId());
        String username=new EmployeeManager().getUsername();
        txtUserName.setText(username);
    }

    private void clearFields(LinkedHashMap<TextField,Pattern> list) {
        for(TextField key: list.keySet()){
            key.clear();
        }
    }


    private boolean isExists(String empId) {
        ArrayList<Employee> employees=new EmployeeManager().getAllEmployees();
        for(Employee e:employees){
            if(e.getEmployeeId().equals(empId)){
                return true;
            }
        }
        return false;
    }

    public void addEmployeeOnAction(ActionEvent actionEvent) {
        if (isFormNotFilled(validationList)) {
            new Alert(Alert.AlertType.WARNING, "Fields are not filled properly...").show();
        } else if (isExists(txtEmpId.getText())) {
            new Alert(Alert.AlertType.WARNING, "Employee already exists...").show();
        } else {
            UserDetail userDetail = new UserDetail(
                    txtUserId.getText(),
                    txtUserName.getText(),
                    txtPassword.getText(),
                    txtOccupation.getText()
            );
            Employee employee = new Employee(
                    txtEmpId.getText(),
                    txtUserId.getText(),
                    txtEmpName.getText(),
                    txtEmpAddress.getText(),
                    txtEmpContact.getText(),
                    txtOccupation.getText(),
                    Double.parseDouble(txtSalary.getText()),
                    userDetail
            );
            if (new EmployeeManager().addEmployee(employee)) {
                new Alert(Alert.AlertType.CONFIRMATION, "Saved...").show();
                clearFields(validationList);
                viewAllEmployees();
                setEmployeeId();
            } else {
                new Alert(Alert.AlertType.WARNING, "Try again...").show();
            }
        }
    }

    public void updateEmployeeOnAction(ActionEvent actionEvent) {
        if(isFormNotFilled(validationList)){
            new Alert(Alert.AlertType.WARNING, "Fields are not filled properly...").show();
        }else if(!isExists(txtEmpId.getText())){
            new Alert(Alert.AlertType.WARNING, "Employee doesn't exist...").show();
        }else{
            UserDetail userDetail=new UserDetail(
                    txtUserId.getText(),
                    txtUserName.getText(),
                    txtPassword.getText(),
                    txtOccupation.getText()
            );
            Employee employee=new Employee(
                    txtEmpId.getText(),
                    txtUserId.getText(),
                    txtEmpName.getText(),
                    txtEmpAddress.getText(),
                    txtEmpContact.getText(),
                    txtOccupation.getText(),
                    Double.parseDouble(txtSalary.getText()),
                    userDetail
            );
            if(new EmployeeManager().updateEmployee(employee)){
                new Alert(Alert.AlertType.CONFIRMATION,"Updated...").show();
                clearFields(validationList);
                viewAllEmployees();
                setEmployeeId();
            }else{
                new Alert(Alert.AlertType.WARNING,"Try again...").show();
            }
        }
    }

    public void deleteEmployeeOnAction(ActionEvent actionEvent) {
        if(isFormNotFilled(validationList)){
            new Alert(Alert.AlertType.WARNING, "Fields are not filled properly...").show();
        }else if(!isExists(txtEmpId.getText())){
            new Alert(Alert.AlertType.WARNING, "Employee doesn't exist...").show();
        }else{
            Employee employee=new EmployeeManager().getEmployee(txtEmpId.getText());
            if(new EmployeeManager().deleteEmployee(employee)){
                new Alert(Alert.AlertType.CONFIRMATION,"Saved...").show();
                clearFields(validationList);
                viewAllEmployees();
                setEmployeeId();
            }else{
                new Alert(Alert.AlertType.WARNING,"Try again...").show();
            }
        }
    }
}
