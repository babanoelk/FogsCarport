package app.dtos;

import java.sql.Date;

public class DTOOrderCustomer {

    private int id;
    private Date date;
    private String customerNote;
    private int statusId;
    private String customerName;
    private String email;
    private int mobile;
    private String orderStatus;
    private float price;


    public DTOOrderCustomer(int id, Date date, String customerNote, int statusId, String customerName, String email, int mobile, String orderStatus, float price) {
        this.id = id;
        this.date = date;
        this.customerNote = customerNote;
        this.statusId = statusId;
        this.customerName = customerName;
        this.email = email;
        this.mobile = mobile;
        this.orderStatus = orderStatus;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getCustomerNote() {
        return customerNote;
    }

    public int getStatusId() {
        return statusId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getEmail() {
        return email;
    }

    public int getMobile() {
        return mobile;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public float getPrice() {
        return price;
    }
}
