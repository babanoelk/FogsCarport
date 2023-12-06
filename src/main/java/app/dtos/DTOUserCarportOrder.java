package app.dtos;

import app.entities.*;

public class DTOUserCarportOrder {
    private User user;
    private Carport carport;
    private Order order;

    public DTOUserCarportOrder(User user, Carport carport, Order order) {
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
