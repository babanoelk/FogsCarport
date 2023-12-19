package app.controllers;

import app.dtos.DTOUserCarportOrder;
import app.entities.Carport;
import app.entities.Order;
import app.entities.Shed;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.*;
import app.utility.SvgGenerator;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FormController {

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
                OrderMapper.addOrderToExistingUser(dto, connectionPool);
            } else {
                DTOUserCarportOrder dto = new DTOUserCarportOrder(user, carport, order);
                OrderMapper.addOrder(dto, connectionPool);
            }


            ctx.attribute("name", user.getName());
            ctx.attribute("length", carportLength);
            ctx.attribute("width", carportWidth);
            ctx.attribute("height", carportHeight);


            String svgContent = SvgGenerator.generateSvg(carportLength, carportWidth);

            Map<String, Object> model = new HashMap<>();
            model.put("svgContent", svgContent);

            EmailController.sendOrderToSalesTeam(ctx);

            ctx.render("tilbud-indsendt.html", model);
        } catch (Exception e) {
            loadMeasurements(ctx, connectionPool);
            ctx.attribute("message", e.getMessage());
            ctx.render("bestilling.html");
        }


    }

    public static void loadMeasurements(Context ctx, ConnectionPool connectionPool) {
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
