package manager;

import database.DatabaseConnection;
import model.Supplier;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierManager {
    public boolean addSupplier(Supplier s){
        try {
            PreparedStatement statement= DatabaseConnection.getInstance().getConnection().prepareStatement("INSERT INTO Supplier VALUES (?,?,?,?)");
            statement.setObject(1,s.getId());
            statement.setObject(2,s.getName());
            statement.setObject(3,s.getAddress());
            statement.setObject(4,s.getContact());
            return statement.executeUpdate()>0;
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
    public boolean updateSupplier(Supplier s){
        try {
            PreparedStatement statement=DatabaseConnection.getInstance().getConnection().prepareStatement("UPDATE Supplier SET name=?, address=?, contact=? WHERE supplierId=?");
            statement.setObject(4,s.getId());
            statement.setObject(1,s.getName());
            statement.setObject(2,s.getAddress());
            statement.setObject(3,s.getContact());
            return statement.executeUpdate()>0;
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
    public boolean deleteSupplier(String supId){
        try {
            PreparedStatement statement=DatabaseConnection.getInstance().getConnection().prepareStatement("DELETE FROM Supplier WHERE supplierId=?");
            statement.setObject(1,supId);
            return statement.executeUpdate()>0;
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public ArrayList<Supplier> getAllSuppliers(){
        try {
            PreparedStatement statement=DatabaseConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Supplier");
            ResultSet resultSet=statement.executeQuery();
            ArrayList<Supplier> suppliers=new ArrayList<>();
            while(resultSet.next()){
                suppliers.add(new Supplier(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getString(4)
                        )
                );
            }
            return suppliers;
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }

    public String getSupplierId(){
        try {
            PreparedStatement statement=DatabaseConnection.getInstance().getConnection().prepareStatement("SELECT supplierId FROM Supplier ORDER BY supplierId DESC LIMIT 1");
            ResultSet resultSet=statement.executeQuery();
            if(resultSet.next()){
                int index=Integer.parseInt(resultSet.getString(1).split("-")[1]);
                if(index<9){
                    return "S-00"+ ++index;
                }else if(index<99){
                    return "S-0"+ ++index;
                }else{
                    return "S-"+ ++index;
                }
            }else{
                return "S-001";
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return "";
    }

    public Supplier getSupplier(String supId){
        try {
            PreparedStatement statement=DatabaseConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Supplier WHERE supplierId=?");
            statement.setObject(1,supId);
            ResultSet resultSet=statement.executeQuery();
            if(resultSet.next()){
                return new Supplier(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4)
                );
            }else{
                return null;
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> getAllSupplierIds(){
        try {
            PreparedStatement statement=DatabaseConnection.getInstance().getConnection().prepareStatement("SELECT supplierId FROM Supplier");
            ResultSet resultSet=statement.executeQuery();
            ArrayList<String> supplierList=new ArrayList<>();
            while(resultSet.next()){
                supplierList.add(resultSet.getString(1));
            }
            return supplierList;
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }
}
