package app.controllers;

import app.dtos.DTOUserCarportOrder;
import app.entities.Carport;
import app.entities.Order;
import app.entities.Shed;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MeasurementMapper;
import app.persistence.OrderMapper;
import app.services.CarportSvgTopView;
import app.utility.Calculator;
import io.javalin.http.Context;

import java.util.List;
import java.util.Locale;


public class FormController {


    public static void createCustomerRequest(Context ctx, ConnectionPool connectionPool) {

        User user = getUser(ctx);
        boolean loggedIn = user != null;

        try {
            DTOUserCarportOrder dto = createOrder(ctx, user, loggedIn, connectionPool);
            sendOrderConfirmation(ctx, user, dto.getCarport());

        } catch (Exception e) {
            handleOrderCreationError(ctx, connectionPool, e);
        }
    }

    private static User getUser(Context ctx) {
        if (ctx.sessionAttribute("currentUser") == null) {
            return UserController.createUser(ctx);
        } else {
            return ctx.sessionAttribute("currentUser");
        }
    }

    private static DTOUserCarportOrder createOrder(Context ctx, User user, boolean loggedIn, ConnectionPool connectionPool) {
        int carportWidth = Integer.parseInt(ctx.formParam("carport_width"));
        int carportLength = Integer.parseInt(ctx.formParam("carport_length"));
        int carportHeight = Integer.parseInt(ctx.formParam("carport_height"));
        String note = ctx.formParam("note");

        Carport carport = new Carport(carportWidth, carportLength, carportHeight);
        Order order = new Order(note);

        String shedChoice = ctx.formParam("redskabsrum");
        if (shedChoice.equalsIgnoreCase("ja")) {
            int shedWidth = Integer.parseInt(ctx.formParam("shed_width"));
            int shedLength = Integer.parseInt(ctx.formParam("shed_length"));
            Shed shed = new Shed(shedWidth, shedLength);
            carport.setShed(shed);
        }

        DTOUserCarportOrder dto = new DTOUserCarportOrder(user, carport, order);
        float carportPrice = Calculator.carportPriceCalculator2(dto);

        try {
            if (loggedIn) {
                OrderMapper.addOrderToExistingUser(dto, carportPrice, connectionPool);
            } else {
                OrderMapper.addOrder(dto, carportPrice, connectionPool);
            }
        } catch (DatabaseException e) {
            handleOrderCreationError(ctx, connectionPool, e);
        }
        return dto;
    }

    private static void sendOrderConfirmation(Context ctx, User user, Carport carport) {
        Locale.setDefault(new Locale("US"));
        CarportSvgTopView svg = new CarportSvgTopView(carport.getLength(), carport.getWidth());
        ctx.attribute("svg", svg.toString());
        ctx.attribute("name", user.getName());
        ctx.attribute("length", carport.getLength());
        ctx.attribute("width", carport.getWidth());
        ctx.attribute("height", carport.getHeight());
        ctx.render("tilbud-indsendt.html");
    }

    private static void handleOrderCreationError(Context ctx, ConnectionPool connectionPool, Exception e) {
        loadMeasurements(ctx, connectionPool);
        ctx.attribute("message", e.getMessage());
        ctx.render("bestilling.html");
    }


    /*
        public static void createCustomerRequest(Context ctx, ConnectionPool connectionPool) {

            User user;
            boolean loggedIn = false;

            try {
                //Carport data
                int carportWidth = Integer.parseInt(ctx.formParam("carport_width"));
                int carportLength = Integer.parseInt(ctx.formParam("carport_length"));
                int carportHeight = Integer.parseInt(ctx.formParam("carport_height"));
                String note = ctx.formParam("note");

                //Shed data
                String shedChoice = ctx.formParam("redskabsrum");

                if (ctx.sessionAttribute("currentUser") == null) {
                    user = UserController.createUser(ctx);
                } else {
                    user = ctx.sessionAttribute("currentUser");
                    loggedIn = true;
                }

                //Create Carport instance from carport input data
                Carport carport = new Carport(carportWidth, carportLength, carportHeight);

                //Create Order instance from note
                Order order = new Order(note);

                if (shedChoice.equalsIgnoreCase("ja")) {
                    int shedWidth = Integer.parseInt(ctx.formParam("shed_width"));
                    int shedLength = Integer.parseInt(ctx.formParam("shed_length"));
                    Shed shed = new Shed(shedWidth, shedLength);
                    carport.setShed(shed);
                }

                if (loggedIn) {
                    DTOUserCarportOrder dto = new DTOUserCarportOrder(user, carport, order);
                    float carportPrice = Calculator.carportPriceCalculator2(dto);
                    OrderMapper.addOrderToExistingUser(dto, carportPrice, connectionPool);
                } else {
                    DTOUserCarportOrder dto = new DTOUserCarportOrder(user, carport, order);
                    float carportPrice = Calculator.carportPriceCalculator2(dto);
                    OrderMapper.addOrder(dto, carportPrice, connectionPool);
                }


                ctx.attribute("name", user.getName());
                ctx.attribute("length", carportLength);
                ctx.attribute("width", carportWidth);
                ctx.attribute("height", carportHeight);

                int id = user.getId();
                String name = user.getName();

                Locale.setDefault(new Locale("US"));
                CarportSvgTopView svg = new CarportSvgTopView(carportLength, carportWidth);
                ctx.attribute("svg", svg.toString());

                EmailController.sendOrderToSalesTeam(ctx, name, id);

                ctx.render("tilbud-indsendt.html");
            } catch (Exception e) {
                loadMeasurements(ctx, connectionPool);
                ctx.attribute("message", e.getMessage());
                ctx.render("bestilling.html");
            }


        }

     */
    public static void loadMeasurements(Context ctx, ConnectionPool connectionPool) {
        try {
            List<Integer> lengthList = MeasurementMapper.getAllLengths(connectionPool);
            List<Integer> widthList = MeasurementMapper.getAllWidths(connectionPool);
            List<Integer> heightList = MeasurementMapper.getAllHeights(connectionPool);

            ctx.attribute("lengthList", lengthList);
            ctx.attribute("widthList", widthList);
            ctx.attribute("heightList", heightList);

        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("fejlside.html");
        }
    }

    public static void renderOrderPage(Context ctx) {
        ctx.render("bestilling.html");
    }
}
