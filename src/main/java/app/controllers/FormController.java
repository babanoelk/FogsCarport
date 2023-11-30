package app.controllers;

import app.entities.Order;
import app.entities.User;
import app.persistence.ConnectionPool;
import io.javalin.http.Context;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


public class FormController {

    public static void formInput(Context ctx, ConnectionPool connectionPool) {

        //Carport width & length
        int carportWidth = Integer.parseInt(ctx.formParam("carport_width"));
        int carportLength = Integer.parseInt(ctx.formParam("carport_length"));


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

        User user = new User(name, email, password, address, mobile, zip);

        // Get the current LocalDateTime
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Convert LocalDateTime to Date
        Date currentDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());

        Order order = new Order((java.sql.Date) currentDate, permission );
    }

}
