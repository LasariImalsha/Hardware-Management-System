package model.tableModel;

public class CartTM {
    private String itemCode;
    private String name;
    private double unitPrice;
    private int quantity;
    private double price;

    public CartTM() { }

    public CartTM(String itemCode, String name, double unitPrice, int quantity, double price) {
        this.setItemCode(itemCode);
        this.setName(name);
        this.setUnitPrice(unitPrice);
        this.setQuantity(quantity);
        this.setPrice(price);
    }

    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String medicineCode) {
        this.itemCode = medicineCode;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "CartTM{" +
                "itemCode='" + getItemCode() + '\'' +
                ", name='" + getName() + '\'' +
                ", unitPrice=" + getUnitPrice() +
                ", quantity=" + getQuantity() +
                ", price=" + getPrice() +
                '}';
    }
}
