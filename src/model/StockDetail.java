package model;

public class StockDetail {
    private String batchNo;
    private String itemCode;
    private double unitPrice;
    private int quantity;
    private double price;

    public StockDetail() { }

    public StockDetail(String batchNo, String itemCode, double unitPrice, int quantity, double price) {
        this.setBatchNo(batchNo);
        this.setItemCode(itemCode);
        this.setUnitPrice(unitPrice);
        this.setQuantity(quantity);
        this.setPrice(price);
    }

    public String getBatchNo() {return batchNo;}
    public void setBatchNo(String batchNo) {this.batchNo = batchNo;}

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
        return "StockDetail{" +
                "batchNo='" + getBatchNo() + '\'' +
                ", itemCode='" + getItemCode() + '\'' +
                ", unitPrice=" + getUnitPrice() +
                ", quantity=" + getQuantity() +
                ", price=" + getPrice() +
                '}';
    }
}
