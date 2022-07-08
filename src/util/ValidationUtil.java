package util;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class ValidationUtil {
    public static void validate(TextField textField, LinkedHashMap<TextField,Pattern> list){
        Pattern pattern=list.get(textField);
        if(!pattern.matcher(textField.getText()).matches()) {
            showVerification(textField,(isFieldEmpty(textField)? 0:-1));
        }else{
            showVerification(textField,1);
        }
    }

    private static void showVerification(TextField textField, int status){
        Label tag=getTag(textField);
        switch(status){
            case -1:
                textField.setStyle("-fx-border-color: #DB0F0F");
                tag.setStyle("-fx-text-fill: #DB0F0F");
                break;
            case 0:
                textField.setStyle("-fx-border-color: rgba(76, 73, 73, 0.83)");
                tag.setStyle("-fx-text-fill: rgba(76, 73, 73, 0.83)");
                break;
            case 1:
                textField.setStyle("-fx-border-color: #028f02");
                tag.setStyle("-fx-text-fill: #028f02");
                break;
        }
    }

    private static Label getTag(TextField textField) {
        AnchorPane parent=(AnchorPane)textField.getParent();
        return (Label)parent.getChildren().get(2);
    }

    private static boolean isFieldEmpty(TextField textField) {
        return textField.getText().equals("");
    }
}
