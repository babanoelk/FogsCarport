package app.entities;

import java.sql.Date;

public class Order {
    private int id;
    private Date date;
    private String customerNote;
    private boolean consent;
    private int userId;
    private String orderStatus;
    private int carportId;

    public Order(Date date, boolean consent) {
        this.date = date;
        this.consent = consent;
    }

    public Order(int id, Date date, String customerNote, boolean consent, int userId, String orderStatus, int carportId) {
        this.id = id;
        this.date = date;
        this.customerNote = customerNote;
        this.consent = consent;
        this.userId = userId;
        this.orderStatus = orderStatus;
        this.carportId = carportId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCustomerNote() {
        return customerNote;
    }

    public void setCustomerNote(String customerNote) {
        this.customerNote = customerNote;
    }

    public boolean getConsent() {
        return consent;
    }

    public void setConsent(boolean consent) {
        this.consent = consent;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getCarportId() {
        return carportId;
    }

    public void setCarportId(int carportId) {
        this.carportId = carportId;
    }
}
