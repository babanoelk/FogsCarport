package app.dtos;

import java.sql.Date;

public class GetOrderWithIdDateCustomerNoteConsentStatus {
    private int orderId;
    private Date date;
    private String customerNote;
    private String orderStatus;

    public GetOrderWithIdDateCustomerNoteConsentStatus(int orderId, Date date, String customerNote, String orderStatus) {
        this.orderId = orderId;
        this.date = date;
        this.customerNote = customerNote;
        this.orderStatus = orderStatus;
    }

    public int getOrderId() {
        return orderId;
    }

    public Date getDate() {
        return date;
    }

    public String getCustomerNote() {
        return customerNote;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
