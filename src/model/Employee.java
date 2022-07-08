package model;

public class Employee {
    private String employeeId;
    private String userId;
    private String name;
    private String address;
    private String contact;
    private String occupation;
    private double salary;
    private UserDetail userDetail;

    public Employee(String text, String txtEmpNameText, String txtEmpAddressText, String txtEmpContactText, String txtOccupationText, double v) {
    }

    public Employee(String employeeId, String userId, String name, String address, String contact, String occupation, double salary, UserDetail userDetail) {
        this.setEmployeeId(employeeId);
        this.setUserId(userId);
        this.setName(name);
        this.setAddress(address);
        this.setContact(contact);
        this.setOccupation(occupation);
        this.setSalary(salary);
        this.setUserDetail(userDetail);
    }

    public String getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getOccupation() {
        return occupation;
    }
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public double getSalary() {
        return salary;
    }
    public void setSalary(double salary) {
        this.salary = salary;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }
    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId='" + getEmployeeId() + '\'' +
                ", userId='" + getUserId() + '\'' +
                ", name='" + getName() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", contact='" + getContact() + '\'' +
                ", occupation='" + getOccupation() + '\'' +
                ", salary=" + getSalary() +
                ", userDetail=" + getUserDetail() +
                '}';
    }
}
