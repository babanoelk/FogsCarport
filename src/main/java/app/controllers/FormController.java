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


        try {
            //Carport width & length
            int carportWidth = Integer.parseInt(ctx.formParam("carport_width"));
            int carportLength = Integer.parseInt(ctx.formParam("carport_length"));
            int carportHeight = Integer.parseInt(ctx.formParam("carport_height"));
            String note = ctx.formParam("note");


            //Shed width & length
            //int shedWidth = Integer.parseInt(ctx.formParam("shed_width"));
            //int shedLength = Integer.parseInt(ctx.formParam("shed_length"));


            //User information
            String name = ctx.formParam("name");
            String address = ctx.formParam("address");
            int zip = Integer.parseInt(ctx.formParam("zip"));
            int mobile = Integer.parseInt(ctx.formParam("phone"));
            String email = ctx.formParam("email");
            String password = (ctx.formParam("pass"));

            //boolean permission = Boolean.parseBoolean(ctx.formParam("permission"));

            /*
            // Get the current LocalDateTime
            LocalDateTime currentDateTime = LocalDateTime.now();

            // Convert LocalDateTime to Date
            Date currentDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());

  */
            User user = new User(name, email, password, address, mobile, zip);
            Order order = new Order(note);
            Carport carport = new Carport(carportWidth,carportLength,carportHeight);

            User user1 = UserMapper.addUser(user,connectionPool);
            Carport carport1 = CarportMapper.addCarport(carport, connectionPool);

            OrderMapper.addOrder(order, user1, carport1, connectionPool);

            ctx.attribute("name", name);
            ctx.attribute("length", carportLength);
            ctx.attribute("width", carportWidth);

            ctx.render("tilbud-indsendt.html");
        } catch (Exception e) {
            loadMeasurements(ctx, connectionPool);
            ctx.attribute("message", e.getMessage());
            ctx.render("bestilling.html");
        }


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
