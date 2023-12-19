package app.controllers;

import app.dtos.*;
import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.*;
import app.utility.Calculator;
import io.javalin.http.Context;

import java.util.List;

public class OrderController {


    public static void getChosenCustomerOrder(Context ctx, ConnectionPool connectionPool) {

        try {
            //Order ID
            int result = Integer.parseInt(ctx.formParam("order_id"));
            ctx.attribute("orderID", result);


            //Brugeren
            User chosenUser = OrderMapper.getOrderDetails(result, connectionPool);
            ctx.sessionAttribute("user", chosenUser);

            //Carporten
            Carport chosenUsersCarport = OrderMapper.getCarportByOrderId(result, connectionPool);
            ctx.sessionAttribute("old_carport", chosenUsersCarport);

            //Shed
            Shed chosenUsersShed = OrderMapper.getShedByOrderId(result, connectionPool);
            ctx.sessionAttribute("old_shed", chosenUsersShed);

            float price1 = Calculator.carportPriceCalculator(chosenUsersCarport);
            float price2 = Calculator.shedPriceCalculator(chosenUsersShed);
            float totalPrice = price1 + price2;

            //Load data
            ctx.sessionAttribute("totalPrice", totalPrice);
            FormController.loadMeasurements(ctx, connectionPool); //todo: double rendering conflict

            ctx.render("se-nærmere-på-ordre.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            //ctx.render(""); //todo: render page with error message
        }
    }

    public static boolean deleteOrder(Context ctx, ConnectionPool connectionPool) {
        try {
            int result = Integer.parseInt(ctx.formParam("order_id"));
            OrderMapper.deleteOrderByOrderID(result, connectionPool);

            ctx.render("ordre-slettet.html"); //todo: should not render its own page, replace with message
        } catch (DatabaseException e) {
            ctx.attribute("message", "Fejl sletning af ordre " + e.getMessage());
            //ctx.render(""); //todo: render page with error message
        }
        return true; //todo: why return something?
    }

    public static void getAllOrders(Context ctx, ConnectionPool connectionPool) {
        try {
            User user = ctx.sessionAttribute("currentUser");
            if (user.getRole() == 1) {
                List<Order> orders = OrderMapper.getAllOrdersByUser(user, connectionPool);
                ctx.attribute("orderlist", orders);
                ctx.render("ordre-side.html");
            } else {
                List<DTOStatus> statusList = StatusMapper.getAllStatuses(connectionPool);
                List<DTOOrderCustomer> allOrders = OrderMapper.getAllOrders(connectionPool);

                ctx.attribute("statusList", statusList);
                ctx.attribute("allOrders", allOrders);
                ctx.render("ordre-side.html");
            }
        } catch (DatabaseException e) {
            ctx.attribute("message", "Fejl indlæsning af ordre " + e.getMessage());
            //ctx.render(""); //todo: render page with error message
        }
    }

    public static void updateOrderStatus(Context ctx, ConnectionPool connectionPool) {
        try {
            int orderId = Integer.parseInt(ctx.formParam("order_id"));
            int statusId = Integer.parseInt(ctx.formParam("status_id"));
            OrderMapper.updateOrderStatus(orderId, statusId, connectionPool);
            getAllOrders(ctx, connectionPool);
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            //ctx.render(""); //todo: render page with error message
        }
    }

    public static void updateOrderUser(Context ctx, ConnectionPool connectionPool) {

        String updateName;
        String updateAddress;
        int updateZipcode;
        int updateMobile;
        String updateEmail;

        //The chosen users editable information page
        User user = ctx.sessionAttribute("user");

        //Old information
        String oldUserName = user.getName();
        String oldUserAddress = user.getAddress();
        int oldUserZipcode = user.getZipcode();
        int oldUserMobile = user.getMobile();
        String oldUserEmail = user.getEmail();

        //New information
        String newInputName = ctx.formParam("newName");
        String newInputAddress = ctx.formParam("newAddress");
        String newInputZipcodeStr = ctx.formParam("newZipcode");
        String newInputMobileStr = ctx.formParam("newMobile");
        String newInputEmail = ctx.formParam("newEmail");


        // Checking if name is null or empty
        if (newInputName != null && !newInputName.isEmpty()) {
            // The newInputName is not null and not empty
            updateName = newInputName;
        } else {
            // The newInputName is either null or empty
            updateName = oldUserName;
        }

        // Checking if address is null or empty
        if (newInputAddress != null && !newInputAddress.isEmpty()) {
            // The newInputAddress is not null and not empty
            updateAddress = newInputAddress;
        } else {
            // The newInputAddress is either null or empty
            updateAddress = oldUserAddress;
        }

        // Checking if zipcode is null or empty
        if (newInputZipcodeStr != null && !newInputZipcodeStr.isEmpty()) {
            // Attempt to parse the newInputZipcodeStr to an integer
            try {
                updateZipcode = Integer.parseInt(newInputZipcodeStr);
            } catch (NumberFormatException e) {
                // Handle the case where the string is not a valid integer
                // You might want to log an error or set a default value
                updateZipcode = oldUserZipcode; // Set a default value or use oldUserZipcode
            }
        } else {
            // The newInputZipcodeStr is either null or empty
            // Set a default value or use the oldUserZipcode
            updateZipcode = oldUserZipcode; // Set a default value or use oldUserZipcode
        }

// Checking if mobile is null or empty
        if (newInputMobileStr != null && !newInputMobileStr.isEmpty()) {
            // Attempt to parse the newInputMobileStr to an integer
            try {
                updateMobile = Integer.parseInt(newInputMobileStr);
            } catch (NumberFormatException e) {
                // Handle the case where the string is not a valid integer
                // You might want to log an error or set a default value
                updateMobile = oldUserMobile; // Set a default value or use oldUserMobile
            }
        } else {
            // The newInputMobileStr is either null or empty
            // Set a default value or use the oldUserMobile
            updateMobile = oldUserMobile; // Set a default value or use oldUserMobile
        }

// Checking if email is null or empty
        if (newInputEmail != null && !newInputEmail.isEmpty()) {
            // The newInputEmail is not null and not empty
            updateEmail = newInputEmail;
        } else {
            // The newInputEmail is either null or empty
            updateEmail = oldUserEmail;
        }

        User newUser = new User(user.getId(), updateName, updateEmail, updateAddress, updateMobile, updateZipcode);

        //Opdater brugere oplysningerne
        try {
            UserMapper.updateUser(newUser, connectionPool);
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            //ctx.render(""); //todo: render page with error message
        }

        int orderID = Integer.parseInt(ctx.formParam("orderID"));

        ctx.attribute("orderID", orderID);

        User oldUser = null;
        try {
            oldUser = OrderMapper.getOrderDetails(orderID, connectionPool);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
        ctx.sessionAttribute("user", oldUser);
        FormController.loadMeasurements(ctx, connectionPool);


        //Load the page
        ctx.render("se-nærmere-på-ordre.html"); //todo: rename html to something with admin
    }

    public static void updateCarport(Context ctx, ConnectionPool connectionPool) {

        int updateLength;
        int updateWidth;
        int updateHeight;

        Carport carport = ctx.sessionAttribute("old_carport");

        //Old information
        int oldCarportLength = carport.getLength();
        int oldCarportWidth = carport.getWidth();
        int oldCarportHeight = carport.getHeight();


        //New information
        String newInputLengthStr = ctx.formParam("newLength");
        String newInputWidthStr = ctx.formParam("newWidth");
        String newInputHeightStr = ctx.formParam("newHeight");

        if (newInputLengthStr != null && !newInputLengthStr.isEmpty()) {
            try {
                updateLength = Integer.parseInt(newInputLengthStr);
            } catch (NumberFormatException e) {
                updateLength = oldCarportLength;
            }
        } else {
            updateLength = oldCarportLength;
        }

        //Width
        if (newInputWidthStr != null && !newInputWidthStr.isEmpty()) {
            try {
                updateWidth = Integer.parseInt(newInputWidthStr);
            } catch (NumberFormatException e) {
                updateWidth = oldCarportWidth;
            }
        } else {
            updateWidth = oldCarportWidth;
        }

        if (newInputHeightStr != null && !newInputHeightStr.isEmpty()) {
            try {
                updateHeight = Integer.parseInt(newInputHeightStr);
            } catch (NumberFormatException e) {
                updateHeight = oldCarportHeight;
            }
        } else {
            updateHeight = oldCarportHeight;
        }

        int orderID = Integer.parseInt(ctx.formParam("orderID"));

        Carport newCarport = new Carport(carport.getId(), updateWidth, updateLength, updateHeight);
        User oldUser = null;
        Carport oldCarport = null;

        //Opdater brugere oplysningerne
        try {
            CarportMapper.updateCarport(newCarport, connectionPool);
            oldUser = OrderMapper.getOrderDetails(orderID, connectionPool);
            oldCarport = OrderMapper.getCarportByOrderId(orderID, connectionPool);

        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            //ctx.render(""); //todo: render page with error message
        }

        ctx.attribute("orderID", orderID);
        ctx.sessionAttribute("user", oldUser);
        ctx.sessionAttribute("old_carport", oldCarport);

        FormController.loadMeasurements(ctx, connectionPool); //todo: problem with double rendering

        //Load the page
        ctx.render("se-nærmere-på-ordre.html");
    }

    public static void updateShed(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

        int updateLength;
        int updateWidth;

        Shed shed = ctx.sessionAttribute("old_shed");

        //Old information
        int oldShedLength = shed.getLength();
        int oldShedWidth = shed.getWidth();


        //New information
        String newInputLengthStr = ctx.formParam("shed_length");
        String newInputWidthStr = ctx.formParam("shed_width");

        if (newInputLengthStr != null && !newInputLengthStr.isEmpty()) {
            try {
                updateLength = Integer.parseInt(newInputLengthStr);
            } catch (NumberFormatException e) {
                updateLength = oldShedLength;
            }
        } else {
            updateLength = oldShedLength;
        }

        //Width
        if (newInputWidthStr != null && !newInputWidthStr.isEmpty()) {
            try {
                updateWidth = Integer.parseInt(newInputWidthStr);
            } catch (NumberFormatException e) {
                updateWidth = oldShedWidth;
            }
        } else {
            updateWidth = oldShedWidth;
        }

        Shed newShed = new Shed(shed.getId(), updateWidth, updateLength);


        //Opdater brugere oplysningerne
        ShedMapper.updateShed(newShed, connectionPool);

        //int result = Integer.parseInt(ctx.formParam("orderID"));
        int orderID = Integer.parseInt(ctx.formParam("orderID"));

        ctx.attribute("orderID", orderID);

        User oldUser = OrderMapper.getOrderDetails(orderID, connectionPool);
        ctx.sessionAttribute("user", oldUser);

        Carport oldCarport = OrderMapper.getCarportByOrderId(orderID, connectionPool);
        ctx.sessionAttribute("old_carport", oldCarport);

        Shed oldShed = OrderMapper.getShedByOrderId(orderID, connectionPool);
        ctx.sessionAttribute("old_shed", oldShed);

        FormController.loadMeasurements(ctx, connectionPool);

        //Load the page
        ctx.render("se-nærmere-på-ordre.html");
    }

    public static void addShed(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

        int updateLength;
        int updateWidth;

        Shed shed = ctx.sessionAttribute("old_shed");

        //Old information
        int shedLength = shed.getLength();
        int shedWidth = shed.getWidth();


        //New information
        String newShedLengthAsStr = ctx.formParam("new_shed_length");
        String newShedWidthAsStr = ctx.formParam("new_shed_width");

        if (newShedLengthAsStr != null && !newShedLengthAsStr.isEmpty()) {
            try {
                updateLength = Integer.parseInt(newShedLengthAsStr);
            } catch (NumberFormatException e) {
                updateLength = shedLength;
            }
        } else {
            updateLength = shedLength;
        }

        //Width
        if (newShedWidthAsStr != null && !newShedWidthAsStr.isEmpty()) {
            try {
                updateWidth = Integer.parseInt(newShedWidthAsStr);
            } catch (NumberFormatException e) {
                updateWidth = shedWidth;
            }
        } else {
            updateWidth = shedWidth;
        }

        Shed newShed = new Shed(updateWidth, updateLength);

        //Opdater brugere oplysningerne
        Shed updatedShed = ShedMapper.addShed(newShed, connectionPool);

        int orderId = Integer.parseInt(ctx.formParam("orderID"));

        ctx.attribute("orderID", orderId);

        Carport carport = OrderMapper.getCarportByOrderId(orderId, connectionPool);
        carport.setShed(updatedShed);
        ctx.sessionAttribute("old_carport", carport);

        //Update Order with new created Shed
        OrderMapper.addShedToOrder(carport, connectionPool);

        User user = OrderMapper.getOrderDetails(orderId, connectionPool);
        ctx.sessionAttribute("user", user);

        ctx.sessionAttribute("old_shed", updatedShed);

        FormController.loadMeasurements(ctx, connectionPool); //todo: problem with double rendering

        ctx.render("se-nærmere-på-ordre.html");
    }

    public static void orderContact(Context ctx) {
        int orderId = Integer.parseInt(ctx.formParam("order_id"));
        ctx.attribute("order_id", orderId);

        ctx.render("kontakt.html");
    }

    public static void sendBill(Context ctx, ConnectionPool connectionPool) {
        try {
            //OrderController.getAllOrders(ctx, connectionPool);
            String message = "Regningen er nu sendt afsted.";
            ctx.attribute("message", message);
            int order_ID = Integer.parseInt(ctx.formParam("orderID"));
            OrderMapper.updateStatusBillSent(order_ID, connectionPool);


            //Update price
            float updatePrice;

            //Old price
            float oldPrice = Float.parseFloat((ctx.formParam("total_price")));

            String newInputPrice = ctx.formParam("newPrice");


            //New information
            if (newInputPrice != null && !newInputPrice.isEmpty()) {
                try {
                    updatePrice = Integer.parseInt(newInputPrice);
                } catch (NumberFormatException e) {
                    updatePrice = oldPrice;
                }
            } else {
                updatePrice = oldPrice;
            }



            OrderMapper.updateOrderPrice(order_ID, updatePrice, connectionPool);

            EmailController.sendBill(ctx);
            OrderController.getAllOrders(ctx, connectionPool);
        } catch (Exception e) {
            ctx.attribute("message", e.getMessage());
            OrderController.getAllOrders(ctx, connectionPool);
        }
    }
    public static void changePriceManually(Context ctx, ConnectionPool connectionPool) {
        try {
            int order_ID = Integer.parseInt(ctx.formParam("orderID"));
            float firstPrice = Float.parseFloat(ctx.formParam("total_price"));
            String newInputPrice = ctx.formParam("changePrice");

            float changePrice;
            if (newInputPrice != null && !newInputPrice.isEmpty()) {
                try {
                    changePrice = Float.parseFloat(newInputPrice);
                } catch (NumberFormatException e) {
                    changePrice = firstPrice;
                }
            } else {
                changePrice = firstPrice;
            }

            OrderMapper.updateOrderPrice(order_ID, changePrice, connectionPool);
            OrderController.getChosenCustomerOrder(ctx, connectionPool);
        } catch (NumberFormatException e) {
            ctx.attribute("message", e.getMessage());
        }
    }


}
