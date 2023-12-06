package app.controllers;

import app.entities.User;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.http.Context;

public class UserController {

    public static void login(Context ctx, ConnectionPool connectionPool){
        try {
            String email = ctx.formParam("email");
            String password = ctx.formParam("password");
            boolean isLoggedIn = UserMapper.loginValidator(email, password, connectionPool);
            if (isLoggedIn) {
                User user = UserMapper.getUserByEmail(email, connectionPool);
                ctx.sessionAttribute("currentUser", user);
                OrderController.getAllOrders(ctx,connectionPool);
                //ctx.render("min-side.html");
            } else {
                ctx.attribute("message", "Forkert email eller password");
                ctx.render("login.html");
            }
        } catch (Exception e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("login.html");
        }
    }
}
