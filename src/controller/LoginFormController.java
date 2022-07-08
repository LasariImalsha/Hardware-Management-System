package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class LoginFormController {
    public JFXTextField txtUserName;
    public JFXPasswordField pwdPassword;
    public AnchorPane loginView;
    public AnchorPane loginContext;


    public void loadDashboardOnAction(ActionEvent actionEvent) throws IOException {
        String username = txtUserName.getText();
        String password = pwdPassword.getText();

        String pw = pwdPassword.getText();
        switch (pw) {
            case "user1":
                loginView.getChildren().add(FXMLLoader.load(getClass().getResource("../view/AdminDashboard.fxml")));
                break;
            case "user2":
                loginView.getChildren().add(FXMLLoader.load(getClass().getResource("../view/Assistant1Dashboard.fxml")));
                break;
            case "user3":
                loginView.getChildren().add(FXMLLoader.load(getClass().getResource("../view/Assistant2Dashboard.fxml")));
                break;
        }
    }
}
