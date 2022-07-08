package manager;

import database.DatabaseConnection;
import model.LoginDetail;
import model.UserDetail;
import util.DateTimeUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoginManager {

    public static void login(String userId) {
        try {
            PreparedStatement statement= DatabaseConnection.getInstance().getConnection().prepareStatement("INSERT INTO `Login Detail` (userId,loginDate,loginTime) VALUES (?,?,?)");
            statement.setObject(1,userId);
            statement.setObject(2, DateTimeUtil.currentDate(1));
            statement.setObject(3, DateTimeUtil.currentTime(1));
            statement.executeUpdate();
        }catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void logout(String userId) {
        try {
            LoginDetail detail=getLastDetail(userId);
            if(detail!=null){
                PreparedStatement statement=DatabaseConnection.getInstance().getConnection().prepareStatement("UPDATE `Login Detail` SET logoutDate=?,logoutTime=? WHERE userId=? AND loginDate=? AND loginTime=?");
                statement.setObject(3, userId);
                statement.setObject(4, detail.getLoginDate());
                statement.setObject(5, detail.getLoginTime());
                statement.setObject(1, DateTimeUtil.currentDate(1));
                statement.setObject(2, DateTimeUtil.currentTime(1));
                statement.executeUpdate();
            }
        }catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    private static LoginDetail getLastDetail(String userId) {
        try {
            ArrayList<LoginDetail> detailList=new ArrayList<>();
            PreparedStatement statement=DatabaseConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `Login Detail` WHERE userId=?");
            statement.setObject(1,userId);
            ResultSet resultSet=statement.executeQuery();
            while(resultSet.next()) {
                detailList.add(new LoginDetail(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5)
                ));
            }
            return detailList.get(detailList.size()-1);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static ArrayList<LoginDetail> getAllDetails() {
        try {
            PreparedStatement statement=DatabaseConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `Login Detail`");
            ResultSet resultSet=statement.executeQuery();
            ArrayList<LoginDetail> detailList=new ArrayList<>();
            while(resultSet.next()){
                detailList.add(new LoginDetail(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getString(4),
                                resultSet.getString(5)
                        )
                );
            }
            return detailList;
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Object verifyLogin(String username, String password) {
        try {
            PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `User Detail` WHERE username=? AND CAST(AES_DECRYPT(password,'ramindu') AS CHAR(100))=?");
            statement.setObject(1, username);
            statement.setObject(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new UserDetail(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4)
                );
            } else {
                return false;
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
}
