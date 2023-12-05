package app.dtos;

import app.entities.*;
import app.persistence.ConnectionPool;

public class UserCarportOrderDTO {
    private User user;
    private Carport carport;
    private Order order;

    public UserCarportOrderDTO(User user, Carport carport, Order order) {
        this.user = user;
        this.carport = carport;
        this.order = order;
    }

    public User getUser() {
        return user;
    }

    public Carport getCarport() {
        return carport;
    }

    public Order getOrder() {
        return order;
    }
}
