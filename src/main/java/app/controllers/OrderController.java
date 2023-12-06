package app.controllers;

import app.dtos.DTOGetOrderWithIdDateCustomerNoteConsentStatus;
import app.dtos.DTOSpecificOrderByOrderIdWithUserAndCarportAndShed;
import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import io.javalin.http.Context;

import java.util.List;

public class OrderController {


    public static void getSpecificOrder(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

        try {
            User user = ctx.sessionAttribute("currentUser");
            int result = Integer.parseInt(ctx.formParam("order_id"));
            DTOSpecificOrderByOrderIdWithUserAndCarportAndShed specificOrderByOrderIdWithUserAndCarportAndShed = OrderMapper.getSpecificOrderByOrderIdWithUserAndCarportAndShed(result,connectionPool);
            ctx.attribute("order",specificOrderByOrderIdWithUserAndCarportAndShed);
            //specificOrderByOrderIdWithUserAndCarportAndShed.getUserI
            ctx.render("se-nærmere-på-ordre.html");
        } catch (DatabaseException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public static boolean deleteOrder(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

        try {
            User user = ctx.sessionAttribute("currentUser");
            int result = Integer.parseInt(ctx.formParam("order_id"));
            OrderMapper.deleteOrderByOrderID(result,connectionPool);

            ctx.render("ordre-slettet.html");
            return true;
        } catch (DatabaseException e) {
            throw new DatabaseException("Fejl sletning af ordre " + e.getMessage());
        }
    }

    public static void getAllOrders(Context ctx, ConnectionPool connectionPool) throws DatabaseException
    {
        try
        {
            User user = ctx.sessionAttribute("currentUser");
            if (user.getRole() == 1) {
                List<DTOGetOrderWithIdDateCustomerNoteConsentStatus> orders = OrderMapper.getAllOrdersByUser(user, connectionPool);
                ctx.attribute("orderlist", orders);
                ctx.render("min-side.html");
            } else {
                List<Order> allOrders = OrderMapper.getAllOrders(connectionPool);
                ctx.attribute("allOrders", allOrders);
                ctx.render("admin-side.html");
            }
        }
        catch (DatabaseException e)
        {
            throw new DatabaseException("Fejl ved indlæsning af kundeordre " + e.getMessage());
        }
    }

}
