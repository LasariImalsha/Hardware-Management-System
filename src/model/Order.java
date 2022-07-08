package model;

import java.util.ArrayList;

public class Order {
    private String orderId;
    private String customerId;
    private String orderDate;
    private String time;
    private double cost;
    private ArrayList<OrderDetail> detailList;

    public Order() {  }

    public Order(String orderId, String customerId, String orderDate, String time, double cost, ArrayList<OrderDetail> detailList) {
        this.setOrderId(orderId);
        this.setCustomerId(customerId);
        this.setOrderDate(orderDate);
        this.setTime(time);
        this.setCost(cost);
        this.setDetailList(detailList);
    }

    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
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

    public ArrayList<OrderDetail> getDetailList() {
        return detailList;
    }
    public void setDetailList(ArrayList<OrderDetail> detailList) {
        this.detailList = detailList;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + getOrderId() + '\'' +
                ", customerId='" + getCustomerId() + '\'' +
                ", orderDate='" + getOrderDate() + '\'' +
                ", time='" + getTime() + '\'' +
                ", cost=" + getCost() +
                ", detailList=" + getDetailList() +
                '}';
    }
}
