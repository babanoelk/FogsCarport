package app.controllers;

import app.dtos.*;
import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.*;
import app.utility.Calculator;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

public class OrderController {


    public static void getSpecificOrder(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

        try {
            //Order ID
            int result = Integer.parseInt(ctx.formParam("order_id"));
            ctx.attribute("orderID", result);


            //Brugeren
            DTOUserWithUserIdNameAddressZipcodeMobileEmail oldUser = OrderMapper.getSpecificOrderByOrderIdWithUserAndCarportAndShed(result, connectionPool);
            ctx.sessionAttribute("user", oldUser);

            //Carporten
            DTOCarportWithIdLengthWidthHeight oldCarport = OrderMapper.getSpecificCarportByOrderId(result, connectionPool);
            ctx.sessionAttribute("old_carport", oldCarport);

            //Shed
            DTOShedIdLengthWidth oldShed = OrderMapper.getSpecificShedByOrderId(result, connectionPool);
            ctx.sessionAttribute("old_shed", oldShed);

            float price1 = Calculator.carportPriceCalculator(oldCarport);
            float price2 = Calculator.shedPriceCalculator(oldShed);
            float totalPrice = price1 + price2;

            //Load data
            ctx.sessionAttribute("totalPrice", totalPrice);
            FormController.loadMeasurements(ctx, connectionPool);

            ctx.render("se-nærmere-på-ordre.html");
        } catch (DatabaseException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public static boolean deleteOrder(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

        try {
            User user = ctx.sessionAttribute("currentUser");
            int result = Integer.parseInt(ctx.formParam("order_id"));
            OrderMapper.deleteOrderByOrderID(result, connectionPool);

            ctx.render("ordre-slettet.html");
            return true;
        } catch (DatabaseException e) {
            throw new DatabaseException("Fejl sletning af ordre " + e.getMessage());
        }
    }

    public static void getAllOrders(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        try {
            User user = ctx.sessionAttribute("currentUser");
            if (user.getRole() == 1) {
                List<DTOGetOrderWithIdDateCustomerNoteConsentStatus> orders = OrderMapper.getAllOrdersByUser(user, connectionPool);
                ctx.attribute("orderlist", orders);
                ctx.render("ordre-side.html");
            } else {
                List<Status> statusList = StatusMapper.getAllStatuses(connectionPool);
                List<DTOOrderCustomer> allOrders = OrderMapper.getAllOrders(connectionPool);
                //ctx.sessionAttribute("currentSession", "all");

                ctx.attribute("statusList", statusList);
                ctx.attribute("allOrders", allOrders);
                ctx.render("ordre-side.html");
            }
        } catch (DatabaseException e) {
            throw new DatabaseException("Fejl ved indlæsning af kundeordre " + e.getMessage());
        }
    }

    public static void updateOrderStatus(Context ctx, ConnectionPool connectionPool) {
        try {
            int orderId = Integer.parseInt(ctx.formParam("order_id"));
            int statusId = Integer.parseInt(ctx.formParam("status_id"));
            OrderMapper.updateOrderStatus(orderId, statusId, connectionPool);
            getAllOrders(ctx, connectionPool);
        } catch (DatabaseException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateOrderUser(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

        String updateName;
        String updateAddress;
        int updateZipcode;
        int updateMobile;
        String updateEmail;

        DTOUserWithUserIdNameAddressZipcodeMobileEmail dto = ctx.sessionAttribute("user");

        //Old information
        String oldUserName = dto.getName();
        String oldUserAddress = dto.getAddress();
        int oldUserZipcode = dto.getZipcode();
        int oldUserMobile = dto.getMobile();
        String oldUserEmail = dto.getEmail();

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

        DTOUserWithUserIdNameAddressZipcodeMobileEmail dtoUserWithUserIdNameAddressZipcodeMobileEmail = new DTOUserWithUserIdNameAddressZipcodeMobileEmail
                (dto.getUserId(), updateName, updateAddress, updateZipcode, updateMobile, updateEmail);

        //Opdater brugere oplysningerne
        UserMapper.updateUser(dtoUserWithUserIdNameAddressZipcodeMobileEmail, connectionPool);


        //int result = Integer.parseInt(ctx.formParam("orderID"));
        int orderID = Integer.parseInt(ctx.formParam("orderID"));


        ctx.attribute("orderID", orderID);

        DTOUserWithUserIdNameAddressZipcodeMobileEmail oldUser = OrderMapper.getSpecificOrderByOrderIdWithUserAndCarportAndShed(orderID, connectionPool);
        ctx.sessionAttribute("user", oldUser);
        FormController.loadMeasurements(ctx, connectionPool);


        //Load the page
        ctx.render("se-nærmere-på-ordre.html");
    }

    public static void updateCarport(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

        int updateLength;
        int updateWidth;
        int updateHeight;

        DTOCarportWithIdLengthWidthHeight dto = ctx.sessionAttribute("old_carport");

        //Old information
        int oldCarportLength = dto.getLength();
        int oldCarportWidth = dto.getWidth();
        int oldCarportHeight = dto.getHeight();


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

        DTOCarportWithIdLengthWidthHeight carportWithIdLengthWidthHeight = new DTOCarportWithIdLengthWidthHeight
                (dto.getId(), updateLength, updateWidth, updateHeight);

        //Opdater brugere oplysningerne
        CarportMapper.updateCarport(carportWithIdLengthWidthHeight, connectionPool);


        //int result = Integer.parseInt(ctx.formParam("orderID"));
        int orderID = Integer.parseInt(ctx.formParam("orderID"));


        ctx.attribute("orderID", orderID);

        DTOUserWithUserIdNameAddressZipcodeMobileEmail oldUser = OrderMapper.getSpecificOrderByOrderIdWithUserAndCarportAndShed(orderID, connectionPool);
        ctx.sessionAttribute("user", oldUser);

        DTOCarportWithIdLengthWidthHeight oldCarport = OrderMapper.getSpecificCarportByOrderId(orderID, connectionPool);
        ctx.sessionAttribute("old_carport", oldCarport);

        FormController.loadMeasurements(ctx, connectionPool);


        //Load the page
        ctx.render("se-nærmere-på-ordre.html");
    }

    public static void updateShed(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

        int updateLength;
        int updateWidth;

        DTOShedIdLengthWidth dto = ctx.sessionAttribute("old_shed");

        //Old information
        int oldShedLength = dto.getLength();
        int oldShedWidth = dto.getWidth();


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

        DTOShedIdLengthWidth shedIdLengthWidth = new DTOShedIdLengthWidth
                (dto.getId(), updateLength, updateWidth);


        //Opdater brugere oplysningerne
        ShedMapper.updateShed(shedIdLengthWidth, connectionPool);

        //int result = Integer.parseInt(ctx.formParam("orderID"));
        int orderID = Integer.parseInt(ctx.formParam("orderID"));

        ctx.attribute("orderID", orderID);

        DTOUserWithUserIdNameAddressZipcodeMobileEmail oldUser = OrderMapper.getSpecificOrderByOrderIdWithUserAndCarportAndShed(orderID, connectionPool);
        ctx.sessionAttribute("user", oldUser);

        DTOCarportWithIdLengthWidthHeight oldCarport = OrderMapper.getSpecificCarportByOrderId(orderID, connectionPool);
        ctx.sessionAttribute("old_carport", oldCarport);

        DTOShedIdLengthWidth oldShed = OrderMapper.getSpecificShedByOrderId(orderID, connectionPool);
        ctx.sessionAttribute("old_shed", oldShed);

        FormController.loadMeasurements(ctx, connectionPool);

        //Load the page
        ctx.render("se-nærmere-på-ordre.html");
    }

    public static void addShed(Context ctx, ConnectionPool connectionPool) throws DatabaseException {


        //ShedMapper.addShed()
        int updateLength;
        int updateWidth;

        DTOShedIdLengthWidth dto = ctx.sessionAttribute("old_shed");

        //Old information
        int oldShedLength = dto.getLength();
        int oldShedWidth = dto.getWidth();


        //New information
        String newInputLengthStr = ctx.formParam("new_shed_length");
        String newInputWidthStr = ctx.formParam("new_shed_width");

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

        DTOShedLengthWidth shedLengthWidth = new DTOShedLengthWidth
                (updateLength, updateWidth);


        //Opdater brugere oplysningerne
        Shed newShed = ShedMapper.addShed(shedLengthWidth, connectionPool);

        //int result = Integer.parseInt(ctx.formParam("orderID"));
        int orderID = Integer.parseInt(ctx.formParam("orderID"));

        ctx.attribute("orderID", orderID);

        DTOCarportWithIdLengthWidthHeight oldCarport = OrderMapper.getSpecificCarportByOrderId(orderID, connectionPool);
        ctx.sessionAttribute("old_carport", oldCarport);

        //Update Order with new created Shed
        OrderMapper.addShedToSpecificOrderId(oldCarport.getId(), newShed.getId(), connectionPool);

        DTOUserWithUserIdNameAddressZipcodeMobileEmail oldUser = OrderMapper.getSpecificOrderByOrderIdWithUserAndCarportAndShed(orderID, connectionPool);
        ctx.sessionAttribute("user", oldUser);

        DTOShedIdLengthWidth oldShed = OrderMapper.getSpecificShedByOrderId(orderID, connectionPool);
        oldShed.setId(newShed.getId());
        oldShed.setLength(newShed.getLength());
        oldShed.setWidth(newShed.getWidth());

        ctx.sessionAttribute("old_shed", oldShed);

        FormController.loadMeasurements(ctx, connectionPool);

        //Load the page
        ctx.render("se-nærmere-på-ordre.html");
    }

    public static void orderContact(Context ctx) {
        int orderId = Integer.parseInt(ctx.formParam("order_id"));
        ctx.attribute("order_id", orderId);

        ctx.render("kontakt.html");
    }

    public static void sendBill(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

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

            //Order order = OrderMapper.getOrderById(order_ID, connectionPool);
            //int carportId = order.getCarportId();

            int carportId = Integer.parseInt(ctx.formParam("carportId"));

            Carport carport = CarportMapper.getCarportById(carportId, connectionPool);

            List <Part> listOfParts = new ArrayList<>(Calculator.calculateParts(carport, order_ID));

            MaterialMapper.addpartsList(listOfParts, connectionPool);

            EmailController.sendBill(ctx);
            OrderController.getAllOrders(ctx, connectionPool);
        } catch (Exception e) {
            ctx.attribute("message", e.getMessage());
            OrderController.getAllOrders(ctx, connectionPool);
        }
    }

    public static void changePriceManually(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
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
            OrderController.getSpecificOrder(ctx, connectionPool);
        } catch (NumberFormatException e) {
            ctx.attribute("message", e.getMessage());
        }
    }


}
