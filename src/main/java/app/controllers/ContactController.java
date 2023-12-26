package app.controllers;

import io.javalin.http.Context;

public class ContactController {
    public static void contact(Context ctx){

        String name = ctx.formParam("name");
        String phone = ctx.formParam("phone");
        String email = ctx.formParam("email");
        String message = ctx.formParam("message");

        try {
            EmailController.sendMessageToSalesTeam(ctx, name, phone, email, message);
        } catch (Exception e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("fejlside.html");
        }

    }

}
