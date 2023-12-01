package app.controllers;

import app.entities.Carport;
import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.*;
import io.javalin.http.Context;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;


public class FormController {

    public static void formInput(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

        //Carport width & length
        int carportWidth = Integer.parseInt(ctx.formParam("carport_width"));
        int carportLength = Integer.parseInt(ctx.formParam("carport_length"));
        int carportHeight = Integer.parseInt(ctx.formParam("carport_height"));


        //Shed width & length
        //int shedWidth = Integer.parseInt(ctx.formParam("shed_width"));
        //int shedLength = Integer.parseInt(ctx.formParam("shed_length"));


        //User information
        String name = (ctx.formParams("fullname")).toString();
        String address = (ctx.formParams("address")).toString();
        int zip = Integer.parseInt(ctx.formParam("zip"));
        int mobile = Integer.parseInt(ctx.formParam("phone"));
        String email = (ctx.formParams("email")).toString();
        String password = (ctx.formParams("pass")).toString();
        boolean permission = Boolean.parseBoolean((ctx.formParams("permission")).toString());

        // Get the current LocalDateTime
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Convert LocalDateTime to Date
        Date currentDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());


        User user = new User(name, email, password, address, mobile, zip);
        Order order = new Order((java.sql.Date) currentDate, permission );
        Carport carport = new Carport(carportWidth,carportLength,carportHeight);


        UserMapper.addUser(user,connectionPool);
        OrderMapper.addOrder(order, user, carport, connectionPool);
        CarportMapper.addCarport(carport, connectionPool);
    }

    public static void loadMeasurements(Context ctx, ConnectionPool connectionPool){

        try {
            List<Integer> lengthList = MeasurementMapper.getAllLengths(connectionPool);
            List<Integer> widthList = MeasurementMapper.getAllWidths(connectionPool);
            List<Integer> heightList = MeasurementMapper.getAllHeights(connectionPool);

            ctx.attribute("lengthList", lengthList);
            ctx.attribute("widthList", widthList);
            ctx.attribute("heightList", heightList);

            ctx.render("bestilling.html");

        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("fejlside.html");
        }

    }

}
