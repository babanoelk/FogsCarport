package app.controllers;

import app.entities.Order;
import app.entities.User;
import app.persistence.ConnectionPool;
import io.javalin.http.Context;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


public class FormularController {

    public static void formularInput(Context ctx, ConnectionPool connectionPool) {

        //Carport width & length
        int carportWidth = Integer.parseInt(ctx.formParam("carport_bredde"));
        int carportLength = Integer.parseInt(ctx.formParam("carport_længde"));


        //Shed width & length
        int shedWidth = Integer.parseInt(ctx.formParam("Redskabsrum_bredde"));
        int shedLength = Integer.parseInt(ctx.formParam("Redskabsrum_længde"));


        //User information
        String name = (ctx.formParams("fullname")).toString();
        String address = (ctx.formParams("address")).toString();
        int postalCode = Integer.parseInt(ctx.formParam("postnumber"));
        int mobile = Integer.parseInt(ctx.formParam("tlfnumber"));
        String eMail = (ctx.formParams("email")).toString();
        String password = (ctx.formParams("email")).toString();
        boolean permission = Boolean.parseBoolean((ctx.formParams("permission")).toString());

        User user = new User(name, address, password, address, mobile, postalCode);

        // Get the current LocalDateTime
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Convert LocalDateTime to Date
        Date currentDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());

        Order order = new Order((java.sql.Date) currentDate, permission );
    }

}
