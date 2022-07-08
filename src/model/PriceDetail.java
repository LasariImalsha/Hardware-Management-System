package model;

public class PriceDetail {
    private String itemCode;
    private String name;
    private String date;
    private String time;
    private double unitPrice;
    private double unitSalePrice;

    public PriceDetail() { }

    public PriceDetail(String medicineCode, String name, String date, String time, double unitPrice, double unitSalePrice) {
        this.setMedicineCode(medicineCode);
        this.setName(name);
        this.setDate(date);
        this.setTime(time);
        this.setUnitPrice(unitPrice);
        this.setUnitSalePrice(unitSalePrice);
    }

    public String getItemCode() {
        return itemCode;
    }
    public void setMedicineCode(String medicineCode) {
        this.itemCode = medicineCode;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public double getUnitPrice() {
        return unitPrice;
    }
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
        return "PriceDetail{" +
                "itemCode='" + getItemCode() + '\'' +
                ", name='" + getName() + '\'' +
                ", date='" + getDate() + '\'' +
                ", time='" + getTime() + '\'' +
                ", unitPrice=" + getUnitPrice() +
                ", unitSalePrice=" + getUnitSalePrice() +
                '}';
    }
}
