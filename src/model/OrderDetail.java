package model;

public class OrderDetail {
   private String orderId;
   private String itemCode;
   private double unitPrice;
   private int quantity;
   private double price;

    public OrderDetail() { }

    public OrderDetail(String orderId, String itemCode, double unitPrice, int quantity, double price) {
        this.setOrderId(orderId);
        this.setItemCode(itemCode);
        this.setUnitPrice(unitPrice);
        this.setQuantity(quantity);
        this.setPrice(price);
    }

    public String getOrderId() {return orderId;}
    public void setOrderId(String orderId) {this.orderId = orderId;}

    public String getItemCode() {return itemCode;}
    public void setItemCode(String itemCode) {this.itemCode = itemCode;}

    public double getUnitPrice() {return unitPrice;}
    public void setUnitPrice(double unitPrice) {this.unitPrice = unitPrice;}

    public int getQuantity() {return quantity;}
    public void setQuantity(int quantity) {this.quantity = quantity;}

    public double getPrice() {return price;}
    public void setPrice(double price) {this.price = price;}

    @Override
    public String toString() {
        return "OrderDetail{" +
                "orderId='" + getOrderId() + '\'' +
                ", itemCode='" + getItemCode() + '\'' +
                ", unitPrice=" + getUnitPrice() +
                ", quantity=" + getQuantity() +
                ", price=" + getPrice() +
                '}';
    }
}
