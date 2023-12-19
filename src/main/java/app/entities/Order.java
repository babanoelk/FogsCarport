package app.entities;

import java.sql.Date;

public class Order {
    private int id;
    private Date date;
    private String customerNote;
    private int userId;
    private String orderStatus; //this is an int in DB
    private int carportId;

    public Order(String customerNote) {
        this.customerNote = customerNote;
    }

    public Order(int id, Date date, String customerNote, String orderStatus) {
        this.id = id;
        this.date = date;
        this.customerNote = customerNote;
        this.orderStatus = orderStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerNote() {
        return customerNote;
    }

    public int getUserId() {
        return userId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public int getCarportId() {
        return carportId;
    }

}
