package app.controllers;

import app.entities.Carport;
import app.entities.Order;
import app.entities.Shed;
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
            //Carport data
            int carportWidth = Integer.parseInt(ctx.formParam("carport_width"));
            int carportLength = Integer.parseInt(ctx.formParam("carport_length"));
            int carportHeight = Integer.parseInt(ctx.formParam("carport_height"));
            String note = ctx.formParam("note");

            //Shed data
            String shedChoice = ctx.formParam("redskabsrum");

            //User data
            String name = ctx.formParam("name");
            String address = ctx.formParam("address");
            int zip = Integer.parseInt(ctx.formParam("zip"));
            int mobile = Integer.parseInt(ctx.formParam("phone"));
            String email = ctx.formParam("email");
            String password = (ctx.formParam("pass"));
            //todo: boolean permission = Boolean.parseBoolean(ctx.formParam("permission"));

            //Create User instance from input data
            User user = new User(name, email, password, address, mobile, zip);
            User user1 = UserMapper.addUser(user,connectionPool);

            //Create Carport instance from carport input data
            Carport carport = new Carport(carportWidth, carportLength, carportHeight);
            Carport carport1 = CarportMapper.addCarport(carport, connectionPool);

            //Create Order instance from note
            Order order = new Order(note);

            if(shedChoice.equalsIgnoreCase("ja")){
                int shedWidth = Integer.parseInt(ctx.formParam("shed_width"));
                int shedLength = Integer.parseInt(ctx.formParam("shed_length"));
                Shed shed = new Shed(shedWidth, shedLength);
                Shed shed1 = CarportMapper.addShed(shed, connectionPool);
                carport1.setShed(shed1);
            }

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
