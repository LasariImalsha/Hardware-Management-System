package model;

import java.util.ArrayList;

public class Stock {
    private String batchNo;
    private String supplierId;
    private String shipmentDate;
    private String time;
    private double cost;
    private ArrayList<StockDetail> detailList;

    public Stock(){  }

    public Stock(String batchNo, String supplierId, String shipmentDate, String time, double cost, ArrayList<StockDetail> detailList) {
        this.setBatchNo(batchNo);
        this.setSupplierId(supplierId);
        this.setShipmentDate(shipmentDate);
        this.setTime(time);
        this.setCost(cost);
        this.setDetailList(detailList);
    }

    public String getBatchNo() {
        return batchNo;
    }
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getSupplierId() {
        return supplierId;
    }
    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getShipmentDate() {
        return shipmentDate;
    }
    public void setShipmentDate(String shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public double getCost() {
        return cost;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }

    public ArrayList<StockDetail> getDetailList() {
        return detailList;
    }
    public void setDetailList(ArrayList<StockDetail> detailList) {
        this.detailList = detailList;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "batchNo='" + getBatchNo() + '\'' +
                ", supplierId='" + getSupplierId() + '\'' +
                ", stockDate='" + getShipmentDate() + '\'' +
                ", time='" + getTime() + '\'' +
                ", cost=" + getCost() +
                ", detailList=" + getDetailList() +
                '}';
    }
}
