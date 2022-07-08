package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import manager.LoginManager;
import model.LoginDetail;

import java.util.ArrayList;

public class ViewLoginDetailsView {

    public TableView<LoginDetail> tblLoginDetailList;
    public TableColumn<LoginDetail,String> colUserId;
    public TableColumn<LoginDetail,String> colLoginDate;
    public TableColumn<LoginDetail,String> colLoginTime;
    public TableColumn<LoginDetail,String> colLogoutDate;
    public TableColumn<LoginDetail,String> colLogoutTime;

    public JFXTextField detailSearchBar;

    ObservableList<LoginDetail> obList= FXCollections.observableArrayList();

    public void initialize(){
        viewAllDetails();

        colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colLoginDate.setCellValueFactory(new PropertyValueFactory<>("loginDate"));
        colLoginTime.setCellValueFactory(new PropertyValueFactory<>("loginTime"));
        colLogoutDate.setCellValueFactory(new PropertyValueFactory<>("logoutDate"));
        colLogoutTime.setCellValueFactory(new PropertyValueFactory<>("logoutTime"));
    }

    private void viewAllDetails() {
        obList.clear();
        ArrayList<LoginDetail> detailList= LoginManager.getAllDetails();
        obList.addAll(detailList);
        tblLoginDetailList.setItems(obList);
    }
}
