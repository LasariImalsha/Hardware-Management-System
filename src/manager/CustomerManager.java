package manager;

import database.DatabaseConnection;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerManager {

    public boolean addCustomer(Customer c){
        try {
            PreparedStatement statement=DatabaseConnection.getInstance().getConnection().prepareStatement("INSERT INTO Customer VALUES (?,?,?,?)");
            statement.setObject(1,c.getId());
            statement.setObject(2,c.getName());
            statement.setObject(3,c.getAddress());
            statement.setObject(4,c.getContact());
            return statement.executeUpdate()>0;
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
    public boolean updateCustomer(Customer c){
        try {
            PreparedStatement statement=DatabaseConnection.getInstance().getConnection().prepareStatement("UPDATE Customer SET name=?, address=?, contact=? WHERE customerId=?");
            statement.setObject(4,c.getId());
            statement.setObject(1,c.getName());
            statement.setObject(2,c.getAddress());
            statement.setObject(3,c.getContact());
            return statement.executeUpdate()>0;
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
    public boolean deleteCustomer(String custId){
        try {
            PreparedStatement statement=DatabaseConnection.getInstance().getConnection().prepareStatement("DELETE FROM Customer WHERE customerId='"+custId+"'");
            return statement.executeUpdate()>0;
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public ArrayList<Customer> getAllCustomers(){
        try {
            PreparedStatement statement=DatabaseConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer");
            ResultSet resultSet=statement.executeQuery();
            ArrayList<Customer> customers=new ArrayList<>();
            while(resultSet.next()){
                customers.add(new Customer(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getString(4)
                        )
                );
            }
            return customers;
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }

    public String getCustomerId(){
        try {
            PreparedStatement statement=DatabaseConnection.getInstance().getConnection().prepareStatement("SELECT customerId FROM Customer ORDER BY customerId DESC LIMIT 1");
            ResultSet resultSet=statement.executeQuery();
            if(resultSet.next()){
                int index=Integer.parseInt(resultSet.getString(1).split("-")[1]);
                if(index<9){
                    return "C-0000"+ ++index;
                }else if(index<99){
                    return "C-000"+ ++index;
                }else if(index<999){
                    return "C-00"+ ++index;
                }else if(index<9999){
                    return "C-0"+ ++index;
                }else{
                    return "C-"+ ++index;
                }
            }else{
                return "C-00001";
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return "";
    }

    public Customer getCustomer(String custId){
        try {
            PreparedStatement statement=DatabaseConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer WHERE customerId=?");
            statement.setObject(1,custId);
            ResultSet resultSet=statement.executeQuery();
            if(resultSet.next()){
                return new Customer(
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

    public ArrayList<String> getAllCustomerIds(){
        /*PreparedStatement statement=DatabaseConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer");
        ResultSet resultSet=statement.executeQuery();*/
        ArrayList<String> customerList=new ArrayList<>();
        ArrayList<Customer> customers=new CustomerManager().getAllCustomers();
        for(Customer c: customers){
            customerList.add(c.getId());
        }
        /*while(resultSet.next()){
            resultSet.getString(1);
        }*/
        return customerList;
    }

    public ArrayList<Customer> searchCustomers(String filterText) {
        try{
            //PreparedStatement statement=DatabaseConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer WHERE CONCAT(customerId,name,address) LIKE `%"+filterText+"%`");
            PreparedStatement statement=DatabaseConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer WHERE address LIKE `%"+filterText+"%`");
            ResultSet resultSet=statement.executeQuery();
            ArrayList<Customer> customers=new ArrayList<>();
            while(resultSet.next()){
                customers.add(new Customer(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getString(4)
                        )
                );
            }
            return customers;
        }catch (SQLException | ClassNotFoundException throwables){
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }
}
