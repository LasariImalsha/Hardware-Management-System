package manager;

import database.DatabaseConnection;
import model.Employee;
import model.Item;
import model.PriceDetail;
import model.UserDetail;
import model.tableModel.IncomeTM;
import util.DateTimeUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ItemManager {

    public boolean addItem(Item item) {
        Connection connection=null;
        try{
            connection=DatabaseConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            if(isItemAdded(item,connection) && isPriceDetailAdded(item,connection)){
                connection.commit();
                return true;
            }else{
                connection.rollback();
                return false;
            }
        }catch(SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }finally{
            try{
                assert connection != null;
                connection.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return false;
    }

    public boolean updateItem(Item item) {
        Connection connection=null;
        try{
            connection=DatabaseConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            if(isItemUpdated(item,connection) && isPriceDetailAdded(item,connection)){
                connection.commit();
                return true;
            }else{
                connection.rollback();
                return false;
            }
        }catch(SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }finally{
            try{
                assert connection != null;
                connection.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return false;
    }

    public boolean deleteItem(String itemCode) {
        try {
            PreparedStatement statement=DatabaseConnection.getInstance().getConnection().prepareStatement("DELETE FROM item WHERE itemCode=?");
            statement.setObject(1,itemCode);
            return statement.executeUpdate()>0;
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    private boolean isPriceDetailAdded(Item i, Connection connection) {
        try {
            PreparedStatement statement=connection.prepareStatement("INSERT INTO `Price Detail` VALUES (?,?,?,?,?,?)");
            statement.setObject(1,i.getCode());
            statement.setObject(2,i.getName());
            statement.setObject(3, DateTimeUtil.currentDate(1));
            statement.setObject(4,DateTimeUtil.currentTime(2));
            statement.setObject(5,i.getUnitPrice());
            statement.setObject(6,i.getUnitSalePrice());
            return statement.executeUpdate()>0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    private boolean isItemAdded(Item i, Connection connection) {
        try {
            PreparedStatement statement=connection.prepareStatement("INSERT INTO Item VALUES (?,?,?,?,?,?,?)");
            statement.setObject(1,i.getCode());
            statement.setObject(2,i.getName());
            statement.setObject(3,i.getDescription());
            statement.setObject(4,i.getPreservedTime());
            statement.setObject(5,i.getQtyOnHand());
            statement.setObject(6,i.getUnitPrice());
            statement.setObject(7,i.getUnitSalePrice());
            return statement.executeUpdate()>0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    private boolean isItemUpdated(Item i, Connection connection) {
        try {
            PreparedStatement statement=connection.prepareStatement("UPDATE Item SET name=?, description=?, preservedTime=?, qtyOnHand=?, unitPrice=?, unitSalePrice=? WHERE itemCode=?");
            statement.setObject(7,i.getCode());
            statement.setObject(1,i.getName());
            statement.setObject(2,i.getDescription());
            statement.setObject(3,i.getPreservedTime());
            statement.setObject(4,i.getQtyOnHand());
            statement.setObject(5,i.getUnitPrice());
            statement.setObject(6,i.getUnitSalePrice());
            return statement.executeUpdate()>0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public String getItemCode() {
        try {
            PreparedStatement statement=DatabaseConnection.getInstance().getConnection().prepareStatement("SELECT itemCode FROM Item ORDER BY itemCode DESC LIMIT 1");
            ResultSet resultSet=statement.executeQuery();
            if(resultSet.next()){
                int index=Integer.parseInt(resultSet.getString(1).split("-")[1]);
                if(index<9){
                    return "I-0000"+ ++index;
                }else if(index<99){
                    return "I-000"+ ++index;
                }else if(index<999){
                    return "I-00"+ ++index;
                }else if(index<9999){
                    return "I-0"+ ++index;
                }else{
                    return "I-"+ ++index;
                }
            }else{
                return "I-00001";
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return "";
    }

    public ArrayList<Item> getAllItem() {
        try {
            PreparedStatement statement=DatabaseConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Item");
            ResultSet resultSet=statement.executeQuery();
            ArrayList<Item> itemList=new ArrayList<>();
            while(resultSet.next()){
                itemList.add(new Item(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getString(4),
                                resultSet.getInt(5),
                                resultSet.getDouble(6),
                                resultSet.getDouble(7)
                        )
                );
            }
            return itemList;
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }

    public ArrayList<String> getAllItemCodes() {
        try {
            PreparedStatement statement=DatabaseConnection.getInstance().getConnection().prepareStatement("SELECT itemCode FROM Item");
            ResultSet resultSet=statement.executeQuery();
            ArrayList<String> medicineList=new ArrayList<>();
            while(resultSet.next()){
                medicineList.add(resultSet.getString(1));
            }
            return medicineList;
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Item getItem(String itemCode) {
        try {
            PreparedStatement statement=DatabaseConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Item WHERE itemCode=?");
            statement.setObject(1,itemCode);
            ResultSet resultSet=statement.executeQuery();
            if(resultSet.next()){
                return new Item(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getInt(5),
                        resultSet.getDouble(6),
                        resultSet.getDouble(7)
                );
            }else{
                return null;
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
    public PriceDetail getPriceDetail(Item item, String date, String time) {
        try {
            PreparedStatement statement=DatabaseConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `Price Detail` ORDER BY date DESC");
            ResultSet resultSet=statement.executeQuery();
            ArrayList<PriceDetail> detailList=new ArrayList<>();
            while(resultSet.next()){
                if(resultSet.getString(1).equals(item.getCode()) && resultSet.getString(2).equals(item.getName())){
                    detailList.add(new PriceDetail(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getDouble(5),
                            resultSet.getDouble(6)
                    ));
                }
            }
            for(PriceDetail pd: detailList) {
                SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
                Date givenDate=sdfDate.parse(date);
                Date detailDate=sdfDate.parse(pd.getDate());
                Date givenTime=sdfTime.parse(time);
                Date detailTime=sdfTime.parse(pd.getTime());
                if(givenDate.compareTo(detailDate)>0){
                    return pd;
                }else if(givenDate.compareTo(detailDate)==0){
                    if(givenTime.compareTo(detailTime)>=0){
                        return pd;
                    }
                }
                int last=detailList.size()-1;
                return detailList.get(last);
            }
        } catch (SQLException | ClassNotFoundException | ParseException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public ArrayList<IncomeTM> getAllIncomes() {
        try {
            PreparedStatement statement= DatabaseConnection.getInstance().getConnection().prepareStatement("SELECT stockDate,time,(-cost) FROM Stock UNION SELECT orderDate,time,cost FROM `Order` ORDER BY stockDate");
            ResultSet resultSet=statement.executeQuery();
            ArrayList<IncomeTM> incomeTMs=new ArrayList<>();
            while(resultSet.next()){
                incomeTMs.add(new IncomeTM(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getDouble(3)
                        )
                );
            }
            return incomeTMs;
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }


}

