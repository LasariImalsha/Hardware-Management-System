package model;

public class Item {
    private String code;
    private String name;
    private String description;
    private String preservedTime;
    private int qtyOnHand;
    private double unitPrice;
    private double unitSalePrice;

    public Item(String txtItemCode1Text, String text, String txtName1Text, double txtDescription1Text, double txtUnitPrice1Text){ }

    public Item(String  code, String name, String description, String preservedTime, int qtyOnHand, double unitPrice, double unitSalePrice) {
        this.setCode(code);
        this.setName(name);
        this.setDescription(description);
        this.setPreservedTime(preservedTime);
        this.setQtyOnHand(qtyOnHand);
        this.setUnitPrice(unitPrice);
        this.setUnitSalePrice(unitSalePrice);
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getPreservedTime() {
        return preservedTime;
    }
    public void setPreservedTime(String preservedTime) {
        this.preservedTime = preservedTime;
    }

    public int getQtyOnHand() {
        return qtyOnHand;
    }
    public void setQtyOnHand(int qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }

    public double getUnitPrice() {return unitPrice;}
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getUnitSalePrice() {
        return unitSalePrice;
    }
    public void setUnitSalePrice(double unitSalePrice) {
        this.unitSalePrice = unitSalePrice;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemCode='" + getCode() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", preservedTime='" + getPreservedTime() + '\'' +
                ", qtyOnHand=" + getQtyOnHand() +
                ", unitPrice=" + getUnitPrice() +
                ", unitSalePrice=" + getUnitSalePrice() +
                '}';
    }
}
