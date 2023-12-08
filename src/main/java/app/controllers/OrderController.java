package app.controllers;

import app.dtos.DTOGetOrderWithIdDateCustomerNoteConsentStatus;
import app.dtos.DTOSpecificOrderByOrderIdWithUserAndCarportAndShed;
import app.entities.Order;
import app.entities.Status;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.persistence.StatusMapper;
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
                List<Status> statusList = StatusMapper.getAllStatuses(connectionPool);
                List<Order> allOrders = OrderMapper.getAllOrders(connectionPool);
                //ctx.sessionAttribute("currentSession", "all");

                ctx.attribute("statusList", statusList);
                ctx.attribute("allOrders", allOrders);
                ctx.render("admin-side.html");
            }
        }
        catch (DatabaseException e)
        {
            throw new DatabaseException("Fejl ved indlæsning af kundeordre " + e.getMessage());
        }
    }

    public static void updateOrderStatus(Context ctx, ConnectionPool connectionPool) {
        try {
            int orderId = Integer.parseInt(ctx.formParam("order_id"));
            int statusId = Integer.parseInt(ctx.formParam("status_id"));
            OrderMapper.updateOrderStatus(orderId, statusId, connectionPool);
            getAllOrders(ctx, connectionPool);
        } catch (DatabaseException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void orderContact(Context ctx) {
        int orderId = Integer.parseInt(ctx.formParam("order_id"));
        ctx.attribute("order_id", orderId);

        ctx.render("kontakt.html");
    }




}
