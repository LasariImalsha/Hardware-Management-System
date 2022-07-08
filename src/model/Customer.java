package model;

public class Customer {
    private String id;
    private String name;
    private String address;
    private String contact;

    public Customer(){ }

    public Customer(String id, String name, String address, String contact){
        this.setId(id);
        this.setName(name);
        this.setAddress(address);
        this.setContact(contact);
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Customer{" +
                "customerId='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", contact='" + getContact() + '\'' +
                '}';
    }
}


