package manager;

import database.DatabaseConnection;
import model.Employee;
import model.UserDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class EmployeeManager {

    public ArrayList<Employee> getAllEmployees() {
        try {
            PreparedStatement statement= DatabaseConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Employee");
            ResultSet resultSet=statement.executeQuery();
            ArrayList<Employee> employees=new ArrayList<>();
            while(resultSet.next()){
                UserDetail userDetail=getUserDetail(resultSet.getString(2));
                employees.add(new Employee(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getString(4),
                                resultSet.getString(5),
                                resultSet.getString(6),
                                resultSet.getDouble(7),
                                userDetail
                        )
                );
            }
            return employees;
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }
    public Employee getEmployee(String empId) {
        try {
            PreparedStatement statement= DatabaseConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Employee  WHERE employeeId=?");
            statement.setObject(1,empId);
            ResultSet resultSet=statement.executeQuery();
            if(resultSet.next()){
                UserDetail userDetail=getUserDetail(resultSet.getString(2));
                return new Employee(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getDouble(7),
                        userDetail
                );
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public UserDetail getUserDetail(String userId) {
        try {
            PreparedStatement statement= DatabaseConnection.getInstance().getConnection().prepareStatement("SELECT userId,username,password,role FROM `User Detail` WHERE userId=?");
            statement.setObject(1,userId);
            ResultSet resultSet=statement.executeQuery();
            if(resultSet.next()){
                return new UserDetail(
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

    public String getEmployeeId() {
        try {
            PreparedStatement statement=DatabaseConnection.getInstance().getConnection().prepareStatement("SELECT employeeId FROM Employee ORDER BY employeeId DESC LIMIT 1");
            ResultSet resultSet=statement.executeQuery();
            if(resultSet.next()){
                int index=Integer.parseInt(resultSet.getString(1).split("-")[1]);
                if(index<9){
                    return "E-00"+ ++index;
                }else if(index<99){
                    return "E-0"+ ++index;
                }else{
                    return "E-"+ ++index;
                }
            }else{
                return "E-001";
            }
        }catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return "";
    }

    public boolean addEmployee(Employee employee) {
        Connection connection=null;
        try{
            connection=DatabaseConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            if(isUserDetailAdded(employee,connection) && isEmployeeAdded(employee,connection)){
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

    private boolean isUserDetailAdded(Employee e, Connection connection) {
        try {
            UserDetail ud=e.getUserDetail();
            PreparedStatement statement=connection.prepareStatement("INSERT INTO `User Detail` VALUES(?,?,?,?)");
            statement.setObject(1, ud.getUserId());
            statement.setObject(2, ud.getUsername());
            statement.setObject(3, ud.getPassword());
            statement.setObject(4, ud.getRole());
            return statement.executeUpdate()>0;
        }catch(SQLException throwables){
            throwables.printStackTrace();
        }
        return false;
    }

    private boolean isEmployeeAdded(Employee e, Connection connection) {
        try {
            PreparedStatement statement=connection.prepareStatement("INSERT INTO Employee VALUES(?,?,?,?,?,?,?)");
            statement.setObject(1, e.getEmployeeId());
            statement.setObject(2, e.getUserId());
            statement.setObject(3, e.getName());
            statement.setObject(4, e.getAddress());
            statement.setObject(5, e.getContact());
            statement.setObject(6, e.getOccupation());
            statement.setObject(7, e.getSalary());
            return statement.executeUpdate()>0;
        }catch(SQLException throwables){
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean updateEmployee(Employee employee) {
        Connection connection=null;
        try{
            connection=DatabaseConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            Employee e=getEmployee(employee.getEmployeeId());
            if(isEmployeeDeleted(e,connection) && isUserDetailDeleted(e,connection) && isUserDetailAdded(employee,connection) && isEmployeeAdded(employee,connection)){
                /*if(isEmployeeUpdated(e,connection) && isUserDetailUpdated(e,connection)){*/
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

    private boolean isEmployeeUpdated(Employee e, Connection connection) {
        try {
            PreparedStatement statement=connection.prepareStatement("UPDATE Employee SET userId=? name=? address=? contact=? occupation=? salary=? WHERE employeeId=?");
            statement.setObject(7, e.getEmployeeId());
            statement.setObject(1, e.getUserId());
            statement.setObject(2, e.getName());
            statement.setObject(3, e.getAddress());
            statement.setObject(4, e.getContact());
            statement.setObject(5, e.getOccupation());
            statement.setObject(6, e.getSalary());
            return statement.executeUpdate()>0;
        }catch(SQLException throwables){
            throwables.printStackTrace();
        }
        return false;
    }

    private boolean isUserDetailUpdated(Employee e, Connection connection) {
        try {
            UserDetail ud=e.getUserDetail();
            PreparedStatement statement=connection.prepareStatement("UPDATE `User Detail` SET userId=?, password=AES_ENCRYPT(?,'ramindu'), role=? WHERE username=?");
            statement.setObject(1, ud.getUserId());
            statement.setObject(4, ud.getUsername());
            statement.setObject(2, ud.getPassword());
            statement.setObject(3, ud.getRole());
            return statement.executeUpdate()>0;
        }catch(SQLException throwables){
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean deleteEmployee(Employee employee) {
        Connection connection=null;
        try{
            connection=DatabaseConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            if(isEmployeeDeleted(employee,connection) && isUserDetailDeleted(employee,connection)){
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

    private boolean isUserDetailDeleted(Employee e, Connection connection) {
        try {
            PreparedStatement statement=connection.prepareStatement("DELETE FROM `User Detail` WHERE userId=?");
            statement.setObject(1,e.getUserId());
            return statement.executeUpdate()>0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    private boolean isEmployeeDeleted(Employee e, Connection connection) {
        try {
            PreparedStatement statement=connection.prepareStatement("DELETE FROM Employee WHERE employeeId=?");
            statement.setObject(1,e.getEmployeeId());
            return statement.executeUpdate()>0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public String getUserId(String role) {
        try {
            PreparedStatement statement=DatabaseConnection.getInstance().getConnection().prepareStatement("SELECT userId FROM `User Detail` WHERE role=? ORDER BY userId DESC LIMIT 1");
            statement.setObject(1,role);
            ResultSet resultSet=statement.executeQuery();
            if(role.equals("Assistant")){
                if (resultSet.next()) {
                    int index=Integer.parseInt(resultSet.getString(1).split("-")[1]);
                    if (index < 9) {
                        return "As-00" + ++index;
                    } else if (index < 99) {
                        return "As-0" + ++index;
                    } else {
                        return "As-" + ++index;
                    }
                } else {
                    return "As-001";
                }
            }else if(role.equals("Pharmacist")){
                if (resultSet.next()) {
                    int index=Integer.parseInt(resultSet.getString(1).split("-")[1]);
                    if (index < 9) {
                        return "Ph-00" + ++index;
                    } else if (index < 99) {
                        return "Ph-0" + ++index;
                    } else {
                        return "Ph-" + ++index;
                    }
                } else {
                    return "Ph-001";
                }
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return "";
    }

    public String getUsername() {
        try {
            PreparedStatement statement=DatabaseConnection.getInstance().getConnection().prepareStatement("SELECT username FROM `User Detail`");
            ResultSet resultSet=statement.executeQuery();
            while(true){
                int length=6;
                String chars="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$&";
                Random random=new Random();
                StringBuilder sb=new StringBuilder(length);
                for(int i=0; i<length; i++){
                    sb.append(chars.charAt(random.nextInt(chars.length())));
                }
                String username="user"+sb.toString();
                String text=null;
                while(resultSet.next()){
                    text=resultSet.getString(1);
                    if(text.equals(username)){
                        break;
                    }
                }
                if(text==null){
                    return username;
                }else if(!text.equals(username)){
                    return username;
                }
            }
        }catch(SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return "";
    }

}
