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
import io.javalin.http.Context;

import java.util.List;
import java.util.Locale;


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

            Locale.setDefault(new Locale("US"));
            CarportSvgTopView svg = new CarportSvgTopView(carportLength, carportWidth);
            ctx.attribute("svg", svg.toString());

            //SLUT: Ahmads SVG

            EmailController.sendOrderToSalesTeam(ctx);

            ctx.render("tilbud-indsendt.html");
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
