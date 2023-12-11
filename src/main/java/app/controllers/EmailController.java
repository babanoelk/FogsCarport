package app.controllers;

import app.utility.EmailFactory;
import io.javalin.http.Context;

import java.io.IOException;

public class EmailController {


    public static void sendOrderQuestion(Context ctx) throws Exception {

        String name = ctx.formParam("name");
        String customerMail = ctx.formParam("email");
        String message = ctx.formParam("message");
        String orderId = ctx.formParam("order_id");

        try {
            EmailFactory.sendOrderQuestion(name, orderId, customerMail, message);
            String confirmation = "Tak for din besked. Vi vender retur indenfor 24 timer.";
            ctx.attribute("confirmation", confirmation);
            ctx.render("kontakt.html");
        } catch (IOException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("kontakt.html");
        }

    }

}