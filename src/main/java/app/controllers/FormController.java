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

    public static void formInput(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

        User user;
        String name = null;

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
                //User data
                name = ctx.formParam("name");
                String address = ctx.formParam("address");
                int zip = Integer.parseInt(ctx.formParam("zip"));
                int mobile = Integer.parseInt(ctx.formParam("phone"));
                String email = ctx.formParam("email");
                String password = (ctx.formParam("pass"));
                boolean consent = Boolean.parseBoolean(ctx.formParam("consent"));
                int role = 1; //Standard role 'Kunde'

                //Create User instance from input data
                user = new User(name, email, password, address, mobile, zip, consent, role);
            } else {
                user = ctx.sessionAttribute("currentUser");
                name = user.getName();
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


            ctx.attribute("name", name);
            ctx.attribute("length", carportLength);
            ctx.attribute("width", carportWidth);
            ctx.attribute("height", carportHeight);


            //START: Ahmads SVG
            String svgText = "<svg width=\"255\" height=210>\n" +
                    "   <rect x=\"0\" y=\"0\" height=\"90\" width=\"90\"\n" +
                    "        style=\"stroke:#000000; fill: #ff0000\"/>\n" +
                    "   <rect x=\"120\" y=\"0\" height=\"90\" width=\"135\"\n" +
                    "        style=\"stroke:#000000; fill: #ff0000\"/>\n" +
                    "   <rect x=\"0\" y=\"120\" height=\"90\" width=\"90\"\n" +
                    "        style=\"stroke:#000000; fill: #ff0000\"/>\n" +
                    "   <rect x=\"120\" y=\"120\" height=\"90\" width=\"135\"\n" +
                    "        style=\"stroke:#000000; fill: #ff0000\"/>\n" +
                    "</svg>";

            ctx.attribute("svg",svgText);

            //SLUT: Ahmads SVG



            //START: Mustafa måde at gøre det på. Skal slettes når Ahmar har fået det andet oppe og køre.
            String svgContent = SvgGenerator.generateSvg(carportLength, carportWidth);

            Map<String, Object> model = new HashMap<>();
            model.put("svgContent", svgContent);

            //SLUT: Mustafa måde at gøre det på. Skal slettes når Ahmar har fået det andet oppe og køre.
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
            //Carport data
            List<Integer> lengthList = MeasurementMapper.getAllLengths(connectionPool);
            List<Integer> widthList = MeasurementMapper.getAllWidths(connectionPool);
            List<Integer> heightList = MeasurementMapper.getAllHeights(connectionPool);

            ctx.attribute("lengthList", lengthList);
            ctx.attribute("widthList", widthList);
            ctx.attribute("heightList", heightList);

            //Shed data:

            ctx.render("bestilling.html");

        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("fejlside.html");
        }

    }

}
